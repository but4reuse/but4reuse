package org.but4reuse.adapters.json;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
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
		List<IElement> elements = new ArrayList<IElement>();
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
				elements.add(keyElt);

				ArrayList<String> paths = new ArrayList<String>();
				paths.add(key);

				adapt(id_file, elements, json.get(key), keyElt, paths).addDependency(keyElt);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return elements;
	}

	private AbstractElement adapt(int id_file, List<IElement> elements, JsonValue jsonVal, AbstractElement parent) {
		if (jsonVal instanceof JsonObject) {
			JsonObject jsonObj = jsonVal.asObject();
			ObjectElement objElt = new ObjectElement((IJsonElement) parent);
			elements.add(objElt);

			for (String key : jsonObj.names()) {
				KeyElement keyElt = new KeyElement(key, objElt);
				elements.add(keyElt);
				keyElt.addDependency(objElt);
				adapt(id_file, elements, jsonObj.get(key), keyElt).addDependency(keyElt);
			}

			return objElt;
		}

		if (jsonVal instanceof JsonArray) {
			JsonArray jsonArr = jsonVal.asArray();
			ArrayElement arrElt = new ArrayElement(JsonTools.generateId(), (IJsonElement) parent);
			elements.add(arrElt);
			Iterator<JsonValue> iter = jsonArr.iterator();

			while (iter.hasNext()) {
				IndexArrayElement indArrElt = new IndexArrayElement(id_file, JsonTools.generateId(), arrElt);
				adapt(id_file, elements, iter.next(), indArrElt).addDependency(arrElt);
			}

			return arrElt;
		}

		ValueElement valElt = new ValueElement(jsonVal, (IJsonElement) parent);
		elements.add(valElt);

		return valElt;
	}

	private AbstractElement adapt(int id_file, List<IElement> elements, JsonValue jsonVal, AbstractElement parent,
			ArrayList<String> paths) {
		ArrayList<String> possiblePaths = JsonTools.matchPossiblePaths(paths);

		if (possiblePaths.size() == 0) {
			return adapt(id_file, elements, jsonVal, parent);
		}

		if (JsonTools.containIgnoredPath(possiblePaths)) {
			IgnoredElement elt = new IgnoredElement(jsonVal, (IJsonElement) parent);
			elements.add(elt);
			return elt;
		}

		if (jsonVal instanceof JsonObject) {
			possiblePaths = JsonTools.addToPaths(possiblePaths, "{}");

			JsonObject jsonObj = jsonVal.asObject();
			ObjectElement objElt = new ObjectElement((IJsonElement) parent);
			elements.add(objElt);

			if (JsonTools.containIgnoredPath(possiblePaths)) {
				IgnoredElement elt = new IgnoredElement(jsonVal, objElt);
				elements.add(elt);
				elt.addDependency(objElt);
			} else {
				for (String key : jsonObj.names()) {
					KeyElement keyElt = new KeyElement(key, objElt);
					elements.add(keyElt);
					keyElt.addDependency(objElt);
					adapt(id_file, elements, jsonObj.get(key), keyElt, possiblePaths).addDependency(keyElt);
				}
			}

			return objElt;
		}

		if (jsonVal instanceof JsonArray) {
			ArrayList<String> possiblePaths_1 = JsonTools.addToPaths(possiblePaths, "[]");

			JsonArray jsonArr = jsonVal.asArray();
			ArrayElement arrElt = new ArrayElement(JsonTools.generateId(), (IJsonElement) parent);
			elements.add(arrElt);

			if (JsonTools.containIgnoredPath(possiblePaths_1)) {
				IgnoredElement elt = new IgnoredElement(jsonVal, arrElt);
				elements.add(elt);
				elt.addDependency(arrElt);
			} else {
				for (int index = 0; index < jsonArr.size(); index++) {
					ArrayList<String> possiblePaths_2 = new ArrayList<String>();
					possiblePaths_2.addAll(possiblePaths_1);
					possiblePaths_2.addAll(JsonTools.addToPaths(possiblePaths, "[" + index + "]"));

					IndexArrayElement indArrElt = new IndexArrayElement(id_file, JsonTools.generateId(), arrElt);
					adapt(id_file, elements, jsonArr.get(index), indArrElt, possiblePaths_2).addDependency(arrElt);
				}
			}

			return arrElt;
		}

		ValueElement valElt = new ValueElement(jsonVal, (IJsonElement) parent);
		elements.add(valElt);

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
