package org.but4reuse.adapters.eclipse;

import java.util.List;

import org.but4reuse.adapters.IElement;

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
