package org.but4reuse.wordclouds.util;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.wordclouds.activator.Activator;
import org.but4reuse.wordclouds.filters.IWordsProcessing;
import org.but4reuse.wordclouds.filters.WordCloudFiltersHelper;
import org.but4reuse.wordclouds.preferences.PreferenceInitializer;
import org.but4reuse.wordclouds.preferences.WordCloudPreferences;
import org.eclipse.core.runtime.IProgressMonitor;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

public class Cloudifier {

	public static Cloud cloudify(List<String> words, List<IWordsProcessing> processors, IProgressMonitor monitor) {
		List<String> tags = processWords(words, processors, monitor);
		Cloud c = new Cloud();
		if (Activator.getDefault() != null) {
			c.setMaxTagsToDisplay(
					Activator.getDefault().getPreferenceStore().getInt(WordCloudPreferences.WORDCLOUD_NB_W));
		} else {
			c.setMaxTagsToDisplay(PreferenceInitializer.DEFAULT_WORDCLOUD_NB_W);
		}
		c.setMaxWeight(50);
		c.setMinWeight(5);
		for (String s : tags) {
			c.addTag(s);
		}
		return c;
	}

	public static Cloud cloudify(List<String> words, IProgressMonitor monitor) {
		List<IWordsProcessing> processors = WordCloudFiltersHelper.getSortedSelectedFilters();
		return cloudify(words, processors, monitor);
	}

	static List<List<String>> cachedProcessedWords = null;

	public static Cloud cloudifyTFIDF(List<List<String>> words, List<IWordsProcessing> processors, int index,
			IProgressMonitor monitor) {
		if (cachedProcessedWords != words) {
			for (int x = 0; x < words.size(); x++) {
				List<String> tags = processWords(words.get(x), processors, monitor);
				words.set(x, tags);
			}
			cachedProcessedWords = words;
		} else {
			words = cachedProcessedWords;
		}

		Cloud c = new Cloud();
		if (Activator.getDefault() != null) {
			c.setMaxTagsToDisplay(
					Activator.getDefault().getPreferenceStore().getInt(WordCloudPreferences.WORDCLOUD_NB_W));
		} else {
			c.setMaxTagsToDisplay(PreferenceInitializer.DEFAULT_WORDCLOUD_NB_W);
		}
		c.setMaxWeight(50);
		c.setMinWeight(5);

		List<String> wordsChecked = new ArrayList<String>();

		double nbBlock = words.size();
		double nbWords = words.get(index).size();

		for (String w : words.get(index)) {
			/*
			 * If we already add this words in the cloud we check the next words.
			 */
			if (TermFrequencyUtils.calculateTermFrequency(w, wordsChecked) != 0) {
				continue;
			}
			/*
			 * Here the score of the word w is calculated Formula TF-IDF (
			 * https://fr.wikipedia.org/wiki/TF-IDF )
			 */

			double nbBlock_isPresent = nbDocContainsWords(words, w);
			double nbTimeW = TermFrequencyUtils.calculateTermFrequency(w, words.get(index));
			double idf = Math.log(nbBlock / nbBlock_isPresent);
			double td = (nbTimeW / nbWords);
			double score = td * idf;

			c.addTag(new Tag(w, score));
			wordsChecked.add(w);
		}
		return c;
	}

	public static Cloud cloudifyTFIDF(List<List<String>> words, int index, IProgressMonitor monitor) {
		List<IWordsProcessing> processors = WordCloudFiltersHelper.getSortedSelectedFilters();
		return cloudifyTFIDF(words, processors, index, monitor);
	}

	public static List<String> processWords(List<String> words, IProgressMonitor monitor) {
		List<IWordsProcessing> processors = WordCloudFiltersHelper.getSortedSelectedFilters();
		return processWords(words, processors, monitor);
	}

	public static List<String> processWords(List<String> words, List<IWordsProcessing> processors,
			IProgressMonitor monitor) {
		for (IWordsProcessing processor : processors) {
			words = processor.processWords(words, monitor);
		}
		return words;
	}

	/**
	 * Count how many lists contain the string word
	 * 
	 * @param list  It will search the word in each sub list of list parameter.
	 * @param words The word that we want to find.
	 * @return How many time we find the tag.
	 */

	private static int nbDocContainsWords(List<List<String>> list, String word) {
		int cpt = 0;
		for (List<String> l : list) {
			for (String w : l) {
				if (w.compareToIgnoreCase(word) == 0) {
					cpt++;
					break;
				}
			}
		}
		return cpt;
	}

}
