package org.but4reuse.adapters.json;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.json.tools.AdapterTools;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class ArrayElement extends AbstractJsonElement {
	public AbstractJsonElement parent;
	public int id;
	public List<ArrayElement> similarArrays;

	public ArrayElement(AbstractJsonElement parent) {
		this.parent = parent;
		this.id = AdapterTools.getUniqueId();
		this.similarArrays = new ArrayList<ArrayElement>();
		this.similarArrays.add(this);
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof ArrayElement) {
			ArrayElement arrayElement = (ArrayElement) anotherElement;
			if (this.id == arrayElement.id)
				return 1;
			if (this.parent.similarity(arrayElement.parent) == 1) {
				List<ArrayElement> arrays = new ArrayList<ArrayElement>();
				arrays.addAll(this.similarArrays);
				arrays.addAll(arrayElement.similarArrays);
				for (ArrayElement array : arrays) {
					array.id = this.id;
					array.similarArrays = arrays;
				}
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
		return this.parent.construct(root, new JsonArray());
	}

	@Override
	public JsonValue construct(JsonObject root, JsonValue jsonValue) {
		return this.construct(root);
	}
}
