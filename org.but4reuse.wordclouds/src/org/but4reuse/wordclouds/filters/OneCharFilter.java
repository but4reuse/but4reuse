package org.but4reuse.wordclouds.filters;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * One char at least
 * 
 * @author jabier.martinez
 * 
 */
public class OneCharFilter implements IWordsProcessing {

	@Override
	public List<String> processWords(List<String> words, IProgressMonitor monitor) {
		List<String> returnWords = new ArrayList<String>();
		for (String word : words) {
			if (word.length() > 1) {
				returnWords.add(word);
			}
		}
		return returnWords;
	}

}
