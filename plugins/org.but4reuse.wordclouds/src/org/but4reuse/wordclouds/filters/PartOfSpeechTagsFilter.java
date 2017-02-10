package org.but4reuse.wordclouds.filters;

import java.util.List;

import org.but4reuse.utils.nlp.POSUtils;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Part of speech tags filter
 * 
 * @author jabier.martinez
 * 
 */
public class PartOfSpeechTagsFilter implements IWordsProcessing {

	@Override
	public List<String> processWords(List<String> words, IProgressMonitor monitor) {
		POSUtils.removeUnnecessaryWords(words);
		return words;
	}

}
