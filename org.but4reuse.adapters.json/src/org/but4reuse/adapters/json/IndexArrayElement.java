package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;

public class IndexArrayElement extends AbstractJsonObject
{
	public int index;
	public AbstractJsonObject parent;
	
	public IndexArrayElement(int index, AbstractJsonObject parent)
	{
		this.index = index;
		this.parent = parent;
	}
	
	@Override
	public double similarity(IElement anotherElement)
	{
		if (anotherElement instanceof IndexArrayElement)
		{
			IndexArrayElement obj = (IndexArrayElement) anotherElement;
			
			if( this.index == obj.index && this.parent.similarity(obj.parent) == 1 )
				return 1;
		}
		return 0;
	}

	@Override
	public String getText()
	{
		return (this.parent.getText() + " -> [" + this.index + "]");
	}
	
	@Override
	public int getMaxDependencies(String dependencyID) {
		return 1;
	}

	@Override
	public int getMinDependencies(String dependencyID) {
		return 1;
	}
}
