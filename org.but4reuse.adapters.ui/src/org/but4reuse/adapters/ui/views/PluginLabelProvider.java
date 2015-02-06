package org.but4reuse.adapters.ui.views;

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
		
		if(element instanceof PluginBouchon)
			return ((PluginBouchon)element).getName();
		
		else // Dependency
			return "";
		
	}
	
}
