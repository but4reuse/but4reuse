package org.but4reuse.adapters.javajdt.elements;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

/**
 * Type extends element
 * 
 * @author jabier.martinez
 */
public class TypeExtendsElement extends AbstractElement {

	public TypeElement type;
	public TypeElement extendedType;
	
	@Override
	public double similarity(IElement anotherElement) {

		// Same type of element
		if (anotherElement.getClass().equals(this.getClass())) {

			// Same parent typeElement
			if (type.similarity(((TypeExtendsElement)anotherElement).type) == 1) {

				// Same extending type
				if (extendedType.similarity(((TypeExtendsElement) anotherElement).extendedType) == 1) {
					return 1;
				}

			}
		}
		return 0;
	}

	@Override
	public String getText() {
		return "TypeExtends: " + type.id + " extends " + extendedType.id;
	}

	/**
	 * For the moment, return no words.
	 */
	public List<String> getWords() {
		return new ArrayList<String>();
	}

}
