package org.but4reuse.adapters.nltext;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.utils.nlp.similarity.WS4JComparer;

/**
 * SentenceElement
 * @author jabier.martinez
 */
public class SentenceElement extends AbstractElement {

	private String sentence = null;

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String text) {
		this.sentence = text;
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (!(anotherElement instanceof SentenceElement)) {
			return 0;
		}
		SentenceElement anotherSentence = (SentenceElement) anotherElement;
		double similarity = WS4JComparer.getSimilarityWUP(sentence, anotherSentence.sentence);
		return similarity;
	}

	@Override
	public String getText() {
		return sentence;
	}
	
	@Override
	public ArrayList<String> getWords()
	{
		StringTokenizer tk = new StringTokenizer(sentence," ²&~\"#'{}()[]-|`_\\^°,.;/§");
		ArrayList<String> words = new ArrayList<String>();
		
		while(tk.hasMoreTokens())
			words.add(tk.nextToken());
		
		return words;
	}

}
