package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class ObjectElement extends AbstractJsonElement {
	public AbstractJsonElement parent;

	public ObjectElement(AbstractJsonElement parent) {
		this.parent = parent;
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof ObjectElement) {
			ObjectElement objectElement = (ObjectElement) anotherElement;
			return this.parent.similarity(objectElement.parent);
		}
		return 0;
	}

	@Override
	public String getText() {
		return parent.getText() + "_{}";
	}

	@Override
	public JsonValue construct(JsonObject root) {
		return this.parent.construct(root, new JsonObject());
	}

	@Override
	public JsonValue construct(JsonObject root, JsonValue jsonValue) {
		return this.construct(root);
	}

}
