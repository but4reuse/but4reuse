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
import org.but4reuse.wordclouds.activator.Activator;
import org.but4reuse.wordclouds.preferences.WordCloudPreferences;
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
	private static List<Cloud> clouds;
	private static List<Cloud> clouds_idf;

	public WordCloudVisualisation() {
	}

	/**
	 * This method returns the word cloud list.
	 * 
	 * @return the value of the attribute clouds.
	 */
	public static List<Cloud> getClouds() {
		return clouds;
	}

	public static List<Cloud> getCloudsIDF() {
		return clouds_idf;
	}

	@Override
	public void prepare(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor) {

		List<String> stopWords = WordCloudUtil.getUserDefinedStopWords();
		
		clouds = new ArrayList<Cloud>();
		clouds_idf = new ArrayList<Cloud>();
		List<List<String>> listWords = new ArrayList<List<String>>();

		for (Block b : adaptedModel.getOwnedBlocks()) {

			Cloud cloud = null;
			cloud = new Cloud(Case.CAPITALIZATION);
			cloud.setMaxWeight(50);
			cloud.setMinWeight(5);
			cloud.setMaxTagsToDisplay(Activator.getDefault().getPreferenceStore()
					.getInt(WordCloudPreferences.WORDCLOUD_NB_W));
			List<String> list = new ArrayList<String>();
			/*
			 * For each block we get all elements owned We use the method
			 * getWords for having several strings which could be used as block
			 * name.
			 */
			for (IElement e : (AdaptedModelHelper.getElementsOfBlock(b))) {
				List<String> words = ((AbstractElement) e).getWords();
				
				// remove stop words
				words.removeAll(stopWords);
				
				addWords(cloud, words);
				for (String s : words) {
					if (s.compareTo("") != 0) {
						list.add(s.trim());
					}
				}
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
