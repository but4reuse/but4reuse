package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class IndexArrayElement extends AbstractElement implements IJsonValuedElement {
	public int index;
	public ArrayElement parent;

	public IndexArrayElement(int index, ArrayElement parent) {
		this.index = index;
		this.parent = parent;
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof IndexArrayElement) {
			IndexArrayElement obj = (IndexArrayElement) anotherElement;

			if (this.index == obj.index && this.parent.similarity(obj.parent) == 1)
				return 1;
		}
		return 0;
	}

	@Override
	public String getText() {
		return this.parent.getText();
	}

	@Override
	public String getText(String childrenText) {
		return this.parent.getText(childrenText);
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
		JsonArray arr = this.parent.construct(root).asArray();

		if (this.index >= arr.size())
			for (int i = arr.size(); i <= this.index; i++)
				arr.add(JsonValue.NULL);

		JsonValue mergeValue = JsonTools.merge(value, arr.get(this.index));

		arr.set(this.index, mergeValue);

		return mergeValue;
	}
}
