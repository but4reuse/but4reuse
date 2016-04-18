package org.but4reuse.wordclouds.filters;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.utils.strings.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Split all the words using camel case
 * 
 * @author jabier.martinez
 * 
 */
public class CamelCaseSplitter implements IWordsProcessing {

	@Override
	public List<String> processWords(List<String> words, IProgressMonitor monitor) {
		List<String> returnWords = new ArrayList<String>();
		for (String word : words) {
			List<String> splitted = StringUtils.splitCamelCase(word);
			for (String splittedWord : splitted) {
				returnWords.add(splittedWord);
			}
		}
		return returnWords;
	}

}
