package org.but4reuse.wordclouds.filters;

import java.util.List;

/**
 * Interfact for processing sets of words
 * 
 * @author jabier.martinez
 * 
 */
public interface IWordsProcessing {

	public List<String> processWords(List<String> words);

}
