package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class ArrayElement extends AbstractElement implements IJsonElement
{
	public IJsonValuedElement parent;
	
	public ArrayElement(IJsonValuedElement parent)
	{
		this.parent = parent;
	}
	
	@Override
	public double similarity(IElement anotherElement)
	{
		if (anotherElement instanceof ArrayElement)
		{
			ArrayElement obj = (ArrayElement) anotherElement;
			
			if( this.parent.similarity(obj.parent) == 1 )
				return 1;
		}
		return 0;
	}

	@Override
	public String getText()
	{
		return (this.parent.getText() + "_[]");
	}
	
	@Override
	public int getMaxDependencies(String dependencyID) {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getMinDependencies(String dependencyID) {
		return Integer.MIN_VALUE;
	}
	
	@Override
	public JsonValue construct(JsonObject root)
	{
		return this.parent.construct(root, new JsonArray());
	}
}
