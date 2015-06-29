package org.but4reuse.adapters.json;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public interface IJsonValuedElement extends IJsonElement
{
	public JsonValue construct(JsonObject root, JsonValue value);
}
