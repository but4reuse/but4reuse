package org.but4reuse.utils.nlp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class UselessWordsRemover {

	public static POSTaggerME tagger = null;

	public static void removeUselessWords(List<String> words) {

		InputStream modelIn = null;

		// http://www.comp.leeds.ac.uk/amalgam/tagsets/upenn.html
		// IN Preposition or subordinating conjunction
		// DT Determiner: This The
		// TO To
		// CC Coordinating conjunction: And
		// WDT WH-determiner that what whatever which whichever
		List<String> tagsToRemoveList = Arrays.asList("IN", "DT", "TO", "CC", "WDT");
		try {
			// Create it only once, expensive otherwise
			if (tagger == null) {
				modelIn = new UselessWordsRemover().getClass().getClassLoader()
						.getResourceAsStream("en-pos-maxent.bin");
				POSModel model = new POSModel(modelIn);
				tagger = new POSTaggerME(model);
			}
			String in[] = new String[words.size()];
			for (int i = 0; i < words.size(); i++) {
				in[i] = new String(words.get(i));
			}
			// tag. out contains the tags in the same order as the words in in
			String[] out = tagger.tag(in);
			int cpt = 0;
			for (int i = 0; i < out.length; i++) {
				if (tagsToRemoveList.contains(out[i])) {
					// remove it
					words.remove(i - cpt);
					cpt++;
				}
			}
		} catch (Exception e) {
			// Model loading failed, handle the error
			e.printStackTrace();
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

}
