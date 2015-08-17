package org.but4reuse.adapters.json.tools;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.json.IndexArrayElement;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

public class ArrayManager {
	public JsonArray jsonArray;
	private List<List<Integer>> indexes;

	public ArrayManager() {
		this.jsonArray = new JsonArray();
		this.indexes = new ArrayList<List<Integer>>();
	}

	public void add(List<IndexArrayElement> indexesAhead, List<Integer> ids,
			JsonValue jsonValue) {
		int index = 0;
		for (int i = 0; i < this.indexes.size(); i++) {
			List<Integer> currentIndexes = this.indexes.get(i);
			for (IndexArrayElement currentIndex : indexesAhead)
				for (int id : currentIndex.similarIds)
					if (currentIndexes.contains(id))
						index = i + 1;
		}
		this.indexes.add(index, ids);
		JsonValue value = jsonValue;
		for (; index < this.jsonArray.size(); index++) {
			JsonValue tmp = jsonArray.get(index);
			jsonArray.set(index, value);
			value = tmp;
		}
		this.jsonArray.add(value);
}
}
