package org.but4reuse.adapters.json.tools;

import org.but4reuse.adapters.json.AbstractJsonElement;

import com.eclipsesource.json.JsonValue;

public class JsonElement {
	public Paths paths;
	public JsonValue jsonValue;
	public AbstractJsonElement parent;
	public AbstractJsonElement dependency;

	public JsonElement(Paths paths, JsonValue jsonValue, AbstractJsonElement parent, AbstractJsonElement dependency) {
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
