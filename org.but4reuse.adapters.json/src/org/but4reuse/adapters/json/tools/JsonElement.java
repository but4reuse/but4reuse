package org.but4reuse.adapters.json.tools;

import org.but4reuse.adapters.impl.AbstractElement;
import com.eclipsesource.json.JsonValue;

public class JsonElement {
	public Paths paths;
	public JsonValue jsonValue;
	public AbstractElement parent;
	public AbstractElement dependency;

	public JsonElement(Paths paths, JsonValue jsonValue,
			AbstractElement parent, AbstractElement dependency) {
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
