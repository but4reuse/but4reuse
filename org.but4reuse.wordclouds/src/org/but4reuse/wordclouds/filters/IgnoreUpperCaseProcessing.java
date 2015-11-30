package org.but4reuse.wordclouds.filters;

import java.util.ArrayList;
import java.util.List;

/**
 * Put all words in lower case
 * 
 * @author jabier.martinez
 * 
 */
public class IgnoreUpperCaseProcessing implements IWordsProcessing {

	@Override
	public List<String> processWords(List<String> words) {
		List<String> returnWords = new ArrayList<String>();
		for (String word : words) {
			returnWords.add(word.toLowerCase());
		}
		return returnWords;
	}

}
