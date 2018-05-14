package org.but4reuse.adapters.eclipse.benchmark;

import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.eclipse.EclipseAdapter;
import org.but4reuse.adapters.eclipse.FileElement;
import org.but4reuse.adapters.eclipse.PluginElement;

/**
 * Eclipse adapter for benchmark. Do not include file elements
 */
public class EclipseAdapter4Benchmark extends EclipseAdapter {

	@Override
	protected void addElement(List<IElement> elements, FileElement newElement) {
		if (newElement instanceof PluginElement) {
			elements.add(newElement);
		}
	}

}
