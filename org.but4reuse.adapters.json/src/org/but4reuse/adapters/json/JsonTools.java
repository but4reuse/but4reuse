package org.but4reuse.adapters.json;

import java.util.HashMap;

public class JsonTools {

	private static int id = 0;
	private static HashMap<Integer, HashMap<Integer, Integer>> map = new HashMap<Integer, HashMap<Integer, Integer>>();

	public static int generateId() {
		id++;
		return id;
	}

	public static int getIndexArray(int id_array, int id_elt) {
		if (!JsonTools.map.containsKey(id_array)) {
			JsonTools.map.put(id_array, new HashMap<Integer, Integer>());
		}

		HashMap<Integer, Integer> m = JsonTools.map.get(id_array);

		if (!m.containsKey(id_elt)) {
			m.put(id_elt, m.size());
		}

		return m.get(id_elt);
	}
}
