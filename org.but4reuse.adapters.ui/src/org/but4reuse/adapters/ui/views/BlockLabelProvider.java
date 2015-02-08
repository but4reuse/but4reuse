package org.but4reuse.adapters.ui.views;

import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adapters.IElement;
import org.eclipse.jface.viewers.LabelProvider;

public class BlockLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {

		if(element instanceof Block)
			return ((Block)element).getName();
		
		else // Dependency
			return "";
		
	}
	
}
