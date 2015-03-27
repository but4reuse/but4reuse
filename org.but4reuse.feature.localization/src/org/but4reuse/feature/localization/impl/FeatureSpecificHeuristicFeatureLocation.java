package org.but4reuse.feature.localization.impl;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.impl.ConstraintsHelper;
import org.but4reuse.feature.localization.IFeatureLocation;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Feature-Specific feature location heuristic Only those Blocks that are ALWAYS
 * for a given Feature are interesting. From this set of Blocks we create a
 * Requires Graph and we remove the ones with in-degree > 0
 * 
 * @author jabier.martinez
 */
public class FeatureSpecificHeuristicFeatureLocation implements IFeatureLocation {

	@Override
	public void locateFeatures(FeatureList featureList, AdaptedModel adaptedModel, IProgressMonitor monitor) {
		String[][] matrix = ConstraintsHelper.createMatrixOfPresenceOfBlocksInFeatures(featureList, adaptedModel);
		List<IConstraint> constraints = ConstraintsHelper.getCalculatedConstraints(adaptedModel);
		// for each feature
		for (int r = 1; r < matrix.length; r++) {
			if (monitor.isCanceled()) {
				return;
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

			// Calculate reduced list
			List<Block> toBeRemoved = new ArrayList<Block>();
			for (Block block : blocks) {
				for (IConstraint c : constraints) {
					if (c.getType().equals(IConstraint.REQUIRES)) {
						if (c.getBlock2().equals(block) && blocks.contains(c.getBlock1())) {
							toBeRemoved.add(block);
							break;
						}
					}
				}
			}
			blocks.removeAll(toBeRemoved);
			for (Block b : blocks) {
				b.getCorrespondingFeatures().add(feature);
			}
		}
	}
}
