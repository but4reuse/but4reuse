package org.but4reuse.feature.location.impl;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.feature.location.LocatedFeature;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Feature Specific Feature Location
 * 
 * @author jabier.martinez
 */
public class StrictFeatureSpecificFeatureLocation implements IFeatureLocation {

	@Override
	public List<LocatedFeature> locateFeatures(FeatureList featureList, AdaptedModel adaptedModel,
			IProgressMonitor monitor) {
		List<LocatedFeature> locatedFeatures = new ArrayList<LocatedFeature>();
		for (Feature f : featureList.getOwnedFeatures()) {
			// user cancel
			if (monitor.isCanceled()) {
				return locatedFeatures;
			}

			List<Block> featureBlocks = null;
			// Here we calculate the common blocks of the artefacts that
			// implements the features
			for (Artefact a : f.getImplementedInArtefacts()) {
				AdaptedArtefact aa = AdaptedModelHelper.getAdaptedArtefact(adaptedModel, a);
				if (featureBlocks == null) {
					featureBlocks = new ArrayList<Block>();
					featureBlocks = AdaptedModelHelper.getBlocksOfAdaptedArtefact(aa);
				} else {
					List<Block> blocksOfAA = AdaptedModelHelper.getBlocksOfAdaptedArtefact(aa);
					featureBlocks.retainAll(blocksOfAA);
				}
			}
			// Remove the Blocks that are already present in other artefacts
			// that do not implement the feature
			for (AdaptedArtefact aa : adaptedModel.getOwnedAdaptedArtefacts()) {
				if (!f.getImplementedInArtefacts().contains(aa.getArtefact())) {
					List<Block> blocksOfAA = AdaptedModelHelper.getBlocksOfAdaptedArtefact(aa);
					if (featureBlocks != null) {
						featureBlocks.removeAll(blocksOfAA);
					}
				}
			}
			if (featureBlocks != null) {
				for (Block commonBlock : featureBlocks) {
					// Add the located features
					locatedFeatures.add(new LocatedFeature(f, commonBlock, 1));
				}
			}
		}
		return locatedFeatures;
	}

}
