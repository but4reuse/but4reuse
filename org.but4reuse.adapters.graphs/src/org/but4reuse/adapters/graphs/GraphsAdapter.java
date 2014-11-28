package org.but4reuse.adapters.graphs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;

/**
 * Graphs Adapter
 * 
 * @author jabier.martinez
 */
public class GraphsAdapter implements IAdapter {

	/**
	 * is a graphml file?
	 */
	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		File file = FileUtils.getFile(uri);
		if (file != null && file.exists() && !file.isDirectory() && FileUtils.isExtension(file, "graphml")) {
			return true;
		}
		return false;
	}

	/**
	 * Read the graph file
	 */
	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		List<IElement> elements = new ArrayList<IElement>();
		File file = FileUtils.getFile(uri);
		Graph graph = new TinkerGraph();
		GraphMLReader reader = new GraphMLReader(graph);

		InputStream is;
		try {
			is = new BufferedInputStream(new FileInputStream(file));
			reader.inputGraph(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		HashMap<Vertex,VertexElement> map = new HashMap<Vertex,VertexElement>();
		Iterable<Vertex> vertices = graph.getVertices();
		for (Vertex vertex : vertices) {
			VertexElement v = new VertexElement();
			v.setVertex(vertex);
			elements.add(v);
			map.put(vertex, v);
		}

		Iterable<Edge> edges = graph.getEdges();
		for (Edge edge : edges) {
			EdgeElement e = new EdgeElement();
			e.setEdge(edge);
			elements.add(e);
			// Add dependencies
			Vertex from = edge.getVertex(Direction.OUT);
			e.addDependency("from",map.get(from));
			Vertex to = edge.getVertex(Direction.IN);
			e.addDependency("to",map.get(to));
		}

		return elements;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		// TODO construct
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Construction",
						"For the moment, construction is not available for the Graphs adapter");
			}
		});
	}

}
