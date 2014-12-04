package org.but4reuse.adapters.graphs;

import java.util.Set;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

import com.tinkerpop.blueprints.Edge;

/**
 * Edge Element
 * @author jabier.martinez
 */
public class EdgeElement extends AbstractElement {

	private Edge edge = null;
	
	@Override
	public double similarity(IElement anotherElement) {
		if(anotherElement instanceof EdgeElement){
			EdgeElement edgeElement = (EdgeElement) anotherElement;
			if(edgeElement.getEdge().getId().equals(edge.getId())){
				return 1;
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		String properties = "";
		Set<String> keys = edge.getPropertyKeys();
		for(String key : keys){
			properties = properties + ", " + key + "=" + edge.getProperty(key);
		}
		return "E: id=" + edge.getId() + properties;
	}

	public Edge getEdge() {
		return edge;
	}

	public void setEdge(Edge edge) {
		this.edge = edge;
	}

}
