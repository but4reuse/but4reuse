package org.but4reuse.adapters.json;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/*
 * extension of IJsonElement
 * here, the value constructed by the children is passed in the function construct (JsonValue, JsonArray, JsonObject)
 * to be merged with the past JsonValue constructed
 */

public interface IJsonValuedElement extends IJsonElement
{
	public JsonValue construct(JsonObject root, JsonValue value);
}
