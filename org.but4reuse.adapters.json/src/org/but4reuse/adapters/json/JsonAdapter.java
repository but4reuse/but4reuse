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

public class JsonAdapter implements IAdapter
{

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
			
			for (String key : json.names()) {
				KeyElement keyElt = new KeyElement(key);
				elements.add(keyElt);
				adapt(id_file, elements, json.get(key), keyElt).addDependency(keyElt);;
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
				adapt(id_file, elements, jsonObj.get(key), keyElt).addDependency(keyElt);;
			}
			
			return objElt;
		}
		
		if (jsonVal instanceof JsonArray) {
			JsonArray jsonArr = jsonVal.asArray();
			ArrayElement arrElt = new ArrayElement((IJsonElement) parent);
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
	
	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

}
