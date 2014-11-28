package org.but4reuse.adapters.graphs;

import java.util.Set;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

import com.tinkerpop.blueprints.Vertex;

/**
 * Vertex Element
 * @author jabier.martinez
 */
public class VertexElement extends AbstractElement {

	private Vertex vertex = null;
	
	@Override
	public double similarity(IElement anotherElement) {
		if(anotherElement instanceof VertexElement){
			VertexElement vertexElement = (VertexElement) anotherElement;
			if(vertexElement.getVertex().getId().equals(vertex.getId())){
				return 1;
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		String properties = "";
		Set<String> keys = vertex.getPropertyKeys();
		for(String key : keys){
			properties = properties + ", " + key + "=" + vertex.getProperty(key);
		}
		return "V: id=" + vertex.getId() + properties;
	}

	public Vertex getVertex() {
		return vertex;
	}

	public void setVertex(Vertex vertex) {
		this.vertex = vertex;
	}

}
