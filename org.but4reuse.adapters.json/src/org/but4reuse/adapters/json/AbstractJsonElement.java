package org.but4reuse.adapters.json;

import org.but4reuse.adapters.impl.AbstractElement;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public abstract class AbstractJsonElement extends AbstractElement {
	public abstract JsonValue construct(JsonObject root);

	public abstract JsonValue construct(JsonObject root, JsonValue jsonValue);
}
