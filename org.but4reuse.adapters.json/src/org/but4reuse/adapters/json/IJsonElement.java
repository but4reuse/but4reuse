package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/*
 * represents a node in the json tree representation
 * each element contains its parent and values depending on its type : key, value, object, array
 * the construct method is called by children recursively until root is reached
 * and returns a JsonValue (conversion from tree representation to JsonValue)
 */

public interface IJsonElement extends IElement
{
	public JsonValue construct(JsonObject root);
}
