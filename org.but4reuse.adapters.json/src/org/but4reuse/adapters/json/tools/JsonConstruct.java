package org.but4reuse.adapters.json.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.json.ArrayElement;
import org.but4reuse.adapters.json.IgnoredElement;
import org.but4reuse.adapters.json.IndexArrayElement;
import org.but4reuse.adapters.json.KeyElement;
import org.but4reuse.adapters.json.ObjectElement;
import org.but4reuse.adapters.json.UnsplittableElement;
import org.but4reuse.adapters.json.ValueElement;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class JsonConstruct {
	public JsonObject root;
	private Map<Integer, JsonObject> objectMap;
	private Map<Integer, JsonArray> arrayMap;
	private Map<Integer, List<Integer>> arrayIndexes;

	public JsonConstruct() {
		this.root = new JsonObject();
		this.objectMap = new HashMap<Integer, JsonObject>();
		this.arrayMap = new HashMap<Integer, JsonArray>();
		this.arrayIndexes = new HashMap<Integer, List<Integer>>();
	}

	public JsonValue construct(IElement element) {
		return construct(element, null);
	}

	private JsonValue construct(IElement element, JsonValue value) {
		if (element instanceof ArrayElement) {
			ArrayElement arrayElement = (ArrayElement) element;
			int id = arrayElement.id;

			if (this.arrayMap.get(id) == null) {
				JsonArray jsonArray = new JsonArray();
				this.arrayMap.put(id, jsonArray);
				this.construct(arrayElement.parent, jsonArray);
			}

			return this.arrayMap.get(id);
		}
		if (element instanceof IgnoredElement) {
			IgnoredElement ignoredElement = (IgnoredElement) element;
			return construct(ignoredElement.parent, ignoredElement.jsonValue);
		}
		if (element instanceof IndexArrayElement) {
			IndexArrayElement indexArrayElement = (IndexArrayElement) element;
			JsonArray jsonArray = construct(indexArrayElement.parent, null).asArray();

			List<Integer> idsAhead = new ArrayList<Integer>();
			for (IndexArrayElement currentIndex : indexArrayElement.indexesAhead)
				idsAhead.add(currentIndex.id);

			int idArray = indexArrayElement.parent.id;
			if (arrayIndexes.get(idArray) == null)
				arrayIndexes.put(idArray, new ArrayList<Integer>());

			List<Integer> indexes = arrayIndexes.get(idArray);
			int index = 0;
			for (int i = 0; i < indexes.size(); i++)
				if (idsAhead.contains(indexes.get(i)))
					index = i + 1;

			indexes.add(index, indexArrayElement.id);

			JsonValue currentValue = value;
			for (; index < jsonArray.size(); index++) {
				JsonValue tmpValue = jsonArray.get(index);
				jsonArray.set(index, currentValue);
				currentValue = tmpValue;
			}
			jsonArray.add(currentValue);

			return value;
		}
		if (element instanceof KeyElement) {
			KeyElement keyElement = (KeyElement) element;
			JsonObject jsonObject;

			if (keyElement.parent == null)
				jsonObject = this.root;
			else
				jsonObject = construct(keyElement.parent, null).asObject();

			JsonValue currentValue = jsonObject.get(keyElement.name);

			if (value == null) {
				if (currentValue == null)
					jsonObject.set(keyElement.name, JsonValue.NULL);
			} else {
				jsonObject.set(keyElement.name, value);
			}

			return jsonObject.get(keyElement.name);
		}
		if (element instanceof ObjectElement) {
			ObjectElement objectElement = (ObjectElement) element;
			int id = objectElement.id;

			if (this.objectMap.get(id) == null) {
				JsonObject jsonObject = new JsonObject();
				this.objectMap.put(id, jsonObject);
				this.construct(objectElement.parent, jsonObject);
			}

			return this.objectMap.get(id);
		}
		if (element instanceof UnsplittableElement) {
			UnsplittableElement unsplittableElement = (UnsplittableElement) element;
			return construct(unsplittableElement.parent, unsplittableElement.content);
		}
		if (element instanceof ValueElement) {
			ValueElement valueElement = (ValueElement) element;
			return this.construct(valueElement.parent, valueElement.jsonValue);
		}
		return value;
	}
}
