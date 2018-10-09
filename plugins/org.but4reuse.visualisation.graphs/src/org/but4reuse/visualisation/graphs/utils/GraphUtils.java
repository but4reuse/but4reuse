package org.but4reuse.visualisation.graphs.utils;

import java.io.File;

import org.but4reuse.utils.files.FileUtils;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;

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

	/**
	 * Default save graph as GraphML
	 * 
	 * @param graph
	 * @param file
	 * @return
	 */
	public static boolean saveGraph(Graph graph, File file) {
		try {
			GraphMLWriter writer = new GraphMLWriter(graph);
			writer.setNormalize(true);
			writer.outputGraph(file.getAbsolutePath());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Save as Trivial Graph Format
	 * 
	 * @param graph
	 * @param file
	 * @return
	 */
	public static boolean saveAsTGFGraph(Graph graph, File file) {
		StringBuilder sb = new StringBuilder();
		for (Vertex node : graph.getVertices()) {
			sb.append(node.getId() + " " + node.getProperty("Label") + "\n");
		}
		sb.append("#\n");
		for (Edge e : graph.getEdges()) {
			sb.append(e.getVertex(Direction.OUT).getId() + " " + e.getVertex(Direction.IN).getId() + " "
					+ e.getProperty("Label") + "\n");
		}

		try {
			FileUtils.appendToFile(file, sb.toString());
			return true;
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}
}
