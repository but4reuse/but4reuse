package org.but4reuse.adapters.javajdt.elements;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.javajdt.JDTParser;
import org.but4reuse.utils.strings.StringUtils;

/**
 * Method Body Element
 * 
 * @author jabier.martinez
 */
public class MethodBodyElement extends AbstractElement {

	public String body;

	@Override
	public double similarity(IElement anotherElement) {

		// Same type of element
		if (anotherElement.getClass().equals(this.getClass())) {

			// Same parent methodElement
			IElement thisMethod = (IElement) getDependencies().get("methodBody").get(0);
			IElement anotherMethod = (IElement) anotherElement.getDependencies().get("methodBody").get(0);
			if (thisMethod.similarity(anotherMethod) == 1) {

				// Same methodBody content
				if (((MethodBodyElement) anotherElement).body.equals(body)) {
					return 1;
				}

			}
		}
		return 0;
	}

	@Override
	public String getText() {
		return "MethodBody: " + StringUtils.removeNewLines(body);
	}

	/**
	 * If we take methodBody text this will provide a lot of words which will
	 * affect the words from other types of elements. For the moment, return no
	 * words.
	 */
	public List<String> getWords() {
		return new ArrayList<String>();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		return result;
	}
	
	@Override
	public boolean isContainment(String dependencyId) {
		return dependencyId.equals(JDTParser.DEPENDENCY_METHOD_BODY);
	}

}
