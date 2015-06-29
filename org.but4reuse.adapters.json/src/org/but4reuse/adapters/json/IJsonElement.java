package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public interface IJsonElement extends IElement
{
	public JsonValue construct(JsonObject root);
}
