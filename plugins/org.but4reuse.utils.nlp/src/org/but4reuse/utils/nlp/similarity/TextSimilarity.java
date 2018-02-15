package org.but4reuse.utils.nlp.similarity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.but4reuse.utils.nlp.POSUtils;
import org.but4reuse.utils.nlp.WordNetUtils;
import org.but4reuse.utils.strings.StringUtils;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

/**
 * Text similarity
 * 
 * @author jabier.martinez
 */
public class TextSimilarity {

	// We use a cache of wup because getRoots is a time expensive method
	private static WuAndPalmer wup = null;

	public static float getSimilarityWUP(String source, String target) {
		if (wup == null) {
			IDictionary dict = WordNetUtils.getDictionary();
			ArrayList<ISynsetID> roots = getRoots(dict);
			wup = new WuAndPalmer(dict, roots);
		}

		List<String> sourceSentence = StringUtils.tokenizeString(source);
		List<String> targetSentence = StringUtils.tokenizeString(target);
		double[][] simMatrix = new double[sourceSentence.size()][targetSentence.size()];

		String[] sourceTags = POSUtils.getTags(sourceSentence);
		String[] targetTags = POSUtils.getTags(targetSentence);

		for (int r = 0; r < sourceSentence.size(); r++) {
			for (int c = 0; c < targetSentence.size(); c++) {
				// nouns and verbs are the accepted pairs
				if (sourceTags[r].startsWith("NN") && targetTags[c].startsWith("NN")) {
					double max = wup.max(sourceSentence.get(r), targetSentence.get(c), "n");
					simMatrix[r][c] = max;
				} else if (sourceTags[r].startsWith("VB") && targetTags[c].startsWith("VB")) {
					double max = wup.max(sourceSentence.get(r), targetSentence.get(c), "n");
					simMatrix[r][c] = max;
				}
			}
		}
		return flattenMatrix(simMatrix);
	}

	/**
	 * Credits: jnoppen
	 */
	public static float flattenMatrix(double[][] simMatrix) {
		List<Double> bestMatches = new ArrayList<Double>();
		for (int i = 0; i < simMatrix.length; i++) {
			double[] currentWordResult = simMatrix[i];
			double bestMatch = -1;
			for (int j = 0; j < currentWordResult.length; j++) {
				double currentMatch = currentWordResult[j];
				if (currentMatch > bestMatch) {
					bestMatch = currentMatch;
				}
			}
			bestMatches.add(new Double(bestMatch));
		}
		return flattenMatchResult(bestMatches);
	}

	public static float flattenMatchResult(List<Double> bestMatches) {
		int numberMatched = 0;
		float accum = 0;

		float thresholdNumberMatched = 0;
		float thresholdAccum = 0;
		float threshold = (float) 0.25;

		float highestMatch = 0;
		float lowestMatch = 1;

		Iterator<Double> MatchIt = bestMatches.iterator();

		while (MatchIt.hasNext()) {
			float currentMatch = MatchIt.next().floatValue();

			if (currentMatch > 0) {
				accum += currentMatch;
				numberMatched++;
				if (currentMatch >= threshold) {
					thresholdAccum += currentMatch;
					thresholdNumberMatched++;
				}

				if (currentMatch < lowestMatch) {
					lowestMatch = currentMatch;
				}

				if (currentMatch > highestMatch) {
					highestMatch = currentMatch;
				}
			}
		}

		float fractionMatched = (float) numberMatched / (float) bestMatches.size();

		float matchAverage = accum / (float) numberMatched;
		float thresHoldMatchAverage = thresholdAccum / (float) thresholdNumberMatched;

		float matchAmplitude = highestMatch - lowestMatch;

		if (fractionMatched > 0.25) { // if at least 25% of the source sentence
										// was matched
			if (matchAmplitude < 0.75) { // if there is not too much difference
											// in similarity matching
				return matchAverage;
			} else { // if there is, only look at the ones that made the
						// threshold
				return thresHoldMatchAverage;
			}
		} else {
			return 0;
		}
	}

	public static ArrayList<ISynsetID> getRoots(IDictionary dict) {
		ArrayList<ISynsetID> roots = new ArrayList<ISynsetID>();
		ISynset synset = null;
		Iterator<ISynset> iterator = null;
		List<ISynsetID> hypernyms = null;
		List<ISynsetID> hypernym_instances = null;
		iterator = dict.getSynsetIterator(POS.NOUN);
		while (iterator.hasNext()) {
			synset = iterator.next();
			hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
			hypernym_instances = synset.getRelatedSynsets(Pointer.HYPERNYM_INSTANCE);
			if (hypernyms.isEmpty() && hypernym_instances.isEmpty()) {
				roots.add(synset.getID());
			}
		}
		iterator = dict.getSynsetIterator(POS.VERB);
		while (iterator.hasNext()) {
			synset = iterator.next();
			hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
			hypernym_instances = synset.getRelatedSynsets(Pointer.HYPERNYM_INSTANCE);
			if (hypernyms.isEmpty() && hypernym_instances.isEmpty()) {
				roots.add(synset.getID());
			}
		}

		return roots;
	}

}
