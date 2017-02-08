package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

import com.eclipsesource.json.JsonValue;

public class ValueElement extends AbstractElement {
	public IElement parent;
	public JsonValue jsonValue;

	public ValueElement(IElement parent, JsonValue jsonValue) {
		this.parent = parent;
		this.jsonValue = jsonValue;
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof ValueElement) {
			ValueElement valueElement = (ValueElement) anotherElement;
			if (this.jsonValue.equals(valueElement.jsonValue)) {
				return this.parent.similarity(valueElement.parent);
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		return parent.getText() + "_" + jsonValue.toString();
	}
}
