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
import org.but4reuse.adapters.json.tools.JsonElement;
import org.but4reuse.adapters.json.tools.Paths;
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
		int id_file = AdapterTools.getUniqueId();

		Paths absolute_paths_to_ignore = AdapterTools.getAbsolutePathsToIgnore();
		Paths relative_paths_to_ignore = AdapterTools.getRelativePathsToIgnore();
		Paths absolute_paths_unsplittable = AdapterTools.getAbsolutePathsUnsplittable();
		Paths relative_paths_unsplittable = AdapterTools.getRelativePathsUnsplittable();

		List<IElement> atomicJsonElementList = new ArrayList<IElement>();
		List<JsonElement> jsonElementList = new ArrayList<JsonElement>();

		try {
			File file = FileUtils.getFile(uri);
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);

			JsonObject root = JsonObject.readFrom(br);
			for (String name : root.names()) {
				KeyElement keyElement = new KeyElement(name, null);
				atomicJsonElementList.add(0, keyElement);
				jsonElementList.add(new JsonElement(new Paths(name), root.get(name), keyElement, keyElement));
			}

			while (jsonElementList.size() > 0) {
				JsonElement jsonElement = jsonElementList.remove(0);
				Paths paths = jsonElement.paths;
				JsonValue jsonValue = jsonElement.jsonValue;
				AbstractJsonElement parent = jsonElement.parent;
				AbstractJsonElement dependency = jsonElement.dependency;

				if (paths.matches(absolute_paths_to_ignore) || paths.matches(relative_paths_to_ignore)) {
					IgnoredElement ignoredElement = new IgnoredElement(jsonValue, parent);
					ignoredElement.addDependency(dependency);
					atomicJsonElementList.add(0, ignoredElement);
					continue;
				}
				if (paths.matches(absolute_paths_unsplittable) || paths.matches(relative_paths_unsplittable)) {
					JsonValue compare = AdapterTools.removePaths(jsonValue, paths, absolute_paths_to_ignore,
							relative_paths_to_ignore);
					UnsplittableElement unsplittableElement = new UnsplittableElement(jsonValue, compare, parent);
					unsplittableElement.addDependency(dependency);
					atomicJsonElementList.add(0, unsplittableElement);
					continue;
				}

				if (jsonValue.isObject()) {
					Paths currentPaths = new Paths(paths);
					currentPaths.extend("{}");
					currentPaths.add("{}");

					if (currentPaths.matches(absolute_paths_to_ignore)
							|| currentPaths.matches(relative_paths_to_ignore)) {
						IgnoredElement ignoredElement = new IgnoredElement(jsonValue, parent);
						ignoredElement.addDependency(dependency);
						atomicJsonElementList.add(0, ignoredElement);
						continue;
					}
					if (currentPaths.matches(absolute_paths_unsplittable)
							|| currentPaths.matches(relative_paths_unsplittable)) {
						JsonValue compare = AdapterTools.removePaths(jsonValue, currentPaths, absolute_paths_to_ignore,
								relative_paths_to_ignore);
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
						currentPaths.add(name);

						KeyElement keyElement = new KeyElement(name, objectElement);
						keyElement.addDependency(objectElement);
						atomicJsonElementList.add(0, keyElement);

						jsonElementList
								.add(new JsonElement(currentPaths, jsonObject.get(name), keyElement, keyElement));
					}

				} else if (jsonValue.isArray()) {
					Paths currentPaths = new Paths(paths);
					currentPaths.extend("[]");
					currentPaths.add("[]");

					if (currentPaths.matches(absolute_paths_to_ignore)
							|| currentPaths.matches(relative_paths_to_ignore)) {
						IgnoredElement ignoredElement = new IgnoredElement(jsonValue, parent);
						ignoredElement.addDependency(dependency);
						atomicJsonElementList.add(0, ignoredElement);
						continue;
					}
					if (currentPaths.matches(absolute_paths_unsplittable)
							|| currentPaths.matches(relative_paths_unsplittable)) {
						JsonValue compare = AdapterTools.removePaths(jsonValue, currentPaths, absolute_paths_to_ignore,
								relative_paths_to_ignore);
						UnsplittableElement unsplittableElement = new UnsplittableElement(jsonValue, compare, parent);
						unsplittableElement.addDependency(dependency);
						atomicJsonElementList.add(0, unsplittableElement);
						continue;
					}

					JsonArray jsonArray = jsonValue.asArray();
					ArrayElement arrayElement = new ArrayElement(parent);
					arrayElement.addDependency(dependency);
					atomicJsonElementList.add(0, arrayElement);

					for (int index = 0; index < jsonArray.size(); index++) {
						currentPaths = new Paths(paths);
						currentPaths.extend("[]", "[" + index + "]");
						currentPaths.add("[]");
						currentPaths.add("[" + index + "]");

						IndexArrayElement indexArrayElement = new IndexArrayElement(id_file, arrayElement);
						jsonElementList.add(new JsonElement(currentPaths, jsonArray.get(index), indexArrayElement,
								arrayElement));
					}
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
		AdapterTools.reset();
		try {
			if (uri.toString().endsWith("/")) {
				uri = new URI(uri.toString() + "jsonConstruction.json");
			}

			File file = FileUtils.getFile(uri);
			FileUtils.createFile(file);
			JsonObject root = new JsonObject();

			for (IElement elt : elements) {
				AbstractJsonElement jsonElt = (AbstractJsonElement) elt;
				jsonElt.construct(root);
			}

			FileUtils.appendToFile(file, root.toString(WriterConfig.PRETTY_PRINT));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
