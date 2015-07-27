package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class ObjectElement extends AbstractElement implements IJsonElement {

	public IJsonElement parent;
	public JsonObject content;

	public ObjectElement(IJsonElement parent) {
		this.parent = parent;
		this.content = null;
	}

	public ObjectElement(IJsonElement parent, JsonObject content) {
		this.parent = parent;
		this.content = content;
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof ObjectElement) {
			ObjectElement elt = (ObjectElement) anotherElement;

			if (this.content == null) {
				if (elt.content == null) {
					return this.parent.similarity(elt.parent);
				}
			} else {
				if (elt.content != null && this.content.equals(elt.content)) {
					return this.parent.similarity(elt.parent);
				}
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		if (this.content == null) {
			return this.parent.getText() + "_{}";
		} else {
			return this.parent.getText() + "_{}_" + this.content.toString();
		}
	}

	@Override
	public JsonValue construct(JsonObject root, JsonValue value) {
		return this.parent.construct(root, value);
	}

	@Override
	public JsonValue construct(JsonObject root) {
		if (this.content == null) {
			return this.parent.construct(root, new JsonObject());
		} else {
			return this.parent.construct(root, this.content);
		}
	}
}
