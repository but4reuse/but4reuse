package org.but4reuse.wordclouds.filters;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Interface for processing sets of words
 * 
 * @author jabier.martinez
 * 
 */
public interface IWordsProcessing {

	public List<String> processWords(List<String> words, IProgressMonitor monitor);

}
