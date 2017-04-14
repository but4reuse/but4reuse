package org.but4reuse.wordclouds.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.but4reuse.utils.nlp.stemming.Stemmer;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Replace words that share the same root with the most common using this root
 * 
 * @author jabier.martinez
 */
public class StemmingFilter implements IWordsProcessing {

	@Override
	public List<String> processWords(List<String> words, IProgressMonitor monitor) {

		Map<String, List<String>> stemMap = new LinkedHashMap<String, List<String>>();
		for (String word : words) {
			String stem = Stemmer.getRoot(word);
			List<String> list = stemMap.get(stem);
			if (list == null) {
				list = new ArrayList<String>();
			}
			list.add(word);
			stemMap.put(stem, list);
		}

		List<String> returnWords = new ArrayList<String>();
		for (Entry<String, List<String>> entry : stemMap.entrySet()) {
			List<String> list = entry.getValue();
			// if (list.size() > 1) {
			// System.out.println(list);
			// }
			String common = mostCommon(list);
			// add as many words as that were before
			for (int i = 0; i < list.size(); i++) {
				returnWords.add(common);
			}
		}
		return returnWords;
	}

	public static <T> T mostCommon(List<T> list) {
		Map<T, Integer> map = new HashMap<T, Integer>();
		for (T t : list) {
			Integer val = map.get(t);
			map.put(t, val == null ? 1 : val + 1);
		}
		Entry<T, Integer> max = null;
		for (Entry<T, Integer> e : map.entrySet()) {
			if (max == null || e.getValue() > max.getValue())
				max = e;
		}
		return max.getKey();
	}

}
