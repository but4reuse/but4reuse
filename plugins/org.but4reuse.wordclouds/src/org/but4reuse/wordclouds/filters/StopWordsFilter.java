package org.but4reuse.wordclouds.filters;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.wordclouds.util.WordCloudUtil;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Remove stop words
 * 
 * @author jabier.martinez
 * 
 */
public class StopWordsFilter implements IWordsProcessing {
	
	List<String> stopWords;
	
	public StopWordsFilter() {
		stopWords = WordCloudUtil.getUserDefinedStopWords();
	}
	
	public StopWordsFilter(List<String> stopWords) {
		this.stopWords = stopWords;
	}

	@Override
	public List<String> processWords(List<String> words, IProgressMonitor monitor) {
		return removeStopWords(words, stopWords);
	}

	public List<String> removeStopWords(List<String> words, List<String> stopWords) {
		List<String> returnWords = new ArrayList<String>();
		for (String word : words) {
			if (!stopWords.contains(word)) {
				returnWords.add(word);
			}
		}
		return returnWords;
	}

}
