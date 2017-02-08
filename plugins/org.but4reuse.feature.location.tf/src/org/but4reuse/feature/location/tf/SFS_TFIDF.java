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
import org.mcavallo.opencloud.Cloud;

public class SFS_TFIDF implements IFeatureLocation {

	@Override
	public List<LocatedFeature> locateFeatures(FeatureList featureList, AdaptedModel adaptedModel,
			IProgressMonitor monitor) {

		// Get SFS results, all located feature are 1 confidence
		StrictFeatureSpecificFeatureLocation sfs = new StrictFeatureSpecificFeatureLocation();
		List<LocatedFeature> sfsLocatedBlocks = sfs.locateFeatures(featureList, adaptedModel, monitor);

		List<LocatedFeature> locatedFeatures = new ArrayList<LocatedFeature>();

		// Get all the features of a given block and all its elements
		for (Block block : adaptedModel.getOwnedBlocks()) {
			// user cancel
			if (monitor.isCanceled()) {
				return locatedFeatures;
			}
			
			monitor.subTask("Feature location FCA SFS and tf-idf. Features competing for Elements at "
					+ block.getName() + " /" + adaptedModel.getOwnedBlocks().size());
			List<Feature> blockFeatures = LocatedFeaturesUtils.getFeaturesOfBlock(sfsLocatedBlocks, block);
			List<IElement> blockElements = AdaptedModelHelper.getElementsOfBlock(block);

			// cache for feature tfidf clouds
			Map<Feature, Cloud> fCloudMap = new HashMap<Feature, Cloud>();
			// For each element, we associate it to the feature with higher tf
			for (IElement e : blockElements) {
				double maxTFIDFfound = 0.0;
				List<Feature> maxFeatures = new ArrayList<Feature>();
				for (Feature f : blockFeatures) {
					Cloud tfidfFCloud = fCloudMap.get(f);
					if (tfidfFCloud == null) {
						tfidfFCloud = TermFrequencyUtils.createFeatureTfIdfCloud(f, blockFeatures);
						fCloudMap.put(f, tfidfFCloud);
					}
					double tfidf = TermFrequencyUtils.calculateTfIdf(tfidfFCloud, e);
					if (tfidf == maxTFIDFfound) {
						maxFeatures.add(f);
					} else if (tfidf > maxTFIDFfound) {
						maxFeatures.clear();
						maxFeatures.add(f);
						maxTFIDFfound = tfidf;
					}
				}
				// Add to the located features
				for (Feature f : maxFeatures) {
					locatedFeatures.add(new LocatedFeature(f, e, 1));
				}
			}
		}
		return locatedFeatures;
	}

}