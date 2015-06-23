package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;


public class KeyElement extends AbstractJsonObject
{
	public String key;
	public AbstractJsonObject parent;
	
	public KeyElement(String key, AbstractJsonObject parent)
	{
		this.key = key;
		this.parent = parent;
	}
	
	public KeyElement(String key)
	{
		this.key = key;
		this.parent = null;
	}
	
	@Override
	public double similarity(IElement anotherElement)
	{
		if (anotherElement instanceof KeyElement)
		{
			KeyElement keyElt = (KeyElement) anotherElement;
			
			if( this.parent == null )
			{
				if( this.key.compareTo(keyElt.key) == 0 )
					return 1;
			}
			else
			{
				if( this.key.compareTo(keyElt.key) == 0 && this.parent.similarity(keyElt.parent) == 1 )
					return 1;
			}
		}
		return 0;
	}
	
	@Override
	public String getText()
	{
		if(this.parent != null)
		{
			return (this.parent.getText() + " -> (key : " + this.key + ")");
		}
		else
		{
			return "(key : " + this.key + ")";
		}
	}
	/*
	@Override
	public JsonValue construct(JsonValue json)
	{
		if(this.parent != null)
		{
			json = this.parent.construct(json);
		}
		
		if(json.isObject())
		{
			JsonObject jsonObj = (JsonObject) json;
			
			if(jsonObj.get(this.key) == null)
				jsonObj.add(this.key, JsonValue.NULL);
			
			return jsonObj.get(this.key);
		}
		else
		{
			return null;
		}
	}
	*/
	
	@Override
	public int getMaxDependencies(String dependencyID) {
		return 1;
	}

	@Override
	public int getMinDependencies(String dependencyID) {
		return 1;
	}
}
