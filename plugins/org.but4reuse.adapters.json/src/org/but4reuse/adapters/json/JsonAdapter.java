package org.but4reuse.adapters.json;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.json.tools.AdapterTools;
import org.but4reuse.adapters.json.tools.JsonConstruct;
import org.but4reuse.adapters.json.tools.JsonElement;
import org.but4reuse.adapters.json.tools.Paths;
import org.but4reuse.adapters.json.tools.PathsTools;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;

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
		Paths pathsToIgnore = PathsTools.getPathsToIgnore();
		Paths pathsUnsplittable = PathsTools.getPathsUnsplittable();

		return adapt(uri, monitor, pathsToIgnore, pathsUnsplittable);
	}

	/**
	 * This is intended to be overriden in adapters that specialize the json
	 * adapter by providing a predefined set of paths to ignore etc.
	 * 
	 * @param uri
	 * @param monitor
	 * @param pathsToIgnore
	 * @param pathsUnsplittable
	 * @return the list of elements
	 */
	public List<IElement> adapt(URI uri, IProgressMonitor monitor, Paths pathsToIgnore, Paths pathsUnsplittable) {
		int id_file = AdapterTools.getUniqueId();

		// the list that will be returned
		List<IElement> atomicJsonElementList = new ArrayList<IElement>();

		// this is a queue to iteratively feed the atomic json element list
		List<JsonElement> jsonElementQueue = new ArrayList<JsonElement>();

		try {
			// Load json file
			File file = FileUtils.getFile(uri);
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			JsonObject root = JsonObject.readFrom(br);

			// Add root
			ObjectElement rootElement = new ObjectElement(null);
			atomicJsonElementList.add(rootElement);

			// Initialize the queue with the root's children
			for (String name : root.names()) {
				KeyElement keyElement = new KeyElement(name, rootElement);
				keyElement.addDependency(rootElement);
				atomicJsonElementList.add(0, keyElement);
				jsonElementQueue.add(new JsonElement(new Paths(name), root.get(name), keyElement, keyElement));
			}

			// while the queue is empty
			while (!jsonElementQueue.isEmpty()) {
				JsonElement jsonElement = jsonElementQueue.remove(0);
				Paths paths = jsonElement.paths;
				JsonValue jsonValue = jsonElement.jsonValue;
				IElement parent = jsonElement.parent;
				IElement dependency = jsonElement.dependency;

				// paths to ignore
				if (paths.matches(pathsToIgnore)) {
					IgnoredElement ignoredElement = new IgnoredElement(jsonValue, parent);
					ignoredElement.addDependency(dependency);
					atomicJsonElementList.add(0, ignoredElement);
					continue;
				}

				// paths unsplittable
				if (paths.matches(pathsUnsplittable)) {
					JsonValue compare = PathsTools.removePaths(jsonValue, paths, pathsToIgnore);
					UnsplittableElement unsplittableElement = new UnsplittableElement(jsonValue, compare, parent);
					unsplittableElement.addDependency(dependency);
					atomicJsonElementList.add(0, unsplittableElement);
					continue;
				}

				// Object
				if (jsonValue.isObject()) {
					Paths currentPaths = new Paths(paths);
					currentPaths.extend("{}");

					if (currentPaths.matches(pathsToIgnore)) {
						IgnoredElement ignoredElement = new IgnoredElement(jsonValue, parent);
						ignoredElement.addDependency(dependency);
						atomicJsonElementList.add(0, ignoredElement);
						continue;
					}
					if (currentPaths.matches(pathsUnsplittable)) {
						JsonValue compare = PathsTools.removePaths(jsonValue, currentPaths, pathsToIgnore);
						UnsplittableElement unsplittableElement = new UnsplittableElement(jsonValue, compare, parent);
						unsplittableElement.addDependency(dependency);
						atomicJsonElementList.add(0, unsplittableElement);
						continue;
					}

					JsonObject jsonObject = jsonValue.asObject();
					ObjectElement objectElement = new ObjectElement(parent);
					objectElement.addDependency(dependency);
					atomicJsonElementList.add(0, objectElement);

					for (String name : jsonObject.names()) {
						currentPaths = new Paths(paths);
						currentPaths.extend(name);

						KeyElement keyElement = new KeyElement(name, objectElement);
						keyElement.addDependency(objectElement);
						atomicJsonElementList.add(0, keyElement);

						jsonElementQueue
								.add(new JsonElement(currentPaths, jsonObject.get(name), keyElement, keyElement));
					}

					// Array
				} else if (jsonValue.isArray()) {
					Paths currentPaths = new Paths(paths);
					currentPaths.extend("[]");

					if (currentPaths.matches(pathsToIgnore)) {
						IgnoredElement ignoredElement = new IgnoredElement(jsonValue, parent);
						ignoredElement.addDependency(dependency);
						atomicJsonElementList.add(0, ignoredElement);
						continue;
					}
					if (currentPaths.matches(pathsUnsplittable)) {
						JsonValue compare = PathsTools.removePaths(jsonValue, currentPaths, pathsToIgnore);
						UnsplittableElement unsplittableElement = new UnsplittableElement(jsonValue, compare, parent);
						unsplittableElement.addDependency(dependency);
						atomicJsonElementList.add(0, unsplittableElement);
						continue;
					}

					JsonArray jsonArray = jsonValue.asArray();
					ArrayElement arrayElement = new ArrayElement(parent);
					arrayElement.addDependency(dependency);
					atomicJsonElementList.add(0, arrayElement);

					List<IndexArrayElement> indexesAhead = new ArrayList<IndexArrayElement>();

					for (int index = 0; index < jsonArray.size(); index++) {
						currentPaths = new Paths(paths);
						currentPaths.extend("[]", "[" + index + "]");

						IndexArrayElement indexArrayElement = new IndexArrayElement(id_file, arrayElement, indexesAhead);
						jsonElementQueue.add(new JsonElement(currentPaths, jsonArray.get(index), indexArrayElement,
								arrayElement));

						indexesAhead.add(indexArrayElement);
					}

					// Value
				} else {
					ValueElement valueElement = new ValueElement(parent, jsonValue);
					valueElement.addDependency(dependency);
					atomicJsonElementList.add(0, valueElement);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return atomicJsonElementList;
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
			FileUtils.appendToFile(file, root.toString(WriterConfig.PRETTY_PRINT));
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
