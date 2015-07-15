package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class ValueElement extends AbstractElement implements IJsonElement {
	
	public JsonValue value; /*String | Number | Boolean | Null*/
	public IJsonElement parent;
	
	public ValueElement(JsonValue value, IJsonElement parent) {
		this.value = value;
		this.parent = parent;
	}
	
	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof ValueElement) {
			ValueElement elt = (ValueElement) anotherElement;
			
			if (this.value.equals(elt.value)) {
				return this.parent.similarity(elt.parent);
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		return this.parent.getText() + "(value : " + this.value.toString() + ")";
	}
	
	@Override
	public int getMaxDependencies(String dependencyID) {
		return 0;
	}
	
	@Override
	public int getMinDependencies(String dependencyID) {
		return 0;
	}

	@Override
	public JsonValue construct(JsonObject root, JsonValue value) {
		return this.parent.construct(root, this.value);
	}

}
