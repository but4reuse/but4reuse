package org.but4reuse.adapters.requirements;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

public class ReqElement extends AbstractElement {

	private String description;
	
	@Override
	public double similarity(IElement anotherElement) {
		// TODO integrating Natural Language Processing techniques
		if(anotherElement instanceof ReqElement){
			String anotherDescription = ((ReqElement)anotherElement).getDescription();
			if(description.equals(anotherDescription)){
				return 1;
			}
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
