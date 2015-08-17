package org.but4reuse.adapters.json;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.json.tools.ArrayManager;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class ArrayElement extends AbstractJsonElement {
	public AbstractJsonElement parent;
	public ArrayManager array;
	public List<ArrayElement> similarArrays;

	public ArrayElement(AbstractJsonElement parent) {
		this.parent = parent;
		this.array = null;
		this.similarArrays = new ArrayList<ArrayElement>();
		this.similarArrays.add(this);
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof ArrayElement) {
			ArrayElement arrayElement = (ArrayElement) anotherElement;
			if (this.parent.similarity(arrayElement.parent) == 1) {
				this.similarArrays.add(arrayElement);
				arrayElement.similarArrays.add(this);

				return 1;
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		return parent.getText() + "_[]";
	}

	@Override
	public JsonValue construct(JsonObject root) {
		if (this.array == null) {
			this.array = new ArrayManager();
			for (ArrayElement arrElt : this.similarArrays)
				arrElt.array = this.array;
			this.parent.construct(root, this.array.jsonArray);
		}
		return this.array.jsonArray;
	}

	@Override
	public JsonValue construct(JsonObject root, JsonValue jsonValue) {
		return this.construct(root);
	}
}
