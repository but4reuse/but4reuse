package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class KeyElement extends AbstractElement implements IJsonElement {

	public String key;
	public IJsonElement parent;

	public KeyElement(String key, IJsonElement parent) {
		this.key = key;
		this.parent = parent;
	}

	public KeyElement(String key) {
		this.key = key;
		this.parent = null;
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof KeyElement) {
			KeyElement elt = (KeyElement) anotherElement;

			if (this.parent == null) {
				if (elt.parent == null && this.key.compareTo(elt.key) == 0) {
					return 1;
				}
			} else {
				if (this.key.compareTo(elt.key) == 0) {
					return this.parent.similarity(elt.parent);
				}
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		if (this.parent == null) {
			return "(key : " + this.key + ")";
		} else {
			return this.parent.getText() + "(key : " + this.key + ")";
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

	@Override
	public JsonValue construct(JsonObject root, JsonValue value) {
		JsonObject json;

		if (this.parent == null) {
			json = root;
		} else {
			json = this.parent.construct(root).asObject();
		}

		JsonValue lastValue = json.get(this.key);

		if (lastValue == null || lastValue == JsonValue.NULL) {
			json.set(this.key, value);
			return value;
		}
		if (value == null || value == JsonValue.NULL) {
			return lastValue;
		}
		if (lastValue.isObject() && value.isObject()) {
			return lastValue;
		}
		if (lastValue.isArray() && value.isArray()) {
			return lastValue;
		}

		json.set(this.key, value);
		return value;
	}

	@Override
	public JsonValue construct(JsonObject root) {
		return this.construct(root, JsonValue.NULL);
	}

}
