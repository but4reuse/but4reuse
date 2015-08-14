package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class ValueElement extends AbstractJsonElement {
	public AbstractJsonElement parent;
	public JsonValue jsonValue;

	public ValueElement(AbstractJsonElement parent, JsonValue jsonValue) {
		this.parent = parent;
		this.jsonValue = jsonValue;
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof ValueElement) {
			ValueElement valueElement = (ValueElement) anotherElement;
			if (this.jsonValue.equals(valueElement.jsonValue))
				return this.parent.similarity(valueElement.parent);
		}
		return 0;
	}

	@Override
	public String getText() {
		return parent.getText() + "_" + jsonValue.toString();
	}

	@Override
	public JsonValue construct(JsonObject root) {
		return this.parent.construct(root, this.jsonValue);
	}

	@Override
	public JsonValue construct(JsonObject root, JsonValue jsonValue) {
		return this.construct(root);
	}

}
