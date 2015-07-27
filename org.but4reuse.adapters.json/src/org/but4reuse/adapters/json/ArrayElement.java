package org.but4reuse.adapters.json;

import java.util.ArrayList;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/*
 * Before blocks creation, each id_array is unique.
 * After  blocks creation, similar ArrayElements share the same unique id_array. 
 * Then, the id_array is used in the construct algorithm to handle with indexes.
 */

public class ArrayElement extends AbstractElement implements IJsonElement {

	public int id_array;
	public IJsonElement parent;
	public JsonArray content;

	public ArrayList<ArrayElement> similarArrays; // List of similar arrays
													// already found. So the
													// change of id_array can
													// properly be done after a
													// right similarity.

	public ArrayElement(int id_array, IJsonElement parent) {
		this.id_array = id_array;
		this.parent = parent;
		this.content = null;

		this.similarArrays = new ArrayList<ArrayElement>();
		this.similarArrays.add(this);
	}

	public ArrayElement(int id_array, IJsonElement parent, JsonArray content) {
		this.id_array = id_array;
		this.parent = parent;
		this.content = content;

		this.similarArrays = new ArrayList<ArrayElement>();
		this.similarArrays.add(this);
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof ArrayElement) {
			ArrayElement elt = (ArrayElement) anotherElement;

			if (this.id_array == elt.id_array) {
				return 1;
			}

			if (this.content == null) {
				if (elt.content == null) {
					if (this.parent.similarity(elt.parent) == 1) {

						ArrayList<ArrayElement> arrays = new ArrayList<ArrayElement>();

						arrays.addAll(this.similarArrays);
						arrays.addAll(elt.similarArrays);

						for (ArrayElement arrElt : arrays) {
							arrElt.id_array = this.id_array;
							arrElt.similarArrays = arrays;
						}

						return 1;
					}
				}
			} else if (elt.content != null && this.content.equals(elt.content)) {
				if (this.parent.similarity(elt.parent) == 1) {

					ArrayList<ArrayElement> arrays = new ArrayList<ArrayElement>();

					arrays.addAll(this.similarArrays);
					arrays.addAll(elt.similarArrays);

					for (ArrayElement arrElt : arrays) {
						arrElt.id_array = this.id_array;
						arrElt.similarArrays = arrays;
					}

					return 1;
				}
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		if (this.content == null) {
			return this.parent.getText() + "_[]";
		} else {
			return this.parent.getText() + "_[]_" + this.content.toString();
		}
	}

	@Override
	public JsonValue construct(JsonObject root, JsonValue value) {
		return this.parent.construct(root, value);
	}

	@Override
	public JsonValue construct(JsonObject root) {
		if (this.content == null) {
			return this.parent.construct(root, new JsonArray());
		} else {
			return this.parent.construct(root, this.content);
		}
	}
}
