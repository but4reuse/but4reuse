package org.but4reuse.adapters.sourcecode;

import java.util.ArrayList;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

/**
 * 
 * @author jabier.martinez
 */
public class BodyElement extends AbstractElement {

	private String body;

	private FSTNodeElement parent;

	@Override
	public double similarity(IElement anotherElement) {
		// check same class
		if (!(anotherElement instanceof BodyElement)) {
			return 0;
		}
		// check same content
		BodyElement anotherBodyElement = (BodyElement) anotherElement;
		if (!anotherBodyElement.getBody().equals(getBody())) {
			return 0;
		}
		// check same parents
		return FSTNodeElement.ancestorsSimilarity(parent, anotherBodyElement.getParent());
	}

	@Override
	public String getText() {
		String text = "";
		text = body.replaceAll("\n", " ");
		text = text.replaceAll("\r", " ");
		return parent.getText() + " Body: " + text;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public FSTNodeElement getParent() {
		return parent;
	}

	public void setParent(FSTNodeElement parent) {
		this.parent = parent;
	}

	@Override
	public ArrayList<String> getWords() {
		ArrayList<String> words = new ArrayList<String>();

		if (parent != null) {
			words.addAll(parent.getWords());
		}
		return words;
	}

}
