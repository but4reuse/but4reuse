package org.but4reuse.wordclouds.filters;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

/**
 * Remove numbers
 * 
 * @author jabier.martinez
 * 
 */
public class NumbersFilter implements IWordsProcessing {

	@Override
	public List<String> processWords(List<String> words) {
		List<String> returnWords = new ArrayList<String>();
		for (String word : words) {
			if(!isNumeric(word)){
				returnWords.add(word);
			}
		}
		return returnWords;
	}

	public static boolean isNumeric(String str) {
	  NumberFormat formatter = NumberFormat.getInstance();
	  ParsePosition pos = new ParsePosition(0);
	  formatter.parse(str, pos);
	  return str.length() == pos.getIndex();
	}

}
