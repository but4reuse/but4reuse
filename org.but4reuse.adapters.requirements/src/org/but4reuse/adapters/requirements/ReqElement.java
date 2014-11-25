package org.but4reuse.adapters.requirements;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.impl.WuPalmer;


public class ReqElement extends AbstractElement {

	private String description;
	
	@Override
	public double similarity(IElement anotherElement) {
		if(anotherElement instanceof ReqElement){
			String anotherDescription = ((ReqElement)anotherElement).getDescription();
			// integrating Natural Language Processing techniques
			return calculateSimilarity(this.getDescription(),anotherDescription);
		}
		return 0;
	}

	@Override
	public String getText() {
		return description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// Wu and Palmer algorithm
	private double calculateSimilarity(String text1, String text2) {
		WS4JComparer ws4jcomparer = new WS4JComparer(new WuPalmer(new NictWordNet()));
		float similarity = ws4jcomparer.CompareText(text1, text2);
		return similarity;
	}
}
