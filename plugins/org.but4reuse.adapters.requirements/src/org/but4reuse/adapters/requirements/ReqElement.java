package org.but4reuse.adapters.requirements;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.utils.nlp.similarity.TextSimilarity;

public class ReqElement extends AbstractElement {

	private String description;
	
	@Override
	public double similarity(IElement anotherElement) {
		if(anotherElement instanceof ReqElement){
			String anotherDescription = ((ReqElement)anotherElement).getDescription();
			// integrating Natural Language Processing techniques
			return TextSimilarity.getSimilarityWUP(description, anotherDescription);
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

}
