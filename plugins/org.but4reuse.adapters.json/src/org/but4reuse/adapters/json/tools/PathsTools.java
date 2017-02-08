package org.but4reuse.adapters.json.tools;

import java.util.Arrays;

import org.but4reuse.adapters.json.activator.Activator;
import org.but4reuse.adapters.json.preferences.JsonAdapterPreferencePage;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class PathsTools {
	public static Paths getPathsToIgnore() {
		String delimiter = Activator.getDefault().getPreferenceStore().getString(JsonAdapterPreferencePage.DELIMITER);
		String absolutePaths = Activator.getDefault().getPreferenceStore()
				.getString(JsonAdapterPreferencePage.ABSOLUTE_PATHS_TO_IGNORE);
		String relativePaths = Activator.getDefault().getPreferenceStore()
				.getString(JsonAdapterPreferencePage.RELATIVE_PATHS_TO_IGNORE);

		Paths paths = new Paths();

		if (absolutePaths.length() > 0)
			paths.addAbsolutePaths(Arrays.asList(absolutePaths.split(delimiter)));
		if (relativePaths.length() > 0)
			paths.addRelativePaths(Arrays.asList(relativePaths.split(delimiter)));

		return paths;
	}

	public static Paths getPathsUnsplittable() {
		String delimiter = Activator.getDefault().getPreferenceStore().getString(JsonAdapterPreferencePage.DELIMITER);
		String absolutePaths = Activator.getDefault().getPreferenceStore()
				.getString(JsonAdapterPreferencePage.ABSOLUTE_PATHS_UNSPLITTABLE);
		String relativePaths = Activator.getDefault().getPreferenceStore()
				.getString(JsonAdapterPreferencePage.RELATIVE_PATHS_UNSPLITTABLE);

		Paths paths = new Paths();

		if (absolutePaths.length() > 0)
			paths.addAbsolutePaths(Arrays.asList(absolutePaths.split(delimiter)));
		if (relativePaths.length() > 0)
			paths.addRelativePaths(Arrays.asList(relativePaths.split(delimiter)));

		return paths;
	}

	public static JsonValue removePaths(JsonValue jsonValue, Paths paths, Paths pathsToIgnore) {
		if (paths.matches(pathsToIgnore))
			return JsonValue.NULL;

		if (jsonValue.isObject()) {
			paths = new Paths(paths);
			paths.extend("{}");

			if (paths.matches(pathsToIgnore))
				return JsonValue.NULL;

			JsonObject jsonObject = new JsonObject();
			for (String name : jsonValue.asObject().names()) {
				Paths currentPaths = new Paths(paths);
				currentPaths.extend(name);
				jsonObject.set(name, removePaths(jsonValue.asObject().get(name), currentPaths, pathsToIgnore));
			}
			return jsonObject;
		}
		if (jsonValue.isArray()) {
			Paths currentPaths = new Paths(paths);
			currentPaths.extend("[]");

			if (currentPaths.matches(pathsToIgnore))
				return JsonValue.NULL;

			JsonArray jsonArray = new JsonArray();
			for (int index = 0; index < jsonValue.asArray().size(); index++) {
				currentPaths = new Paths(paths);
				currentPaths.extend("[]", "[" + index + "]");

				jsonArray.add(removePaths(jsonValue.asArray().get(index), currentPaths, pathsToIgnore));
			}
			return jsonArray;
		}
		return jsonValue;
	}
}
