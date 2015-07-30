package org.but4reuse.adapters.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.json.activator.Activator;

public class JsonTools {

	private static int id = 0;
	private static HashMap<Integer, HashMap<Integer, Integer>> map = new HashMap<Integer, HashMap<Integer, Integer>>();
	public static ArrayList<String> pathsToIgnore = new ArrayList<String>();

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

	public static void addPathToIgnore(String path) {
		if (!pathsToIgnore.contains(path)) {
			pathsToIgnore.add(path);
		}
	}

	public static void setPathsToIgnore() {
		String delimiter = Activator.getDefault().getPreferenceStore().getString("DELIMITER");
		String paths = Activator.getDefault().getPreferenceStore().getString("PATHS");
		
		if (delimiter.compareTo("") != 0) {
			for (String path : Arrays.asList(paths.split(delimiter))) {
				addPathToIgnore(path);
			}
		} else {
			addPathToIgnore(paths);
		}
	}

	public static ArrayList<String> matchPossiblePaths(ArrayList<String> paths) {
		ArrayList<String> possiblePaths = new ArrayList<String>();

		for (String path : paths) {
			Pattern pattern = Pattern.compile("^" + Pattern.quote(path));

			for (String pathToIgnore : pathsToIgnore) {
				Matcher matcher = pattern.matcher(pathToIgnore);
				if (matcher.find()) {
					possiblePaths.add(path);
					break;
				}
			}
		}

		return possiblePaths;
	}

	public static boolean containIgnoredPath(ArrayList<String> paths) {
		for (String path : paths) {
			for (String pathToIgnore : pathsToIgnore) {
				if (path.compareTo(pathToIgnore) == 0) {
					return true;
				}
			}
		}
		return false;
	}

	public static ArrayList<String> addToPaths(ArrayList<String> paths, String elt) {
		ArrayList<String> newPaths = new ArrayList<String>();
		for (String path : paths) {
			newPaths.add(path + "_" + elt);
		}
		return newPaths;
	}

	public static void addElement(Map<Integer, List<IElement>> elements, IElement element, int depth) {
		if (!elements.containsKey(depth)) {
			elements.put(depth, new ArrayList<IElement>());
		}

		elements.get(depth).add(element);
	}
}
