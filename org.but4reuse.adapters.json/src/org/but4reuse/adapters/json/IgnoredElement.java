package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class IgnoredElement extends AbstractJsonElement {
	public JsonValue jsonValue;
	public AbstractJsonElement parent;

	public IgnoredElement(JsonValue jsonValue, AbstractJsonElement parent) {
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

	@Override
	public JsonValue construct(JsonObject root) {
		return this.parent.construct(root, this.jsonValue);
	}

	@Override
	public JsonValue construct(JsonObject root, JsonValue jsonValue) {
		return this.construct(root);
	}

}
