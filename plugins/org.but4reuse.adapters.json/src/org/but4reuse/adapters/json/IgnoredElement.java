package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

import com.eclipsesource.json.JsonValue;

public class IgnoredElement extends AbstractElement {
	public JsonValue jsonValue;
	public IElement parent;

	public IgnoredElement(JsonValue jsonValue, IElement parent) {
		this.jsonValue = jsonValue;
		this.parent = parent;
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof IgnoredElement) {
			IgnoredElement ignoredElement = (IgnoredElement) anotherElement;
			return this.parent.similarity(ignoredElement.parent);
		}
		return 0;
	}

	@Override
	public String getText() {
		return parent.getText() + "_/IGNORED/_" + jsonValue.toString();
	}
}
