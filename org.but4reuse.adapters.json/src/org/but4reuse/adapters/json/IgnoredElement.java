package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class IgnoredElement extends AbstractElement implements IJsonElement {

	public JsonValue content;
	public IJsonElement parent;

	public IgnoredElement(JsonValue content, IJsonElement parent) {
		this.content = content;
		this.parent = parent;
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof IgnoredElement) {
			IgnoredElement elt = (IgnoredElement) anotherElement;

			return this.parent.similarity(elt.parent);
		}
		return 0;
	}

	@Override
	public String getText() {
		return this.parent.getText() + "_/ignored/";
	}

	@Override
	public JsonValue construct(JsonObject root) {
		return this.parent.construct(root, content);
	}

	@Override
	public JsonValue construct(JsonObject root, JsonValue value) {
		return construct(root);
	}

}
