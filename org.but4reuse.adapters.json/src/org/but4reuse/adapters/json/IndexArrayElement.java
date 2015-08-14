package org.but4reuse.adapters.json;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.json.tools.AdapterTools;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class IndexArrayElement extends AbstractJsonElement {
	public int id_file;
	public ArrayElement parent;
	public int id;
	public List<Integer> similarFiles;
	public List<IndexArrayElement> similarIndexes;

	public IndexArrayElement(int id_file, ArrayElement parent) {
		this.id_file = id_file;
		this.parent = parent;
		this.id = AdapterTools.getUniqueId();
		this.similarFiles = new ArrayList<Integer>();
		this.similarIndexes = new ArrayList<IndexArrayElement>();
		this.similarFiles.add(id_file);
		this.similarIndexes.add(this);
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof IndexArrayElement) {
			IndexArrayElement elt = (IndexArrayElement) anotherElement;
			if (this.id == elt.id)
				return 1;

			for (int file : this.similarFiles)
				if (elt.similarFiles.contains(file))
					return 0;

			for (int file : elt.similarFiles)
				if (this.similarFiles.contains(file))
					return 0;

			if (this.parent.similarity(elt.parent) == 1) {
				ArrayList<Integer> files = new ArrayList<Integer>();
				ArrayList<IndexArrayElement> indexes = new ArrayList<IndexArrayElement>();

				files.addAll(this.similarFiles);
				files.addAll(elt.similarFiles);

				indexes.addAll(this.similarIndexes);
				indexes.addAll(elt.similarIndexes);

				for (IndexArrayElement indArrElt : indexes) {
					indArrElt.id = this.id;
					indArrElt.similarFiles = files;
					indArrElt.similarIndexes = indexes;
				}
				return 1;
			}
		}

		return 0;
	}

	@Override
	public String getText() {
		return parent.getText();
	}

	@Override
	public JsonValue construct(JsonObject root) {
		return this.construct(root, JsonValue.NULL);
	}

	@Override
	public JsonValue construct(JsonObject root, JsonValue jsonValue) {
		JsonArray array = this.parent.construct(root).asArray();
		int index = AdapterTools.getIndexArray(this.parent.id, this.id);

		if (index >= array.size()) {
			array.add(jsonValue);
			return jsonValue;
		} else {
			JsonValue lastValue = array.get(index);

			if (lastValue == null || lastValue == JsonValue.NULL) {
				array.set(index, jsonValue);
				return jsonValue;
			}
			if (jsonValue == JsonValue.NULL)
				return lastValue;
			if (lastValue.isObject() && jsonValue.isObject())
				return lastValue;
			if (lastValue.isArray() && jsonValue.isArray())
				return lastValue;

			array.set(index, jsonValue);
			return jsonValue;
		}
	}

}
