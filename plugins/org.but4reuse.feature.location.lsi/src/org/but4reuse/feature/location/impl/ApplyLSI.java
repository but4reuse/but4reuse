package org.but4reuse.feature.location.impl;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.feature.location.LocatedFeature;
import org.but4reuse.feature.location.lsi.activator.Activator;
import org.but4reuse.feature.location.lsi.location.preferences.LSIPreferencePage;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.wordclouds.util.Cloudifier;
import org.eclipse.core.runtime.NullProgressMonitor;

import lsi4j.LSI4J;

public class ApplyLSI {

	/**
	 * This class is to chain LSI with the results of a previous FL technique (e.g.,
	 * SFS) Some e.g.: - Classic
	 * http://www1.se.cuhk.edu.hk/~seem5680/lecture/LSI-Eg.pdf - Standford
	 * https://nlp.stanford.edu/IR-book/html/htmledition/latent-semantic-indexing-1.html
	 * Based on the featureList and the adapted Model
	 * 
	 * @author Nicolas Ordonez Chala
	 */
	public List<LocatedFeature> locateFeaturesFromAnotherTechnique(Block block, List<Feature> blockFeatures,
			List<IElement> blockElements, List<LocatedFeature> previousResultsForThisBlock) {

		List<LocatedFeature> locatedFeatures = new ArrayList<LocatedFeature>();

		try {
			if (blockFeatures.isEmpty()) {
				return null;
			} else if (blockFeatures.size() == 1) {
				locatedFeatures.add(new LocatedFeature(blockFeatures.get(0), block, 1));
				return locatedFeatures;
			} else {
				// get lsi user parameters
				boolean fixed = Activator.getDefault().getPreferenceStore().getBoolean(LSIPreferencePage.FIXED);
				double approximationValue = Activator.getDefault().getPreferenceStore()
						.getDouble(LSIPreferencePage.DIM);
				int approximationType;
				if (fixed) {
					approximationType = LSI4J.APPROXIMATION_K_VALUE;
				} else {
					approximationType = LSI4J.APPROXIMATION_PERCENTAGE;
				}
				// build documents
				List<List<String>> documents = buildDocuments(blockFeatures);
				LSI4J lsiTechnique = new LSI4J(documents, approximationType, approximationValue);
				if (documents != null) {
					for (IElement blockElement : blockElements) {
						boolean added = false;
						// builder query from each blockElement
						List<String> query = buildQuery(blockElement);
						// do nothing if no query is created (empty words)
						if (query != null && !query.isEmpty()) {
							// Get similarity from LSI Technique
							double vecSimilarity[] = lsiTechnique.applyLSI(query);

							// Get the position with the highest value
							int higherSimilarityIndex = getHigherSimilarityIndex(vecSimilarity);

							// if there exist one highest value it means that it found a feature
							if (vecSimilarity[higherSimilarityIndex] != -1) {
								double confidence = (vecSimilarity[higherSimilarityIndex] + 1) / 2;
								if (confidence > 0) {
									locatedFeatures.add(new LocatedFeature(blockFeatures.get(higherSimilarityIndex),
											blockElement, confidence));
									added = true;
								}
							}
						}
						// It was not assigned to any feature. Add it to all features.
						if (!added) {
							for (Feature f : blockFeatures) {
								// use confidence of the previous technique
								// we use the confidence of the first one
								double confidence = 1;
								if(!previousResultsForThisBlock.isEmpty()) {
									confidence = previousResultsForThisBlock.get(0).getConfidence();
								}
								locatedFeatures.add(new LocatedFeature(f, blockElement, confidence));
							}
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return locatedFeatures;
	}

	/**
	 * Build the documents matrix based on the terms and the feature blocks
	 * 
	 * @param featureBlocks
	 * @return documents
	 */
	static private List<List<String>> buildDocuments(List<Feature> featureBlocks) {
		List<List<String>> documents = new ArrayList<List<String>>();
		// For each feature inside the block
		for (Feature feature : featureBlocks) {
			List<String> processedWords = FeatureLocationLSI.getFeatureWords(feature);
			if (processedWords != null && processedWords.size() > 0) {
				documents.add(processedWords);
			}
		}
		return documents;
	}

	/**
	 * Build the query array based on the terms and the elements blocks
	 * 
	 * @param elementsBlocks
	 * @return query
	 */
	static private List<String> buildQuery(IElement element) {
		List<String> query = new ArrayList<String>();
		// Get the words of the element inside the block
		List<String> elementWords = ((AbstractElement) element).getWords();
		query = Cloudifier.processWords(elementWords, new NullProgressMonitor());
		return query;
	}

	/**
	 * Get the position of the array with the higher value
	 * 
	 * @param similarities
	 * @return
	 */
	static private int getHigherSimilarityIndex(double[] similarities) {
		int maxValueIndex = -1;
		double maxValue = 0;
		if (similarities == null || similarities.length < 1) {
			return maxValueIndex;
		}

		for (int i = 0; i < similarities.length; i++) {
			double actualValue = similarities[i];

			if (maxValueIndex == -1) {
				maxValueIndex = i;
				maxValue = actualValue;
			}

			else if (actualValue > maxValue) {
				maxValueIndex = i;
				maxValue = similarities[i];
			}
		}
		return maxValueIndex;
	}
}
