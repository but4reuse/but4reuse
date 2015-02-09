package org.but4reuse.adapters.ui.views;

import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.eclipse.PluginElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

/** 
 * ContentProvider of our graph. Input : the list of plugins.
 * @author Selma Sadouk
 * @author Julia Wisniewski
 */ 
public class PluginContentProvider implements IGraphEntityContentProvider {

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		return ((List<PluginElement>)inputElement).toArray();
	}

	/**
	 * For each plugin, the list of plugins from which depend the current plugin is returned.
	 */
	@Override
	public Object[] getConnectedTo(Object entity) {
		if(entity instanceof PluginElement) {
			
			Map<String,List<IDependencyObject>> map =
					((PluginElement)entity).getDependencies();

			List<IDependencyObject> dependencies = map.get(AbstractElement.MAIN_DEPENDENCY_ID);
			if(dependencies == null) 
				return null;
				
			return dependencies.toArray();
		}
		else
			return null;
	}
	
	@Override
	public void dispose() { }

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { }

}
