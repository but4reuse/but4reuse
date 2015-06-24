package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;

import com.eclipsesource.json.JsonValue;

public class ValueElement extends AbstractJsonObject
{
	public JsonValue value;
	public AbstractJsonObject parent;
	
	public ValueElement(JsonValue value, AbstractJsonObject parent)
	{
		this.value = value;
		this.parent = parent;
	}
	
	@Override
	public double similarity(IElement anotherElement)
	{
		if (anotherElement instanceof ValueElement)
		{
			ValueElement obj = (ValueElement) anotherElement;
			
			if( this.value.equals(obj.value) && this.parent.similarity(obj.parent) == 1 )
				return 1;
		}
		return 0;
	}
	
	@Override
	public String getText()
	{
		return (this.parent.getText() + "_(value : " + this.value + ")");
	}
	/*
	@Override
	public JsonValue construct(JsonValue json)
	{
		json = parent.construct(json);
		
		JsonObject jsonObj = (JsonObject) json;
		
		jsonObj.
	}
	*/
/*	
	@Override
	public int getMaxDependencies(String dependencyID) {
		return 1;
	}

	@Override
	public int getMinDependencies(String dependencyID) {
		return 1;
	}
*/
	@Override
	public int getMaxDependencies(String dependencyID) {
		return 0;
	}

	@Override
	public int getMinDependencies(String dependencyID) {
		return 0;
	}
}
