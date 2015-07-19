package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class ObjectElement extends AbstractElement implements IJsonElement {

	public IJsonElement parent;

	public ObjectElement(IJsonElement parent) {
		this.parent = parent;
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof ObjectElement) {
			ObjectElement elt = (ObjectElement) anotherElement;

			return this.parent.similarity(elt.parent);
		}
		return 0;
	}

	@Override
	public String getText() {
		return this.parent.getText() + "{}";
	}

	@Override
	public JsonValue construct(JsonObject root, JsonValue value) {
		return this.construct(root);
	}

	@Override
	public JsonValue construct(JsonObject root) {
		return this.parent.construct(root, new JsonObject());
	}

}
