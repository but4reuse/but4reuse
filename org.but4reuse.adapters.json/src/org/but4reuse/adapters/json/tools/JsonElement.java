package org.but4reuse.adapters.json.tools;

import org.but4reuse.adapters.IElement;

import com.eclipsesource.json.JsonValue;

public class JsonElement {
	public Paths paths;
	public JsonValue jsonValue;
	public IElement parent;
	public IElement dependency;

	public JsonElement(Paths paths, JsonValue jsonValue, IElement parent, IElement dependency) {
		this.paths = paths;
		this.jsonValue = jsonValue;
		this.parent = parent;
		this.dependency = dependency;
	}

	@Override
	public String toString() {
		return paths.toString() + jsonValue.toString();
	}
}
