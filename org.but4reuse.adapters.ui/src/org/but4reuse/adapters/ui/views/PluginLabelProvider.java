package org.but4reuse.adapters.ui.views;

import org.but4reuse.adapters.eclipse.PluginElement;
import org.eclipse.jface.viewers.LabelProvider;

/** 
 * LabelProvider of the graph. Choose the text to display depending on
 * the type of element.
 * @author Selma Sadouk
 * @author Julia Wisniewski
 */
public class PluginLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		
		if(element instanceof PluginElement)
			return ((PluginElement)element).getPluginSymbName();
		
		else // Dependency
			return "";
		
	}
	
}
