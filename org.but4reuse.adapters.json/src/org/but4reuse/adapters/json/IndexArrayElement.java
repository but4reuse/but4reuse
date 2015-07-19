package org.but4reuse.adapters.json;

import java.util.ArrayList;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/*This class must not be considered as an atomic element like Array|Key|Object|Value elements.
 * Its job consists in splitting, in a flexible way, arrays' elements.
 * Using a set of IDs.
 */

/*
 * Before blocks creation, each IndexArrayElement has an unique id_elt.
 * After blocks creation, similar IndexArrayElements share the same unique id_elt.
 * id_elt is used to handle the merging of atomics elements in construct algorithm.
 */

public class IndexArrayElement extends AbstractElement implements IJsonElement {

	public int id_elt;
	public ArrayElement parent;

	public ArrayList<Integer> similarFiles; // list of each file where a similar
											// IndexArrayElement has been found.
	public ArrayList<IndexArrayElement> similarIndexes; // list of a similar
														// IndexArrayElements
														// already found.

	public IndexArrayElement(int id_file, int id_elt, ArrayElement parent) {
		this.id_elt = id_elt;
		this.parent = parent;

		this.similarFiles = new ArrayList<Integer>();
		this.similarFiles.add(id_file);
		this.similarIndexes = new ArrayList<IndexArrayElement>();
		this.similarIndexes.add(this);
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof IndexArrayElement) {
			IndexArrayElement elt = (IndexArrayElement) anotherElement;

			if (this.id_elt == elt.id_elt) {
				return 1;
			}

			for (int file : this.similarFiles) {
				if (elt.similarFiles.contains(file)) {
					return 0;
				}
			}

			for (int file : elt.similarFiles) {
				if (this.similarFiles.contains(file)) {
					return 0;
				}
			}

			if (this.parent.similarity(elt.parent) == 1) {

				ArrayList<Integer> files = new ArrayList<Integer>();
				ArrayList<IndexArrayElement> indexes = new ArrayList<IndexArrayElement>();

				files.addAll(this.similarFiles);
				files.addAll(elt.similarFiles);

				indexes.addAll(this.similarIndexes);
				indexes.addAll(elt.similarIndexes);

				for (IndexArrayElement indArrElt : indexes) {
					indArrElt.id_elt = this.id_elt;
					indArrElt.similarFiles = files;
					indArrElt.similarIndexes = indexes;
				}

				return 1;
			}
		}

		return 0;
	}

	@Override
	public int getMaxDependencies(String dependencyID) {
		return 1;
	}

	@Override
	public int getMinDependencies(String dependencyID) {
		return 1;
	}

	@Override
	public String getText() {
		return this.parent.getText() + "_[" + this.id_elt + "]";
	}

	@Override
	public JsonValue construct(JsonObject root, JsonValue value) {
		JsonArray array = this.parent.construct(root).asArray();
		int index = JsonTools.getIndexArray(this.parent.id_array, this.id_elt);

		if (index >= array.size()) {
			array.add(value);
			return value;
		} else {
			JsonValue lastValue = array.get(index);

			if (lastValue == null || lastValue == JsonValue.NULL) {
				array.set(index, value);
				return value;
			}
			if (value == null || value == JsonValue.NULL) {
				return lastValue;
			}
			if (lastValue.isObject() && value.isObject()) {
				return lastValue;
			}
			if (lastValue.isArray() && value.isArray()) {
				return lastValue;
			}

			array.set(index, value);
			return value;
		}
	}

	@Override
	public JsonValue construct(JsonObject root) {
		return this.construct(root, JsonValue.NULL);
	}

}
