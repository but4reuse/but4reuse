package org.but4reuse.graphs.utils;

import java.util.List;

import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.IElement;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class GraphUtils {

	public static DirectedGraph<IElement, DefaultEdge> createDirectedGraph(Block block) {
		return createDirectedGraph(AdaptedModelHelper.getElementsOfBlock(block));
	}

	public static DirectedGraph<IElement, DefaultEdge> createDirectedGraph(List<IElement> elements) {
		DirectedGraph<IElement, DefaultEdge> graph = new DefaultDirectedGraph<IElement, DefaultEdge>(DefaultEdge.class);
		for (IElement element : elements) {
			graph.addVertex(element);
		}
		for (IElement element : elements) {
			for(List<IDependencyObject> l : element.getDependencies().values()){
				for(IDependencyObject ido : l){
					if(ido instanceof IElement){
						if(elements.contains(ido)){
							graph.addEdge(element, (IElement)ido);
						}
					}
				}
			}
		}
		return graph;
	}
}
