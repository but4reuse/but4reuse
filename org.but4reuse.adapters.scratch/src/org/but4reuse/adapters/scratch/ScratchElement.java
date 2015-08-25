package org.but4reuse.adapters.scratch;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

public class ScratchElement extends AbstractElement {
	public IElement element;

	public ScratchElement(IElement element) {
		super();
		this.element = element;
	}

	@Override
	public double similarity(IElement anotherElement) {
		return this.element
				.similarity(((ScratchElement) anotherElement).element);
	}

	@Override
	public String getText() {
		return this.element.getText();
	}

}
