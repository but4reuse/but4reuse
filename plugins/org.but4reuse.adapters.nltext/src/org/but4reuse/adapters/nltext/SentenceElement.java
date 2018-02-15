package org.but4reuse.adapters.nltext;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.utils.nlp.similarity.TextSimilarity;

/**
 * SentenceElement
 * 
 * @author jabier.martinez
 */
public class SentenceElement extends AbstractElement {

	protected String sentence = null;

	@Override
	public double similarity(IElement anotherElement) {
		if (!(anotherElement instanceof SentenceElement)) {
			return 0;
		}
		SentenceElement anotherSentence = (SentenceElement) anotherElement;
		double similarity = TextSimilarity.getSimilarityWUP(sentence, anotherSentence.sentence);
		return similarity;
	}

	@Override
	public String getText() {
		return sentence;
	}

}