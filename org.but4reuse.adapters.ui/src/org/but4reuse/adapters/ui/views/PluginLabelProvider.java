package org.but4reuse.adapters.ui.views;

import org.but4reuse.adapters.eclipse.PluginElement;
import org.eclipse.jface.viewers.LabelProvider;

/** 
 * LabelProvider du graphe. Determine le texte a afficher selon le type  
 * d'element.
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
