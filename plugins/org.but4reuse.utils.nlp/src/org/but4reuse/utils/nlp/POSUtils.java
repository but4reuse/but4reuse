package org.but4reuse.utils.nlp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

/**
 * Parts of speech utils
 * 
 * @author jabier.martinez
 */
public class POSUtils {

	public static POSTaggerME tagger = null;

	/**
	 * get the tags in the same order as the words
	 */
	public static String[] getTags(List<String> words) {
		InputStream modelIn = null;
		try {
			// Create it only once, expensive otherwise
			if (tagger == null) {
				modelIn = new POSUtils().getClass().getClassLoader().getResourceAsStream("en-pos-maxent.bin");
				POSModel model = new POSModel(modelIn);
				tagger = new POSTaggerME(model);
			}
			String in[] = new String[words.size()];
			for (int i = 0; i < words.size(); i++) {
				in[i] = new String(words.get(i));
			}
			return tagger.tag(in);
		} catch (Exception e) {
			// Model loading failed, handle the error
			e.printStackTrace();
			return null;
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Modify the list by removing some unnecessary words. Example: the, this,
	 * to
	 * 
	 * @param words
	 */
	public static void removeUnnecessaryWords(List<String> words) {

		// http://www.comp.leeds.ac.uk/amalgam/tagsets/upenn.html
		// IN Preposition or subordinating conjunction
		// DT Determiner: This The
		// TO To
		// CC Coordinating conjunction: And
		// WDT WH-determiner that what whatever which whichever
		List<String> tagsToRemoveList = Arrays.asList("IN", "DT", "TO", "CC", "WDT");
		String[] out = getTags(words);
		int cpt = 0;
		for (int i = 0; i < out.length; i++) {
			if (tagsToRemoveList.contains(out[i])) {
				// remove it
				words.remove(i - cpt);
				cpt++;
			}
		}
	}

	/**
	 * Get possible parts of speech of a word
	 */
	public static List<POS> getPartsOfSpeech(String word) {
		List<POS> posList = new ArrayList<POS>();

		IDictionary dict = WordNetUtils.getDictionary();
		WordnetStemmer stemmer = new WordnetStemmer(dict);
		for (POS pos : POS.values()) {
			for (String stem : stemmer.findStems(word, pos)) {
				IIndexWord iw = dict.getIndexWord(stem, pos);
				if (iw != null) {
					posList.add(pos);
				}
			}
		}

		return posList;
	}
}
