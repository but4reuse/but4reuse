package org.but4reuse.wordclouds.filters;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Put all words in lower case
 * 
 * @author jabier.martinez
 * 
 */
public class IgnoreUpperCaseProcessing implements IWordsProcessing {

	@Override
	public List<String> processWords(List<String> words, IProgressMonitor monitor) {
		List<String> returnWords = new ArrayList<String>();
		for (String word : words) {
			returnWords.add(word.toLowerCase());
		}
		return returnWords;
	}

}
