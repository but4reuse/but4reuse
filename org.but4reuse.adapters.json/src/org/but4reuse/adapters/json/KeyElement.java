package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class KeyElement extends AbstractJsonElement {
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
					if (keyElement.parent == null)
						return 1;
					else
						return 0;
				} else {
					if (keyElement.parent == null)
						return 0;
					else
						this.parent.similarity(keyElement.parent);
				}
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		if (parent == null)
			return name;
		else
			return parent.getText() + "_" + name;
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
	public JsonValue construct(JsonObject root) {
		return this.construct(root, JsonValue.NULL);
	}

	@Override
	public JsonValue construct(JsonObject root, JsonValue jsonValue) {
		JsonObject jsonObject = null;

		if (this.parent == null)
			jsonObject = root;
		else
			jsonObject = this.parent.construct(root).asObject();

		JsonValue lastJsonValue = jsonObject.get(this.name);

		if (lastJsonValue == null || lastJsonValue == JsonValue.NULL) {
			jsonObject.set(name, jsonValue);
			return jsonValue;
		}
		if (jsonValue == JsonValue.NULL)
			return lastJsonValue;

		jsonObject.set(name, jsonValue);
		return jsonValue;
	}

}
