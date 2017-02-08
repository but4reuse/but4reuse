package org.but4reuse.feature.location.impl;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.impl.ConstraintsHelper;
import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.feature.location.LocatedFeature;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Feature-Specific feature location heuristic Only those Blocks that are ALWAYS
 * for a given Feature are interesting.
 * 
 * @author jabier.martinez
 */
public class FeatureSpecificHeuristicFeatureLocation implements IFeatureLocation {

	@Override
	public List<LocatedFeature> locateFeatures(FeatureList featureList, AdaptedModel adaptedModel,
			IProgressMonitor monitor) {
		List<LocatedFeature> locatedFeatures = new ArrayList<LocatedFeature>();
		String[][] matrix = ConstraintsHelper.createMatrixOfPresenceOfBlocksInFeatures(featureList, adaptedModel);
		List<IConstraint> constraints = ConstraintsHelper.getCalculatedConstraints(adaptedModel);
		// for each feature
		for (int r = 1; r < matrix.length; r++) {
			if (monitor.isCanceled()) {
				return locatedFeatures;
			}
			Feature feature = featureList.getOwnedFeatures().get(r - 1);
			monitor.subTask("Locating " + feature.getName());

			List<Block> blocks = new ArrayList<Block>();

			String[] cells = matrix[r];
			for (int ce = cells.length - 1; ce > 0; ce--) {
				// Only those Blocks that are ALWAYS
				if (Double.parseDouble(matrix[r][ce]) == 1) {
					Block block = adaptedModel.getOwnedBlocks().get(ce - 1);
					blocks.add(block);
				}
			}

			// This is a hook for the non conservative version
			post(constraints, blocks);

			for (Block b : blocks) {
				// add the located features
				locatedFeatures.add(new LocatedFeature(feature, b, 1));
			}
		}
		return locatedFeatures;
	}

	/**
	 * This method is intended to be overriden
	 * 
	 * @param constraints
	 * @param blocks
	 */
	public void post(List<IConstraint> constraints, List<Block> blocks) {
		// Do nothing
	}
}
