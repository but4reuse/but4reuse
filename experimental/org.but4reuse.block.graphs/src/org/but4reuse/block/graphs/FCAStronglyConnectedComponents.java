package org.but4reuse.block.graphs;

import java.util.List;
import java.util.Set;

import org.but4reuse.adapters.IElement;
import org.but4reuse.block.identification.IBlockIdentification;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.GabowStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.graph.DefaultEdge;

/**
 * FCA + strongly connected components
 * 
 * @author jabier.martinez
 * 
 */
public class FCAStronglyConnectedComponents extends FCAWeaklyConnectedComponents implements IBlockIdentification {

	@Override	
	public List<Set<IElement>> getConnectedSets(DirectedGraph<IElement, DefaultEdge> graph){
		StrongConnectivityAlgorithm<IElement, DefaultEdge> algo = new GabowStrongConnectivityInspector<IElement, DefaultEdge>(
				graph);
		List<Set<IElement>> connectedSets = algo.stronglyConnectedSets();
		return connectedSets;
	}
	
	public String getNameForMonitor() {
		return "strongly connected components";
	}

}
