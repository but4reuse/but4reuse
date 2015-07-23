package org.but4reuse.wordclouds.visualisation;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.but4reuse.visualisation.IVisualisation;
import org.but4reuse.wordclouds.util.WordCloudUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;
import org.mcavallo.opencloud.Cloud.Case;

public class WordCloudVisualisation implements IVisualisation {

	/**
	 * A list which contains a word cloud for each identified features.
	 */
	private static ArrayList<Cloud> clouds;
	private static ArrayList<Cloud> clouds_idf;

	public WordCloudVisualisation() {
	}

	/**
	 * This method returns the word cloud list.
	 * 
	 * @return the value of the attribute clouds.
	 */
	public static ArrayList<Cloud> getClouds() {
		return clouds;
	}

	public static ArrayList<Cloud> getCloudsIDF() {
		return clouds_idf;
	}

	@Override
	public void prepare(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor) {

		clouds = new ArrayList<Cloud>();
		clouds_idf = new ArrayList<Cloud>();
		ArrayList<ArrayList<String>> listWords = new ArrayList<ArrayList<String>>();

		for (Block b : adaptedModel.getOwnedBlocks()) {

			Cloud cloud = null;
			cloud = new Cloud(Case.CAPITALIZATION);
			cloud.setMaxWeight(50);
			cloud.setMinWeight(5);
			cloud.setMaxTagsToDisplay(50);
			ArrayList<String> list = new ArrayList<String>();
			/*
			 * For each block we get all elements owned We use the method
			 * getWords for having several strings which could be used as block
			 * name.
			 */
			for (IElement e : (AdaptedModelHelper.getElementsOfBlock(b))) {
				List<String> words = ((AbstractElement) e).getWords();
				addWords(cloud, words);
				for (String s : words)
					if (s.compareTo("") != 0)
						list.add(s.trim());
			}

			clouds.add(cloud);
			listWords.add(list);
		}

		for (int i = 0; i < listWords.size(); i++) {
			clouds_idf.add(WordCloudUtil.createWordCloudIDF(listWords, i));
		}

	}

	@Override
	public void show() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				WorkbenchUtils.forceShowView("org.but4reuse.wordclouds.view");
				WordCloudVis.update(0, false);
			}
		});

	}

	/**
	 * Add strings in a cloud
	 * 
	 * @param cloud
	 *            This is the cloud where strings will be added.
	 * @param words
	 *            The list which contains all strings to add in the cloud.
	 */
	private static void addWords(Cloud cloud, List<String> words) {
		for (String word : words) {
			String s = word.trim();
			cloud.addTag(new Tag(s));
		}
	}

}
