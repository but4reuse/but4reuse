package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

public class KeyElement extends AbstractElement {
	public String name;
	public ObjectElement parent;

	public KeyElement(String name, ObjectElement parent) {
		this.name = name;
		this.parent = parent;
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof KeyElement) {
			KeyElement keyElement = (KeyElement) anotherElement;
			if (this.name.compareTo(keyElement.name) == 0) {
				if (this.parent == null) {
					if (keyElement.parent == null) {
						return 1;
					} else {
						return 0;
					}
				} else {
					if (keyElement.parent == null) {
						return 0;
					} else {
						return this.parent.similarity(keyElement.parent);
					}
				}
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		if (parent.parent == null) {
			return name;
		} else {
			return parent.getText() + "_" + name;
		}
	}

	@Override
	public int getMaxDependencies(String dependencyID) {
		return 1;
	}

	@Override
	public int getMinDependencies(String dependencyID) {
		return 1;
	}
}
