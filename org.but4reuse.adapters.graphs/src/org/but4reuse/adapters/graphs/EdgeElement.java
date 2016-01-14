package org.but4reuse.adapters.graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.graphs.activator.Activator;
import org.but4reuse.adapters.graphs.preferences.GraphsAdapterPreferencePage;
import org.but4reuse.adapters.impl.AbstractElement;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;

/**
 * Edge Element
 * 
 * @author jabier.martinez
 */
public class EdgeElement extends AbstractElement {

	private Edge edge = null;

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof EdgeElement) {
			EdgeElement edgeElement = (EdgeElement) anotherElement;
			String id = Activator.getDefault().getPreferenceStore().getString(GraphsAdapterPreferencePage.EDGE_ID);
			if (id == null || id.isEmpty()) {
				if (edgeElement.getEdge().getId().equals(edge.getId())) {
					return 1;
				}
			} else {
				if (edgeElement.getEdge().getProperty(id) != null
						&& edgeElement.getEdge().getProperty(id).equals(edge.getProperty(id))) {
					return 1;
				}
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		String properties = "";
		Set<String> keys = edge.getPropertyKeys();
		for (String key : keys) {
			properties = properties + ", " + key + "=" + edge.getProperty(key);
		}
		return "E: id=" + edge.getId() + " fromTo=" + edge.getVertex(Direction.OUT).getId() + "->"
				+ edge.getVertex(Direction.IN).getId() + properties;
	}

	public Edge getEdge() {
		return edge;
	}

	public void setEdge(Edge edge) {
		this.edge = edge;
	}

	@Override
	public List<String> getWords() {
		List<String> words = new ArrayList<String>();
		String id = Activator.getDefault().getPreferenceStore().getString(GraphsAdapterPreferencePage.EDGE_ID);
		if (id == null || id.isEmpty()) {
			words.add(edge.getId().toString());
		} else {
			words.add(edge.getProperty(id).toString());
		}
		return words;
	}
}
