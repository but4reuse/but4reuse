package org.but4reuse.graphs.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class GraphUtils {
	
	public static DirectedGraph<IElement, DefaultEdge> createDirectedGraph(List<IElement> elements) {
		return createDirectedGraph(elements, false);
	}

	/**
	 * Create a directed graph using the elements' dependencies
	 * @param elements
	 * @param onlyContainment
	 * @return A directed graph
	 */
	public static DirectedGraph<IElement, DefaultEdge> createDirectedGraph(List<IElement> elements,
			boolean onlyContainment) {
		DirectedGraph<IElement, DefaultEdge> graph = new DefaultDirectedGraph<IElement, DefaultEdge>(DefaultEdge.class);
		// Add nodes
		for (IElement element : elements) {
			graph.addVertex(element);
		}
		// Add edges
		for (IElement element : elements) {
			Collection<List<IDependencyObject>> listOfElements = null;
			if (!onlyContainment) {
				// all dependencies
				listOfElements = element.getDependencies().values();
			} else {
				// only the element-parent relationships
				listOfElements = new ArrayList<List<IDependencyObject>>();
				listOfElements.add(AdaptersHelper.getParentElements(element));				
			}
			for (List<IDependencyObject> l : listOfElements) {
				for (IDependencyObject ido : l) {
					if (ido instanceof IElement) {
						if (elements.contains(ido)) {
							graph.addEdge(element, (IElement) ido);
						}
					}
				}
			}
		}
		return graph;
	}
}
