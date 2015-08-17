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
		/*
		 * System.out.println(this.jsonArray.toString());
		 * System.out.println(this.indexes.toString()); for(IndexArrayElement in
		 * : indexesAhead) System.out.print(in.id + " "); System.out.println();
		 * System.out.println(ids.toString());
		 * System.out.println(jsonValue.toString()); System.out.println();
		 * 
		 * /* jsonArray.add(jsonValue); this.indexes.add(ids);
		 * System.out.println(this.jsonArray.toString());
		 * System.out.println(this.indexes.toString()); System.out.println();
		 * for(IndexArrayElement in : indexesAhead) { for(int ind :
		 * in.similarIds) System.out.print(ind + " "); System.out.println(); }
		 * System.out.println();
		 */}
}
