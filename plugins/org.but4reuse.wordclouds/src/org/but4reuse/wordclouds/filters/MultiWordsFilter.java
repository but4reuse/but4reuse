package org.but4reuse.wordclouds.filters;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.wordclouds.util.WordCloudUtil;
import org.eclipse.core.runtime.IProgressMonitor;

// TODO support multi words of more than 2 words?
/**
 * Append multi words to consider them as a unique word
 * 
 * @author jabier.martinez
 * 
 */
public class MultiWordsFilter implements IWordsProcessing {

	@Override
	public List<String> processWords(List<String> words, IProgressMonitor monitor) {
		return processWords(words, getMultiWords());
	}

	public List<String> getMultiWords() {
		return WordCloudUtil.getUserDefinedMultiWords();
	}

	public List<String> processWords(List<String> words, List<String> multiWords) {
		List<Integer> indexesToRemove = new ArrayList<Integer>();
		List<String> multiWordsToAdd = new ArrayList<String>();

		for (String multiWord : multiWords) {
			String[] multi = multiWord.split(" ");
			for (int i = 0; i < words.size() - 1; i++) {
				String current = words.get(i);
				if (current.equals(multi[0])) {
					String next = words.get(i + 1);
					if (next.equals(multi[1])) {
						while (i >= multiWordsToAdd.size()) {
							multiWordsToAdd.add(null);
						}
						multiWordsToAdd.add(i, multiWord);
						indexesToRemove.add(i);
						indexesToRemove.add(i + 1);
					}
				}
			}
		}

		List<String> returnWords = new ArrayList<String>();
		for (int i = 0; i < words.size(); i++) {
			if (!indexesToRemove.contains(i)) {
				returnWords.add(words.get(i));
			} else {
				if (i < multiWordsToAdd.size() && multiWordsToAdd.get(i) != null) {
					returnWords.add(multiWordsToAdd.get(i));
				}
			}
		}
		return returnWords;
	}
}
