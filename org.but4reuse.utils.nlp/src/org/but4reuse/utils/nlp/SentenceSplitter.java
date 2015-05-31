package org.but4reuse.utils.nlp;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/**
 * Sentence Splitter
 * 
 * @author jabier.martinez
 */
public class SentenceSplitter {

	/**
	 * split text in sentences
	 * 
	 * @param text
	 * @return sentences
	 */
	public static String[] split(String text) {
		SentenceDetector sentenceDetector = null;
		InputStream modelIn = null;
		try {
			// load the model http://opennlp.sourceforge.net/models-1.5/
			modelIn = new SentenceSplitter().getClass().getClassLoader().getResourceAsStream("en-sent.bin");
			final SentenceModel sentenceModel = new SentenceModel(modelIn);
			modelIn.close();
			sentenceDetector = new SentenceDetectorME(sentenceModel);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sentenceDetector.sentDetect(text);
	}

}
