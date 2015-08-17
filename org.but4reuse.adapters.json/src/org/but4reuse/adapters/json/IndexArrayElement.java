package org.but4reuse.adapters.json;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.json.tools.AdapterTools;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class IndexArrayElement extends AbstractJsonElement {
	public int id_file;
	public ArrayElement parent;
	public int id;
	public List<Integer> similarFiles;
	public List<IndexArrayElement> similarIndexes;
	public List<Integer> similarIds;
	public List<IndexArrayElement> indexesAhead;

	public IndexArrayElement(int id_file, ArrayElement parent,
			List<IndexArrayElement> ahead) {
		this.id_file = id_file;
		this.parent = parent;
		this.id = AdapterTools.getUniqueId();
		this.similarFiles = new ArrayList<Integer>();
		this.similarIndexes = new ArrayList<IndexArrayElement>();
		this.similarFiles.add(id_file);
		this.similarIndexes.add(this);
		this.similarIds = new ArrayList<Integer>();
		this.similarIds.add(this.id);
		this.indexesAhead = new ArrayList<IndexArrayElement>();
		for (IndexArrayElement index : ahead)
			this.indexesAhead.add(index);
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof IndexArrayElement) {
			IndexArrayElement elt = (IndexArrayElement) anotherElement;
			if (this.similarIds.contains(elt.id)
					|| elt.similarIds.contains(this.id))
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
				ArrayList<Integer> simIds = new ArrayList<Integer>();
				ArrayList<IndexArrayElement> ahead = new ArrayList<IndexArrayElement>();

				files.addAll(this.similarFiles);
				files.addAll(elt.similarFiles);

				indexes.addAll(this.similarIndexes);
				indexes.addAll(elt.similarIndexes);

				simIds.addAll(this.similarIds);
				simIds.addAll(elt.similarIds);

				ahead.addAll(this.indexesAhead);
				ahead.addAll(elt.indexesAhead);

				for (IndexArrayElement indArrElt : indexes) {
					indArrElt.similarFiles = files;
					indArrElt.similarIndexes = indexes;
					indArrElt.similarIds = simIds;
					indArrElt.indexesAhead = ahead;
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
		this.parent.construct(root);
		this.parent.array.add(this.indexesAhead, this.similarIds, jsonValue);
		return jsonValue;
	}

}
