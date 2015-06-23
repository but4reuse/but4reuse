package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

public class ObjectElement extends AbstractElement implements IJsonObject
{
	public IJsonObject parent;
	
	public ObjectElement(IJsonObject parent)
	{
		this.parent = parent;
	}
	
	@Override
	public double similarity(IElement anotherElement)
	{
		if (anotherElement instanceof ObjectElement)
		{
			ObjectElement obj = (ObjectElement) anotherElement;
			
			if( this.parent.similarity(obj.parent) == 1 )
				return 1;
		}
		return 0;
	}

	@Override
	public String getText()
	{
		return (this.parent.getText() + " -> {}");
	}

}
