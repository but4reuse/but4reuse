package org.but4reuse.wordclouds.feature.location;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.wordclouds.util.WordCloudUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Cloud.Case;

public class FeatureLocationWordCloud implements IFeatureLocation {

	public static double rateMin = 0.65;

	@Override
	public void locateFeatures(FeatureList featureList, AdaptedModel adaptedModel, IProgressMonitor monitor) {

		/*
		 * Word cloud creation for each block
		 */
		ArrayList<Cloud> list = new ArrayList<Cloud>();
		for (Block b : adaptedModel.getOwnedBlocks()) {
			Cloud cloud = new Cloud(Case.CAPITALIZATION);
			cloud.setMaxWeight(50);
			cloud.setMinWeight(5);
			cloud.setMaxTagsToDisplay(50);
			for (BlockElement e : b.getOwnedBlockElements()) {
				for (ElementWrapper wr : e.getElementWrappers()) {

					AbstractElement element = (AbstractElement) (wr.getElement());
					for (String s : element.getWords())
						cloud.addTag(s.trim());
				}
			}
			list.add(cloud);
		}

		ArrayList<Cloud> clouds = new ArrayList<Cloud>();

		// Word Cloud creation for each feature
		for (Feature f : featureList.getOwnedFeatures()) {
			Cloud cloud_f = new Cloud(Case.CAPITALIZATION);
			cloud_f.setMaxWeight(50);
			cloud_f.setMinWeight(5);
			cloud_f.setMaxTagsToDisplay(50);

			/*
			 * Here we split the feature name
			 */
			if (f.getName() != null) {
				StringTokenizer tk = new StringTokenizer(f.getName(), " :!?*+²&~\"#'{}()[]-|`_\\^°,.;/§");

				while (tk.hasMoreTokens()) {
					for (String w : tk.nextToken().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
						cloud_f.addTag(w.trim());
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
						cloud_f.addTag(w.trim());
					}
				}
			}
			clouds.add(cloud_f);
		}

		// Word cloud IDF calculation for feature
		ArrayList<Cloud> clouds_IDF = new ArrayList<Cloud>();
		for (Cloud c : clouds) {
			clouds_IDF.add(WordCloudUtil.getCloudIDF(clouds, c));
		}

		int i = 0;
		for (Feature f : featureList.getOwnedFeatures()) {
			for (Cloud c : list) {

				Block b = adaptedModel.getOwnedBlocks().get(list.indexOf(c));

				/*
				 * // Check if block and feature are in the same artefact if
				 * (!checkArtefact(b, f, adaptedModel)) continue;
				 */
				double d = WordCloudUtil.cmpClouds(clouds_IDF.get(i), c);
				if (d >= rateMin) {
					b.getCorrespondingFeatures().add(f);
				}
			}
			i++;
		}

	}

	/**
	 * 
	 * @param b
	 *            The bloc tested
	 * @param fThe
	 *            feature tested
	 * @param adaptedModel
	 *            The adapted model
	 * @return True if the block and the feature are in the same artefact.
	 */

	private static boolean checkArtefact(Block b, Feature f, AdaptedModel adaptedModel) {
		for (Artefact art : f.getImplementedInArtefacts()) {
			AdaptedArtefact adaptedArt = AdaptedModelHelper.getAdaptedArtefact(adaptedModel, art);
			if (AdaptedModelHelper.getBlocksOfAdaptedArtefact(adaptedArt).contains(b))
				return true;

		}
		return false;
	}

}
