package org.but4reuse.feature.location.lsi;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adapters.IElement;
import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.feature.location.LocatedFeature;
import org.but4reuse.feature.location.LocatedFeaturesUtils;
import org.but4reuse.feature.location.impl.StrictFeatureSpecificFeatureLocation;
import org.but4reuse.feature.location.lsi.activator.Activator;
import org.but4reuse.feature.location.lsi.preferences.LSIPreferencePage;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.wordclouds.filters.IWordsProcessing;
import org.but4reuse.wordclouds.filters.WordCloudFiltersHelper;
import org.eclipse.core.runtime.IProgressMonitor;

import lsi4j.LSI4J;

/**
 * SFS + LSI. Features identified as relevant for a block through SFS are then
 * transformed into LSI documents. Then, block elements are used as query and
 * the document (feature) with highest similarity is used as located feature for
 * the element.
 * 
 * @author jabier.martinez
 * @author Nicolas Ordonez Chala
 */
public class SFS_LSI implements IFeatureLocation {

	@Override
	public List<LocatedFeature> locateFeatures(FeatureList featureList, AdaptedModel adaptedModel,
			IProgressMonitor monitor) {

		// Preferences
		boolean fixed = Activator.getDefault().getPreferenceStore().getBoolean(LSIPreferencePage.FIXED);
		double approximationValue = Activator.getDefault().getPreferenceStore().getDouble(LSIPreferencePage.DIM);
		int approximationType;
		if (fixed) {
			approximationType = LSI4J.APPROXIMATION_K_VALUE;
		} else {
			approximationType = LSI4J.APPROXIMATION_PERCENTAGE;
		}

		List<IWordsProcessing> wordProcessors = WordCloudFiltersHelper.getSortedSelectedFilters();

		List<LocatedFeature> locatedFeatures = locateFeatures(featureList, adaptedModel, approximationType,
				approximationValue, wordProcessors, monitor);
		return locatedFeatures;
	}

	public List<LocatedFeature> locateFeatures(FeatureList featureList, AdaptedModel adaptedModel,
			int approximationType, double approximationValue, List<IWordsProcessing> wordProcessors,
			IProgressMonitor monitor) {

		// Get SFS results, all located features are 1 confidence
		StrictFeatureSpecificFeatureLocation sfs = new StrictFeatureSpecificFeatureLocation();
		List<LocatedFeature> sfsLocatedBlocks = sfs.locateFeatures(featureList, adaptedModel, monitor);

		List<LocatedFeature> locatedFeatures = new ArrayList<LocatedFeature>();

		// Get all the features of a given block and all its elements
		int i = 1;
		for (Block block : adaptedModel.getOwnedBlocks()) {
			// user cancel
			if (monitor.isCanceled()) {
				return locatedFeatures;
			}

			List<Feature> blockFeatures = LocatedFeaturesUtils.getFeaturesOfBlock(sfsLocatedBlocks, block);
			monitor.subTask("Feature location SFS and LSI. " + blockFeatures.size()
					+ " features competing for Elements inside a Block " + (i - 1) + " : " + i + " /"
					+ adaptedModel.getOwnedBlocks().size());
			List<LocatedFeature> blockLFeatures = LocatedFeaturesUtils.getLocatedFeaturesOfBlock(sfsLocatedBlocks,
					block);
			List<IElement> blockElements = AdaptedModelHelper.getElementsOfBlock(block);

			// Calculate LSI in each block
			ApplyLSI flsi = new ApplyLSI();
			List<LocatedFeature> lfs = flsi.locateFeaturesFromAnotherTechnique(block, blockFeatures, blockElements,
					blockLFeatures, approximationType, approximationValue, wordProcessors);
			if (lfs != null && !lfs.isEmpty()) {
				locatedFeatures.addAll(lfs);
			}
			i++;
		}
		return locatedFeatures;
	}
}