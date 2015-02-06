package org.but4reuse.adapters.ui.views;

import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

/** 
 * ContentProvider de notre graphe. Cette implementation prend en donnees  
 * d'entree la liste des plugins.
 * @author Selma Sadouk
 * @author Julia Wisniewski
 */ 
public class PluginContentProvider implements IGraphEntityContentProvider {

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		return ((List<PluginBouchon>)inputElement).toArray();
	}

	/**
	 * Pour chaque plugin, on retourne la liste des plugins dont depend le plugin courant.
	 */
	@Override
	public Object[] getConnectedTo(Object entity) {
		if(entity instanceof PluginBouchon)
			return ((PluginBouchon)entity).getDependencies().toArray();
		else
			return null;
	}
	
	@Override
	public void dispose() { }

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { }

}
