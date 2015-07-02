package org.but4reuse.visualisation.graphs.utils;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

/**
 * Graph Utils
 * 
 * @author jabier.martinez
 */
public class GraphUtils {

	/**
	 * Clone a graph
	 * 
	 * @param graph
	 * @return a clone of the graph
	 */
	public static Graph cloneGraph(Graph graph) {
		Graph graph2 = new TinkerGraph();
		for (Vertex vertex : graph.getVertices()) {
			Vertex newVertex = graph2.addVertex(vertex.getId());
			for (String key : vertex.getPropertyKeys()) {
				newVertex.setProperty(key, vertex.getProperty(key));
			}
		}
		for (Edge edge : graph.getEdges()) {
			Vertex vOut = graph2.getVertex(edge.getVertex(Direction.OUT).getId());
			Vertex vIn = graph2.getVertex(edge.getVertex(Direction.IN).getId());
			Edge newEdge = graph2.addEdge(edge.getId(), vOut, vIn, edge.getId().toString());
			for (String key : edge.getPropertyKeys()) {
				newEdge.setProperty(key, edge.getProperty(key));
			}
		}
		return graph2;
	}
}
