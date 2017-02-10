package org.but4reuse.wordclouds.filters;

import java.util.List;

import org.but4reuse.utils.nlp.similarity.Synonyms;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Replace words which are synonyms
 * 
 * @author jabier.martinez
 * 
 */
public class SynonymsFilter implements IWordsProcessing {

	@Override
	public List<String> processWords(List<String> words, IProgressMonitor monitor) {
		for (int i = 0; i < words.size(); i++) {
			for (int j = 0; j < words.size(); j++) {
				if (i != j) {
					String word1 = words.get(i);
					String word2 = words.get(j);
					if (!word1.equals(word2)) {
						boolean synonym = Synonyms.isSynonym(word1, word2);
						if (synonym) {
							// replace with the smallest
							if (word1.compareTo(word2) > 0) {
								words.set(i, words.get(j));
							} else {
								words.set(j, words.get(i));
							}
						}
					}
				}
			}
		}
		return words;
	}

}
