package org.but4reuse.adapters.ui.views;

import java.util.List;
import java.util.Map;

import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.eclipse.PluginElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.feature.constraints.impl.BinaryRelationConstraintsDiscovery;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

public class BlockContentProvider implements IGraphEntityContentProvider {

	private List<Block> blockList;
	
	public List<Block> getBlockList() {
		return blockList;
	}

	public void setBlockList(List<Block> blockList) {
		this.blockList = blockList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		return ((List<Block>)inputElement).toArray();
	}

	@Override
	public Object[] getConnectedTo(Object entity) {
		if(entity instanceof Block) {
			
			/*
			List<Block> dependencies =
			BinaryRelationConstraintsDiscovery.getAllDependencies(((Block)entity), blockList);
			if(dependencies == null) 
				return null;
			
			return dependencies.toArray();
		    */
		
		}
		return null;
	}

	@Override
	public void dispose() { }

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { }
	
}
