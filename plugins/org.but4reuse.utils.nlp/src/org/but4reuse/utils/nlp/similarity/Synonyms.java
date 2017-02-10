package org.but4reuse.utils.nlp.similarity;

import java.util.Iterator;
import java.util.List;

import org.but4reuse.utils.nlp.POSUtils;
import org.but4reuse.utils.nlp.WordNetUtils;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

/**
 * Check if two words are synonym
 * 
 * @author jabier.martinez
 * 
 */
public class Synonyms {

	public static boolean isSynonym(String word1, String word2) {
		try {
			IDictionary dict = WordNetUtils.getDictionary();

			List<POS> possiblePOS = POSUtils.getPartsOfSpeech(word1);
			if (possiblePOS.isEmpty()) {
				return false;
			}

			// All possible parts of speech: example house can be noun and verb
			for (POS pos : possiblePOS) {
				IIndexWord idxWord = dict.getIndexWord(word1, pos);
				if (idxWord.getWordIDs().isEmpty()) {
					// continue with next pos
					continue;
				}

				IWordID wordID = idxWord.getWordIDs().get(0);
				IWord word = dict.getWord(wordID);
				ISynset synset = word.getSynset();

				// get the hypernyms
				List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);

				// check hypernyms and synonyms
				List<IWord> words;
				for (ISynsetID sid : hypernyms) {
					words = dict.getSynset(sid).getWords();
					for (Iterator<IWord> i = words.iterator(); i.hasNext();) {
						IWord iw = i.next();
						if (iw.getLemma().equals(word2)) {
							return true;
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

}
