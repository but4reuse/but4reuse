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
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			JsonObject json = JsonObject.readFrom(br);

			for (String key : json.names()) {
				KeyElement keyElt = new KeyElement(key);
				elements.add(keyElt);

				adapt(elements, json.get(key), keyElt);
			}

			br.close();
			in.close();
			fstream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return elements;
	}

	/*
	 * recursive adapt function, tests if the jsonVal is a value, array or
	 * object and construct the appropriate IJsonElement
	 */

	public void adapt(List<IElement> elements, JsonValue jsonVal, IJsonElement parent) {
		if (jsonVal instanceof JsonObject) {
			JsonObject jsonObj = jsonVal.asObject();

			ObjectElement objElt = new ObjectElement((IJsonValuedElement) parent);
			elements.add(objElt);

			objElt.addDependency(parent);

			for (String key : jsonObj.names()) {
				KeyElement keyElt = new KeyElement(key, objElt);
				elements.add(keyElt);

				keyElt.addDependency(objElt);

				adapt(elements, jsonObj.get(key), keyElt);
			}
		} else if (jsonVal instanceof JsonArray) {
			JsonArray jsonArr = jsonVal.asArray();

			ArrayElement arrElt = new ArrayElement((IJsonValuedElement) parent);
			elements.add(arrElt);

			arrElt.addDependency(parent);

			for (int index = 0; index < jsonArr.size(); index++) {
				IndexArrayElement indArrElt = new IndexArrayElement(index, arrElt);
				elements.add(indArrElt);

				indArrElt.addDependency(arrElt);

				adapt(elements, jsonArr.get(index), indArrElt);
			}
		} else {
			ValueElement valElt = new ValueElement(jsonVal, (IJsonValuedElement) parent);
			elements.add(valElt);

			valElt.addDependency(parent);
		}
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		try {
			if (uri.toString().endsWith("/")) {
				uri = new URI(uri.toString() + "jsonConstruction.txt");
			}

			File file = FileUtils.getFile(uri);
			FileUtils.createFile(file);

			JsonObject root = new JsonObject();

			for (IElement element : elements) {
				IJsonElement jsonElt = (IJsonElement) element;
				jsonElt.construct(root);
			}

			FileUtils.appendToFile(file, root.toString(WriterConfig.PRETTY_PRINT));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
