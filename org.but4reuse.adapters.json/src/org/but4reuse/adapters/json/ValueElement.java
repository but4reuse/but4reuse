package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class ValueElement extends AbstractElement implements IJsonElement {
	public JsonValue value;
	public IJsonValuedElement parent;

	public ValueElement(JsonValue value, IJsonValuedElement parent) {
		this.value = value;
		this.parent = parent;
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof ValueElement) {
			ValueElement obj = (ValueElement) anotherElement;

			if (this.value.equals(obj.value) && this.parent.similarity(obj.parent) == 1)
				return 1;
		}
		return 0;
	}

	@Override
	public String getText() {
		return this.parent.getText(this.value.toString());
	}

	@Override
	public String getText(String childrenText) {
		return this.parent.getText(this.value.toString());
	}

	@Override
	public int getMaxDependencies(String dependencyID) {
		return 0;
	}

	@Override
	public int getMinDependencies(String dependencyID) {
		return 0;
	}

	@Override
	public JsonValue construct(JsonObject root) {
		return this.parent.construct(root, this.value);
	}
}
