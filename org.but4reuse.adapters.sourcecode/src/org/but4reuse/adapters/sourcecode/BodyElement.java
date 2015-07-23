package org.but4reuse.adapters.sourcecode;

import java.util.ArrayList;
import java.util.StringTokenizer;

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

		StringTokenizer tk = new StringTokenizer(body, " :!?=*+²&~\"#'{}()[]-|`_\\^°,.;/§");
		while (tk.hasMoreTokens()) {
			String w = tk.nextToken();
			if (checkWord(w))
				words.add(w);
		}
		return words;
	}

	public static boolean checkWord(String w) {
		if (w.compareToIgnoreCase("int") == 0 || w.compareToIgnoreCase("integer") == 0
				|| w.compareToIgnoreCase("float") == 0 || w.compareToIgnoreCase("boolean") == 0
				|| w.compareToIgnoreCase("double") == 0 || w.compareToIgnoreCase("string") == 0
				|| w.compareToIgnoreCase("long") == 0 || w.compareToIgnoreCase("char") == 0
				|| w.compareToIgnoreCase("unsigned") == 0 || w.compareToIgnoreCase("public") == 0
				|| w.compareToIgnoreCase("private") == 0 || w.compareToIgnoreCase("final") == 0
				|| w.compareToIgnoreCase("static") == 0 || w.compareToIgnoreCase("this") == 0
				|| w.compareToIgnoreCase("while") == 0 || w.compareToIgnoreCase("for") == 0
				|| w.compareToIgnoreCase("null") == 0 || w.compareToIgnoreCase("do") == 0
				|| w.compareToIgnoreCase("new") == 0 || w.compareToIgnoreCase("new") == 0
				|| w.compareToIgnoreCase("if") == 0 || w.compareToIgnoreCase("else") == 0)
			return false;
		return true;
	}

}
