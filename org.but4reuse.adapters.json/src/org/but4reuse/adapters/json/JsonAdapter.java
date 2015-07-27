package org.but4reuse.adapters.json;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.json.activator.Activator;
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
		Map<Integer, List<IElement>> elements = new HashMap<Integer, List<IElement>>();
		File file = FileUtils.getFile(uri);

		try {
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);

			JsonObject json = JsonObject.readFrom(br);
			int id_file = JsonTools.generateId();

			JsonTools.setPathsToIgnore();

			for (String key : json.names()) {
				KeyElement keyElt = new KeyElement(key);
				JsonTools.addElement(elements, keyElt, 1);

				ArrayList<String> paths = new ArrayList<String>();
				paths.add(key);

				adapt(id_file, elements, json.get(key), keyElt, paths, 2).addDependency(keyElt);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		List<Integer> index = new ArrayList<>(elements.keySet());
		Collections.sort(index);
		Collections.reverse(index);
		List<IElement> finalListElements = new ArrayList<IElement>();

		for (int depth : index) {
			finalListElements.addAll(elements.get(depth));
		}

		return finalListElements;
	}

	private AbstractElement adapt(int id_file, Map<Integer, List<IElement>> elements, JsonValue jsonVal,
			AbstractElement parent, int depth) {
		if (jsonVal instanceof JsonObject) {
			if (Activator.getDefault().getPreferenceStore().getBoolean("OBJECT")) {
				ObjectElement objElt = new ObjectElement((IJsonElement) parent, jsonVal.asObject());
				JsonTools.addElement(elements, objElt, depth);
				return objElt;
			}

			JsonObject jsonObj = jsonVal.asObject();
			ObjectElement objElt = new ObjectElement((IJsonElement) parent);
			JsonTools.addElement(elements, objElt, depth);

			for (String key : jsonObj.names()) {
				KeyElement keyElt = new KeyElement(key, objElt);
				JsonTools.addElement(elements, keyElt, depth);
				keyElt.addDependency(objElt);
				adapt(id_file, elements, jsonObj.get(key), keyElt, depth + 1).addDependency(keyElt);
			}

			return objElt;
		}

		if (jsonVal instanceof JsonArray) {
			if (Activator.getDefault().getPreferenceStore().getBoolean("ARRAY")) {
				ArrayElement arrElt = new ArrayElement(JsonTools.generateId(), (IJsonElement) parent, jsonVal.asArray());
				JsonTools.addElement(elements, arrElt, depth);
				return arrElt;
			}
			
			JsonArray jsonArr = jsonVal.asArray();
			ArrayElement arrElt = new ArrayElement(JsonTools.generateId(), (IJsonElement) parent);
			JsonTools.addElement(elements, arrElt, depth);
			Iterator<JsonValue> iter = jsonArr.iterator();

			while (iter.hasNext()) {
				IndexArrayElement indArrElt = new IndexArrayElement(id_file, JsonTools.generateId(), arrElt);
				adapt(id_file, elements, iter.next(), indArrElt, depth + 1).addDependency(arrElt);
			}

			return arrElt;
		}

		ValueElement valElt = new ValueElement(jsonVal, (IJsonElement) parent);
		JsonTools.addElement(elements, valElt, depth);

		return valElt;
	}

	private AbstractElement adapt(int id_file, Map<Integer, List<IElement>> elements, JsonValue jsonVal,
			AbstractElement parent, ArrayList<String> paths, int depth) {
		ArrayList<String> possiblePaths = JsonTools.matchPossiblePaths(paths);

		if (possiblePaths.size() == 0) {
			return adapt(id_file, elements, jsonVal, parent, depth);
		}

		if (JsonTools.containIgnoredPath(possiblePaths)) {
			IgnoredElement elt = new IgnoredElement(jsonVal, (IJsonElement) parent);
			JsonTools.addElement(elements, elt, depth);
			return elt;
		}

		if (jsonVal instanceof JsonObject) {
			if (Activator.getDefault().getPreferenceStore().getBoolean("OBJECT")) {
				ObjectElement objElt = new ObjectElement((IJsonElement) parent, jsonVal.asObject());
				JsonTools.addElement(elements, objElt, depth);
				return objElt;
			}

			possiblePaths = JsonTools.addToPaths(possiblePaths, "{}");

			JsonObject jsonObj = jsonVal.asObject();
			ObjectElement objElt = new ObjectElement((IJsonElement) parent);
			JsonTools.addElement(elements, objElt, depth);

			if (JsonTools.containIgnoredPath(possiblePaths)) {
				IgnoredElement elt = new IgnoredElement(jsonVal, objElt);
				JsonTools.addElement(elements, elt, depth);
				elt.addDependency(objElt);
			} else {
				for (String key : jsonObj.names()) {
					KeyElement keyElt = new KeyElement(key, objElt);
					JsonTools.addElement(elements, keyElt, depth);
					keyElt.addDependency(objElt);
					adapt(id_file, elements, jsonObj.get(key), keyElt, possiblePaths, depth + 1).addDependency(keyElt);
				}
			}

			return objElt;
		}

		if (jsonVal instanceof JsonArray) {
			if (Activator.getDefault().getPreferenceStore().getBoolean("ARRAY")) {
				ArrayElement arrElt = new ArrayElement(JsonTools.generateId(), (IJsonElement) parent, jsonVal.asArray());
				JsonTools.addElement(elements, arrElt, depth);
				return arrElt;
			}
			
			ArrayList<String> possiblePaths_1 = JsonTools.addToPaths(possiblePaths, "[]");

			JsonArray jsonArr = jsonVal.asArray();
			ArrayElement arrElt = new ArrayElement(JsonTools.generateId(), (IJsonElement) parent);
			JsonTools.addElement(elements, arrElt, depth);

			if (JsonTools.containIgnoredPath(possiblePaths_1)) {
				IgnoredElement elt = new IgnoredElement(jsonVal, arrElt);
				JsonTools.addElement(elements, elt, depth);
				elt.addDependency(arrElt);
			} else {
				for (int index = 0; index < jsonArr.size(); index++) {
					ArrayList<String> possiblePaths_2 = new ArrayList<String>();
					possiblePaths_2.addAll(possiblePaths_1);
					possiblePaths_2.addAll(JsonTools.addToPaths(possiblePaths, "[" + index + "]"));

					IndexArrayElement indArrElt = new IndexArrayElement(id_file, JsonTools.generateId(), arrElt);
					adapt(id_file, elements, jsonArr.get(index), indArrElt, possiblePaths_2, depth + 1).addDependency(
							arrElt);
				}
			}

			return arrElt;
		}

		ValueElement valElt = new ValueElement(jsonVal, (IJsonElement) parent);
		JsonTools.addElement(elements, valElt, depth);

		return valElt;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		try {
			if (uri.toString().endsWith("/")) {
				uri = new URI(uri.toString() + "jsonConstruction.json");
			}

			File file = FileUtils.getFile(uri);
			FileUtils.createFile(file);
			JsonObject root = new JsonObject();

			for (IElement elt : elements) {
				IJsonElement jsonElt = (IJsonElement) elt;
				jsonElt.construct(root);
			}

			FileUtils.appendToFile(file, root.toString(WriterConfig.PRETTY_PRINT));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
