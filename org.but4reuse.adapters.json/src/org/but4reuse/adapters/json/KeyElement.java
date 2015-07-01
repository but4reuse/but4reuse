package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class KeyElement extends AbstractElement implements IJsonValuedElement {
	/*
	 * A keyElement can only have as parent an Object Element { "key" : "value"
	 * }
	 */

	public String key;
	public ObjectElement parent;

	public KeyElement(String key, ObjectElement parent) {
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
			KeyElement keyElt = (KeyElement) anotherElement;

			if (this.parent == null) {
				if (this.key.compareTo(keyElt.key) == 0 && keyElt.parent == null)
					return 1;
			} else {
				if (this.key.compareTo(keyElt.key) == 0 && this.parent.similarity(keyElt.parent) == 1)
					return 1;
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		return this.getText("");
	}

	@Override
	public String getText(String childrenText) {
		String text = "\"" + this.key + "\" : " + childrenText;

		if (this.parent == null) {
			return ("{" + text + "}");
		} else {
			return this.parent.getText(text);
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
	public JsonValue construct(JsonObject root) {
		return this.construct(root, JsonValue.NULL);
	}

	@Override
	public JsonValue construct(JsonObject root, JsonValue value) {
		JsonObject jsonObj = null;

		if (this.parent == null)
			jsonObj = root;
		else
			jsonObj = this.parent.construct(root).asObject();

		/*
		 * here, we merge the value passed with a possible value already
		 * containing in 'key' for example, if 'key' contains an array or an
		 * object
		 */

		JsonValue mergeValue = JsonTools.merge(value, jsonObj.get(this.key));

		jsonObj.set(this.key, mergeValue);

		return mergeValue;
	}
}
