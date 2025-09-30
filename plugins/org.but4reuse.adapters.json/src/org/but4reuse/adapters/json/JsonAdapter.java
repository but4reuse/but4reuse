package org.but4reuse.adapters.json;

import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.json.activator.Activator;
import org.but4reuse.adapters.json.preferences.JsonAdapterPreferencePage;
import org.but4reuse.adapters.json.tools.AdapterTools;
import org.but4reuse.adapters.json.tools.JsonConstruct;
import org.but4reuse.adapters.json.tools.JsonElement;
import org.but4reuse.adapters.json.tools.Paths;
import org.but4reuse.adapters.json.tools.PathsTools;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;

/**
 * Adapter for JSON
 */
public class JsonAdapter implements IAdapter {

	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		File file = FileUtils.getFile(uri);
		if (file != null && file.exists() && !file.isDirectory() && FileUtils.isExtension(file, "json")) {
			return true;
		}
		return false;
	}

	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		boolean onlyValueElements = Activator.getDefault().getPreferenceStore()
				.getBoolean(JsonAdapterPreferencePage.ONLY_VALUES);
		Paths pathsToInclude = PathsTools.getPathsToInclude();
		Paths pathsToOmit = PathsTools.getPathsToOmit();
		Paths pathsToIgnore = PathsTools.getPathsToIgnore();
		Paths pathsUnsplittable = PathsTools.getPathsUnsplittable();

		return adapt(uri, monitor, onlyValueElements, pathsToInclude, pathsToOmit, pathsToIgnore, pathsUnsplittable);
	}

	/**
	 * This is intended to be overriden in adapters that specialize the json adapter
	 * by providing a predefined set of paths to ignore etc.
	 * 
	 * @param uri
	 * @param monitor
	 * @param pathsToInclude    (only keys in the root)
	 * @param pathsToOmit
	 * @param pathsToIgnore
	 * @param pathsUnsplittable
	 * @return the list of elements
	 */
	public List<IElement> adapt(URI uri, IProgressMonitor monitor, boolean onlyValueElements, Paths pathsToInclude,
			Paths pathsToOmit, Paths pathsToIgnore, Paths pathsUnsplittable) {

		// the list that will be returned
		List<IElement> atomicJsonElementList = new ArrayList<IElement>();

		try {
			// Load json file
			File file = FileUtils.getFile(uri);
			JsonValue root = Json.parse(new FileReader(file));
			atomicJsonElementList = adapt(root, monitor, onlyValueElements, pathsToInclude, pathsToOmit, pathsToIgnore,
					pathsUnsplittable);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return atomicJsonElementList;
	}

	/**
	 * Adapt
	 * 
	 * @param root
	 * @param monitor
	 * @param pathsToInclude
	 * @param pathsToOmit
	 * @param pathsToIgnore
	 * @param pathsUnsplittable
	 * @return
	 */
	public List<IElement> adapt(JsonValue root, IProgressMonitor monitor, boolean onlyValueElements,
			Paths pathsToInclude, Paths pathsToOmit, Paths pathsToIgnore, Paths pathsUnsplittable) {
		int id_file = AdapterTools.getUniqueId();

		// the list that will be returned
		List<IElement> elements = new ArrayList<IElement>();

		// this is a queue to iteratively feed the atomic json element list
		List<JsonElement> elementQueue = new ArrayList<JsonElement>();

		boolean first = true;

		// while the queue is empty (or the root)
		while (first || !elementQueue.isEmpty()) {

			Paths paths;
			JsonValue jsonValue;
			IElement parent;
			IElement dependency;

			if (elementQueue.isEmpty()) {
				first = false;
				paths = new Paths();
				jsonValue = root;
				parent = null;
				dependency = null;
			} else {
				JsonElement jsonElement = elementQueue.remove(0);
				paths = jsonElement.paths;
				jsonValue = jsonElement.jsonValue;
				parent = jsonElement.parent;
				dependency = jsonElement.dependency;
			}

			// paths to omit content
			if (paths.matches(pathsToOmit)) {
				continue;
			}

			// paths to ignore content
			if (paths.matches(pathsToIgnore)) {
				IgnoredElement ignoredElement = new IgnoredElement(jsonValue, parent);
				ignoredElement.addDependency(dependency);
				elements.add(ignoredElement);
				continue;
			}

			// paths unsplittable
			if (paths.matches(pathsUnsplittable)) {
				JsonValue compare = PathsTools.removePaths(jsonValue, paths, pathsToIgnore);
				UnsplittableElement unsplittableElement = new UnsplittableElement(jsonValue, compare, parent);
				unsplittableElement.addDependency(dependency);
				elements.add(unsplittableElement);
				continue;
			}

			// Object { key : value , key2 : value2 }
			if (jsonValue.isObject()) {
				Paths currentPaths = new Paths(paths);
				currentPaths.extend("{}");

				if (currentPaths.matches(pathsToOmit)) {
					continue;
				}

				if (currentPaths.matches(pathsToIgnore)) {
					IgnoredElement ignoredElement = new IgnoredElement(jsonValue, parent);
					ignoredElement.addDependency(dependency);
					elements.add(ignoredElement);
					continue;
				}
				if (currentPaths.matches(pathsUnsplittable)) {
					JsonValue compare = PathsTools.removePaths(jsonValue, currentPaths, pathsToIgnore);
					UnsplittableElement unsplittableElement = new UnsplittableElement(jsonValue, compare, parent);
					unsplittableElement.addDependency(dependency);
					elements.add(unsplittableElement);
					continue;
				}

				JsonObject jsonObject = jsonValue.asObject();
				ObjectElement objectElement = new ObjectElement(parent);
				objectElement.addDependency(dependency);
				elements.add(objectElement);

				// Keys
				for (String name : jsonObject.names()) {
					currentPaths = new Paths(paths);
					currentPaths.extend(name);

					// check if we are in the root and if the key is not included
					if (!pathsToInclude.absolutePaths.isEmpty() && objectElement.parent == null
							&& !pathsToInclude.absolutePaths.contains(name)) {
						continue;
					}

					if (currentPaths.matches(pathsToOmit)) {
						continue;
					}

					KeyElement keyElement = new KeyElement(name, objectElement);
					keyElement.addDependency(objectElement);
					elements.add(keyElement);

					elementQueue.add(new JsonElement(currentPaths, jsonObject.get(name), keyElement, keyElement));
				}

				// Array [ ]
			} else if (jsonValue.isArray()) {
				Paths currentPaths = new Paths(paths);
				currentPaths.extend("[]");

				// paths to omit content
				if (currentPaths.matches(pathsToOmit)) {
					continue;
				}

				if (currentPaths.matches(pathsToIgnore)) {
					IgnoredElement ignoredElement = new IgnoredElement(jsonValue, parent);
					ignoredElement.addDependency(dependency);
					elements.add(ignoredElement);
					continue;
				}
				if (currentPaths.matches(pathsUnsplittable)) {
					JsonValue compare = PathsTools.removePaths(jsonValue, currentPaths, pathsToIgnore);
					UnsplittableElement unsplittableElement = new UnsplittableElement(jsonValue, compare, parent);
					unsplittableElement.addDependency(dependency);
					elements.add(unsplittableElement);
					continue;
				}

				JsonArray jsonArray = jsonValue.asArray();
				ArrayElement arrayElement = new ArrayElement(parent);
				arrayElement.addDependency(dependency);
				elements.add(arrayElement);

				List<IndexArrayElement> indexesAhead = new ArrayList<IndexArrayElement>();

				for (int i = 0; i < jsonArray.size(); i++) {
					currentPaths = new Paths(paths);
					currentPaths.extend("[]", "[" + i + "]");

					IndexArrayElement indexArrayElement = new IndexArrayElement(id_file, arrayElement, indexesAhead);
					elementQueue.add(new JsonElement(currentPaths, jsonArray.get(i), indexArrayElement, arrayElement));

					indexesAhead.add(indexArrayElement);
				}

				// Value (for string, number, true, false, null)
			} else {
				ValueElement valueElement = new ValueElement(parent, jsonValue);
				valueElement.addDependency(dependency);
				elements.add(valueElement);
			}
		}

		// check if we should keep only value elements
		if (onlyValueElements) {
			List<IElement> toRemove = new ArrayList<IElement>();
			for (IElement e : elements) {
				if (!(e instanceof ValueElement)) {
					toRemove.add(e);
				}
			}
			elements.removeAll(toRemove);
		}

		return elements;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		try {
			// use default name if folder was provided
			if (uri.toString().endsWith("/")) {
				uri = new URI(uri.toString() + "jsonConstruction.json");
			}

			File file = FileUtils.getFile(uri);
			FileUtils.createFile(file);

			// Construct
			JsonObject root = construct(elements, monitor);

			// Save to file and pretty print
			FileUtils.writeFile(file, root.toString(WriterConfig.PRETTY_PRINT));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JsonObject construct(List<IElement> elements, IProgressMonitor monitor) {
		JsonConstruct construct = new JsonConstruct();
		for (IElement elt : elements) {
			construct.construct(elt);
		}
		return construct.root;
	}

}
