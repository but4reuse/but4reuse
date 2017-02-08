package org.but4reuse.utils.nlp.similarity;

import edu.cmu.lti.ws4j.WS4J;

/**
 * Check if two words are synonym
 * 
 * @author jabier.martinez
 * 
 */
public class WS4JSynonym {
	public static double getSimilaritySynonym(String word1, String word2) {
		double[][] d = WS4J.getSynonymyMatrix(new String[] { word1 }, new String[] { word2 });
		return d[0][0];
	}
}
