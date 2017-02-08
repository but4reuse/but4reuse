package org.but4reuse.feature.location.tf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adapters.IElement;
import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.feature.location.LocatedFeature;
import org.but4reuse.feature.location.LocatedFeaturesUtils;
import org.but4reuse.feature.location.impl.StrictFeatureSpecificFeatureLocation;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.wordclouds.util.TermFrequencyUtils;
import org.eclipse.core.runtime.IProgressMonitor;

public class SFS_SharedTerm implements IFeatureLocation {

	@Override
	public List<LocatedFeature> locateFeatures(FeatureList featureList, AdaptedModel adaptedModel,
			IProgressMonitor monitor) {

		// Get SFS results, all located feature are 1 confidence
		StrictFeatureSpecificFeatureLocation sfs = new StrictFeatureSpecificFeatureLocation();
		List<LocatedFeature> sfsLocatedBlocks = sfs.locateFeatures(featureList, adaptedModel, monitor);

		List<LocatedFeature> locatedFeatures = new ArrayList<LocatedFeature>();

		// Get all the features of a given block and all its elements
		for (Block block : adaptedModel.getOwnedBlocks()) {
			monitor.subTask("Feature location FCA SFS and Shared Term. Checking Elements at " + block.getName());
			List<Feature> blockFeatures = LocatedFeaturesUtils.getFeaturesOfBlock(sfsLocatedBlocks, block);
			List<IElement> blockElements = AdaptedModelHelper.getElementsOfBlock(block);

			// Cache of feature words
			Map<Feature,List<String>> fWordsMap = new HashMap<Feature,List<String>>();
			// For each element, we associate it to the feature with has at least one shared term
			for (IElement e : blockElements) {
				List<Feature> maxFeatures = new ArrayList<Feature>();
				for (Feature f : blockFeatures) {
					List<String> featureWords = fWordsMap.get(f);
					if (featureWords == null) {
						featureWords = TermFrequencyUtils.getFeatureWords(f);
						fWordsMap.put(f, featureWords);
					}
					List<String> elementWords = TermFrequencyUtils.getElementWords(e);
					int tf = TermFrequencyUtils.calculateTermFrequency(featureWords, elementWords);
					if (tf > 0) {
						maxFeatures.add(f);
					}
				}
				// Add to the located features
				for (Feature f : maxFeatures) {
					locatedFeatures.add(new LocatedFeature(f, e, 1));
				}
			}
		}

		// System.out.println(locatedFeatures.size());
		return locatedFeatures;
	}

}