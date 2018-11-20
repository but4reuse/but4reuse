package org.but4reuse.adapters.javajdt.elements;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

/**
 * Type implements element
 * 
 * @author jabier.martinez
 */
public class TypeImplementsElement extends AbstractElement {

	public TypeElement type;
	public TypeElement implementedType;
	
	@Override
	public double similarity(IElement anotherElement) {

		// Same type of element
		if (anotherElement.getClass().equals(this.getClass())) {

			// Same parent typeElement
			if (type.similarity(((TypeImplementsElement)anotherElement).type) == 1) {

				// Same implementing type
				if (implementedType.similarity(((TypeImplementsElement) anotherElement).implementedType) == 1) {
					return 1;
				}

			}
		}
		return 0;
	}

	@Override
	public String getText() {
		return "TypeImplements: " + type.id + " implements " + implementedType.id;
	}

	/**
	 * For the moment, return no words.
	 */
	public List<String> getWords() {
		return new ArrayList<String>();
	}

}
