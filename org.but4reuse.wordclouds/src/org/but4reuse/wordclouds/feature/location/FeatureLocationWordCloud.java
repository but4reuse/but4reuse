package org.but4reuse.wordclouds.feature.location;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.nlp.UselessWordsRemover;
import org.but4reuse.wordclouds.activator.Activator;
import org.but4reuse.wordclouds.util.WordCloudUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.mcavallo.opencloud.Cloud;

public class FeatureLocationWordCloud implements IFeatureLocation {

	public static double rateMin;

	@Override
	public void locateFeatures(FeatureList featureList, AdaptedModel adaptedModel, IProgressMonitor monitor) {

		/*
		 * We gather all words for each blocks
		 */
		rateMin = Activator.getDefault().getPreferenceStore().getDouble(FeaturesLocationPreferences.HIGHT_SIM);
		ArrayList<ArrayList<String>> listBlocksWords;
		System.out.println("Rate : "+rateMin);
		listBlocksWords = new ArrayList<ArrayList<String>>();

		for (Block b : adaptedModel.getOwnedBlocks()) {
			ArrayList<String> l = new ArrayList<String>();

			for (BlockElement e : b.getOwnedBlockElements()) {
				for (ElementWrapper wr : e.getElementWrappers()) {

					AbstractElement element = (AbstractElement) (wr.getElement());
					for (String s : element.getWords())
						l.add(s.trim());

				}
			}

			UselessWordsRemover.removeUselessWords(l);
			listBlocksWords.add(l);
		}

		/*
		 * We gather all words for each features
		 */
		ArrayList<ArrayList<String>> listFeaturesWords;
		listFeaturesWords = new ArrayList<ArrayList<String>>();

		for (Feature f : featureList.getOwnedFeatures()) {

			ArrayList<String> l = new ArrayList<String>();
			/*
			 * Here we split the feature name
			 */
			if (f.getName() != null) {
				StringTokenizer tk = new StringTokenizer(f.getName(), " :!?*+²&~\"#'{}()[]-|`_\\^°,.;/§");

				while (tk.hasMoreTokens()) {
					for (String w : tk.nextToken().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
						l.add(w.trim());
					}
				}
			}
			/*
			 * Here we split the feature description
			 */
			if (f.getDescription() != null) {
				StringTokenizer tk = new StringTokenizer(f.getDescription(), " :!?*+²&~\"#'{}()[]-|`_\\^°,.;/§");
				while (tk.hasMoreTokens()) {
					for (String w : tk.nextToken().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
						l.add(w.trim());
					}
				}
			}
			UselessWordsRemover.removeUselessWords(l);
			listFeaturesWords.add(l);
		}

		// Word cloud IDF calculation for feature

		ArrayList<Cloud> clouds_IDF_Features = new ArrayList<Cloud>();
		for (int i = 0; i < listFeaturesWords.size(); i++) {
			clouds_IDF_Features.add(WordCloudUtil.createWordCloudIDF(listFeaturesWords, i));
		}

		// Word cloud IDF calculation for blocks
		ArrayList<Cloud> clouds_IDF_Blocks = new ArrayList<Cloud>();
		for (int i = 0; i < listBlocksWords.size(); i++) {
			clouds_IDF_Blocks.add(WordCloudUtil.createWordCloudIDF(listBlocksWords, i));
		}

		for (int i = 0; i < featureList.getOwnedFeatures().size(); i++) {
			for (Cloud c : clouds_IDF_Blocks) {

				Block b = adaptedModel.getOwnedBlocks().get(clouds_IDF_Blocks.indexOf(c));

				Feature f = featureList.getOwnedFeatures().get(i);
				double d = WordCloudUtil.cmpClouds(clouds_IDF_Features.get(i), c);
				if (d >= rateMin) {
					b.getCorrespondingFeatures().add(f);
				}
			}

		}

	}

}
