package org.but4reuse.feature.location.impl;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.feature.location.LocatedFeature;
import org.but4reuse.feature.location.lsi.activator.Activator;
import org.but4reuse.feature.location.lsi.location.preferences.LSIPreferencePage;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.strings.StringUtils;
import org.but4reuse.wordclouds.util.Cloudifier;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import lsi4j.LSI4J;

/**
 * Feature Location LSI
 * 
 * @author jabier.martinez
 * @author arthur joanny
 */
public class FeatureLocationLSI implements IFeatureLocation {

	@Override
	public List<LocatedFeature> locateFeatures(FeatureList featureList, AdaptedModel adaptedModel,
			IProgressMonitor monitor) {

		List<List<String>> documents = new ArrayList<List<String>>();
		List<Block> documentBlocks = new ArrayList<Block>();

		// Each block is a document
		// We gather all words for each block
		for (Block b : adaptedModel.getOwnedBlocks()) {

			ArrayList<String> currentListOfWords = new ArrayList<String>();

			for (IElement e : AdaptedModelHelper.getElementsOfBlock(b)) {
				List<String> words = ((AbstractElement) e).getWords();
				currentListOfWords.addAll(words);
			}

			// Only add them to documents if they have at least a word
			if (currentListOfWords.size() > 0) {
				documentBlocks.add(b);
				documents.add(currentListOfWords);
			}
		}

		// if the list is empty it means we found 0 zero block for the current feature
		// so it's not necessary to continue with the feature
		if (documents.size() == 0) {
			return new ArrayList<LocatedFeature>();
		}

		// Preferences
		boolean fixed = Activator.getDefault().getPreferenceStore().getBoolean(LSIPreferencePage.FIXED);
		double approximationValue = Activator.getDefault().getPreferenceStore().getDouble(LSIPreferencePage.DIM);
		int approximationType;
		if (fixed) {
			approximationType = LSI4J.APPROXIMATION_K_VALUE;
		} else {
			approximationType = LSI4J.APPROXIMATION_PERCENTAGE;
		}

		// We create LSI
		LSI4J lsi4j = new LSI4J(documents, approximationType, approximationValue);

		// We apply the features as query
		List<LocatedFeature> locatedFeatures = new ArrayList<LocatedFeature>();
		for (Feature f : featureList.getOwnedFeatures()) {
			List<String> featureWords = getFeatureWords(f);
			double[] results = lsi4j.applyLSI(featureWords);
			if (results != null) {
				for (int i = 0; i < documentBlocks.size(); i++) {
					if (results[i] > 0) {
						locatedFeatures.add(new LocatedFeature(f, documentBlocks.get(i), results[i]));
					}
				}
			}
		}
		return locatedFeatures;
	}

	/**
	 * It will give the words from the feature
	 * 
	 * @param f
	 *            The feature
	 * @return processed list of words
	 */
	static public List<String> getFeatureWords(Feature feature) {
		// Get each word of the name of the feature
		List<String> featureTerms = StringUtils.tokenizeString(feature.getName());
		// Get each word in the description of the feature
		featureTerms.addAll(StringUtils.tokenizeString(feature.getDescription()));
		List<String> processedWords = Cloudifier.processWords(featureTerms, new NullProgressMonitor());
		return processedWords;
	}

}
