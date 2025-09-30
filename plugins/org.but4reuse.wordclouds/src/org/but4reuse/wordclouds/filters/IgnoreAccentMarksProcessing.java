package org.but4reuse.wordclouds.filters;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Put all words without accent marks
 * 
 */
public class IgnoreAccentMarksProcessing implements IWordsProcessing {

	private static String accents = "áàäâãåéèëêíìïîóòöôõúùüûýÿÁÀÄÂÃÅÉÈËÊÍÌÏÎÓÒÖÔÕÚÙÜÛÇÑÝ";
	private static String replacements = "aaaaaaeeeeiiiiooooouuuuyyAAAAAAEEEEIIIIOOOOOUUUUCNY";

	
	@Override
	public List<String> processWords(List<String> words, IProgressMonitor monitor) {
		List<String> returnWords = new ArrayList<String>();
		for (String word : words) {
			StringBuilder sb = new StringBuilder(word.length());
			for (char c : word.toCharArray()) {
				int index = accents.indexOf(c);
				if (index >= 0) {
					sb.append(replacements.charAt(index));
				} else {
					sb.append(c);
				}
			}
			returnWords.add(sb.toString());
		}
		return returnWords;
	}

}
