package org.but4reuse.wordclouds.filters;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.wordclouds.util.WordCloudUtil;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Replace words which are synonyms
 * 
 * @author jabier.martinez
 * 
 */
public class UserSynonymsFilter implements IWordsProcessing {

	@Override
	public List<String> processWords(List<String> words, IProgressMonitor monitor) {
		List<String> synonymsWords = WordCloudUtil.getUserDefinedSynonyms();
		return processWords(words, synonymsWords);
	}

	public List<String> processWords(List<String> words, List<String> synonyms) {
		List<String> one = new ArrayList<String>();
		List<String> two = new ArrayList<String>();
		for (String w : synonyms) {
			String[] syn = w.split(" ");
			if (syn.length == 2) {
				one.add(syn[0]);
				two.add(syn[1]);
			}
		}
		for (int i = 0; i < words.size(); i++) {
			for (int j = 0; j < words.size(); j++) {
				if (i != j) {
					String word1 = words.get(i);
					String word2 = words.get(j);
					int x = one.indexOf(word1);
					if (x != -1 && two.get(x).equals(word2)) {
						words.set(j, word1);
					} else {
						x = two.indexOf(word1);
						if (x != -1 && one.get(x).equals(word2)) {
							words.set(i, word2);
						}
					}
				}
			}
		}
		return words;
	}

}
