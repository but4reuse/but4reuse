package org.but4reuse.adapters.json.tools;

import java.util.Arrays;
import java.util.HashMap;

import org.but4reuse.adapters.json.activator.Activator;
import org.but4reuse.adapters.json.preferences.JsonAdapterPreferencePage;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

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

	private static Paths getPaths(String delimiter, String paths) {
		if (paths.compareTo("") == 0)
			return new Paths();

		if (delimiter.compareTo("") == 0)
			return new Paths(paths);

		return new Paths(Arrays.asList(paths.split(delimiter)));
	}

	public static Paths getAbsolutePathsToIgnore() {
		String delimiter = Activator.getDefault().getPreferenceStore().getString(JsonAdapterPreferencePage.DELIMITER);
		String paths = Activator.getDefault().getPreferenceStore()
				.getString(JsonAdapterPreferencePage.ABSOLUTE_PATHS_TO_IGNORE);

		return getPaths(delimiter, paths);
	}

	public static Paths getRelativePathsToIgnore() {
		String delimiter = Activator.getDefault().getPreferenceStore().getString(JsonAdapterPreferencePage.DELIMITER);
		String paths = Activator.getDefault().getPreferenceStore()
				.getString(JsonAdapterPreferencePage.RELATIVE_PATHS_TO_IGNORE);

		return getPaths(delimiter, paths);
	}

	public static Paths getAbsolutePathsUnsplittable() {
		String delimiter = Activator.getDefault().getPreferenceStore().getString(JsonAdapterPreferencePage.DELIMITER);
		String paths = Activator.getDefault().getPreferenceStore()
				.getString(JsonAdapterPreferencePage.ABSOLUTE_PATHS_UNSPLITTABLE);

		return getPaths(delimiter, paths);
	}

	public static Paths getRelativePathsUnsplittable() {
		String delimiter = Activator.getDefault().getPreferenceStore().getString(JsonAdapterPreferencePage.DELIMITER);
		String paths = Activator.getDefault().getPreferenceStore()
				.getString(JsonAdapterPreferencePage.RELATIVE_PATHS_UNSPLITTABLE);

		return getPaths(delimiter, paths);
	}

	public static JsonValue removePaths(JsonValue jsonValue, Paths paths, Paths absolute_paths_to_ignore,
			Paths relative_paths_to_ignore) {
		if (paths.matches(absolute_paths_to_ignore) || paths.matches(relative_paths_to_ignore))
			return JsonValue.NULL;

		if (jsonValue.isObject()) {
			paths = new Paths(paths);
			paths.extend("{}");
			paths.add("{}");

			if (paths.matches(absolute_paths_to_ignore) || paths.matches(relative_paths_to_ignore))
				return JsonValue.NULL;

			JsonObject jsonObject = new JsonObject();
			for (String name : jsonValue.asObject().names()) {
				Paths currentPaths = new Paths(paths);
				currentPaths.extend(name);
				currentPaths.add(name);
				jsonObject.set(
						name,
						removePaths(jsonValue.asObject().get(name), currentPaths, absolute_paths_to_ignore,
								relative_paths_to_ignore));
			}
			return jsonObject;
		}
		if (jsonValue.isArray()) {
			Paths currentPaths = new Paths(paths);
			currentPaths.extend("[]");
			currentPaths.add("[]");

			if (currentPaths.matches(absolute_paths_to_ignore) || currentPaths.matches(relative_paths_to_ignore))
				return JsonValue.NULL;

			JsonArray jsonArray = new JsonArray();
			for (int index = 0; index < jsonValue.asArray().size(); index++) {
				currentPaths = new Paths(paths);
				currentPaths.extend("[]", "[" + index + "]");
				currentPaths.add("[]");
				currentPaths.add("[" + index + "]");

				jsonArray.add(removePaths(jsonValue.asArray().get(index), currentPaths, absolute_paths_to_ignore,
						relative_paths_to_ignore));
			}
			return jsonArray;
		}
		return jsonValue;
	}
}
