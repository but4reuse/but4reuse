package org.but4reuse.adapters.json.tools;

import java.util.HashMap;

public class AdapterTools {
	private static int id = 0;
	private static HashMap<Integer, HashMap<Integer, Integer>> indexes = new HashMap<Integer, HashMap<Integer, Integer>>();

	public static int getUniqueId() {
		return id++;
	}

	public static void reset() {
		indexes = new HashMap<Integer, HashMap<Integer, Integer>>();
	}

	public static int getIndexArray(int id_array, int id_element) {
		if (!indexes.containsKey(id_array)) {
			indexes.put(id_array, new HashMap<Integer, Integer>());
		}

		HashMap<Integer, Integer> specificIndexes = indexes.get(id_array);

		if (!specificIndexes.containsKey(id_element)) {
			specificIndexes.put(id_element, specificIndexes.size());
		}

		return specificIndexes.get(id_element);
	}
}
