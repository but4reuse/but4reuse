package org.but4reuse.wordclouds.util;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.featurelist.Feature;
import org.but4reuse.utils.nlp.UselessWordsRemover;
import org.but4reuse.utils.strings.StringUtils;

public class FeatureWordCloudUtil {

	/**
	 * get the words of a feature
	 * 
	 * @param feature
	 * @return non null list
	 */
	public static List<String> getFeatureWords(Feature f, List<String> stopWords) {
		List<String> words = new ArrayList<String>();
		// name
		if (f.getName() != null) {
			for (String s : StringUtils.tokenizeAndCamelCase(f.getName())) {
				if (!stopWords.contains(s)) {
					words.add(s);
				}
			}
		}
		// description
		if (f.getDescription() != null) {
			for (String s : StringUtils.tokenizeAndCamelCase(f.getDescription())) {
				if (!stopWords.contains(s)) {
					words.add(s);
				}
			}
		}
		UselessWordsRemover.removeUselessWords(words);
		return words;
	}

}
