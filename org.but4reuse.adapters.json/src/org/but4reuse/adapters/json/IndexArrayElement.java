package org.but4reuse.adapters.json;

import java.util.ArrayList;
import java.util.HashMap;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/*This class must not be considered as an atomic element like Array|Key|Object|Value elements.
 * Its job consists in splitting, in a flexible way, arrays' elements.
 * Using a set of IDs.
 */

public class IndexArrayElement extends AbstractElement implements IJsonElement {
	
	public int id_file;
	public int id_elt;
	public IJsonElement parent;
	
	public ArrayList<Integer> filesCompared;
	public HashMap<Integer, IndexArrayElement> elementsCompared;
	
	public IndexArrayElement(int id_file, int id_elt, IJsonElement parent) {
		this.id_file = id_file;
		this.id_elt = id_elt;
		this.parent = parent;
		
		this.filesCompared = new ArrayList<Integer>();
		this.elementsCompared = new HashMap<Integer, IndexArrayElement>();
	}
	
	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof IndexArrayElement) {
			IndexArrayElement elt = (IndexArrayElement) anotherElement;
			
			if (this.similarity(elt, new ArrayList<String>()) == 1) {
				this.filesCompared.add(elt.id_file);
				this.elementsCompared.put(elt.id_elt, elt);
				
				elt.filesCompared.add(this.id_file);
				elt.elementsCompared.put(this.id_elt, this);
				
				return 1;
			}
		}
		
		return 0;
	}
	
	private double similarity(IndexArrayElement elt, ArrayList<String> excluded) {
		if (excluded.contains(this.id_elt + "_" + elt.id_elt) || excluded.contains(elt.id_elt + "_" + this.id_elt)) {
			return 1;
		}
		
		if (this.id_file == elt.id_file) {
			if (this.id_elt == elt.id_elt) {
				return 1;
			} else {
				return 0;
			}
		}
		
		if (this.elementsCompared.containsKey(elt.id_elt) || elt.elementsCompared.containsKey(this.id_elt)) {
			return 1;
		}
		
		if (this.filesCompared.contains(elt.id_file) || elt.filesCompared.contains(this.id_file)) {
			return 0;
		}
		
		if (this.parent.similarity(elt.parent) == 0) {
			return 0;
		}
		
		excluded.add(this.id_elt + "_" + elt.id_elt);
		
		for (int id : this.elementsCompared.keySet()) {
			if (elt.similarity(this.elementsCompared.get(id), excluded) == 0) {
				return 0;
			}
		}
		
		for (int id : elt.elementsCompared.keySet()) {
			if (this.similarity(elt.elementsCompared.get(id), excluded) == 0) {
				return 0;
			}
		}
		
		return 1;
	}
	
	@Override
	public String getText() {
		return this.parent.getText();
	}

	@Override
	public JsonValue construct(JsonObject root, JsonValue value) {
		return null;
	}

}
