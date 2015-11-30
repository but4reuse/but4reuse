package org.but4reuse.wordclouds.filters;

import java.util.List;

import org.but4reuse.utils.nlp.UselessWordsRemover;

/**
 * Part of speech tags filter
 * 
 * @author jabier.martinez
 * 
 */
public class PartOfSpeechTagsFilter implements IWordsProcessing {

	@Override
	public List<String> processWords(List<String> words) {
		UselessWordsRemover.removeUselessWords(words);
		return words;
	}

}
