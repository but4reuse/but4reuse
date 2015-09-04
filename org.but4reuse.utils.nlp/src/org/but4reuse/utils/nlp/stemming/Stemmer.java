package org.but4reuse.utils.nlp.stemming;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

/**
 * Calculates the root of a word
 * 
 * @author jabier.martinez
 * 
 */
public class Stemmer {

	/**
	 * Get the root of a word using the snowball library
	 * 
	 * @param word
	 * @return the root or the same word
	 */
	public static String getRoot(String word) {
		SnowballStemmer stemmer = new englishStemmer();
		stemmer.setCurrent(word);
		stemmer.stem();
		String root = stemmer.getCurrent();
		return root;
	}

}
