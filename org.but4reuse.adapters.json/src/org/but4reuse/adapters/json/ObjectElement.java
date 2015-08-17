package org.but4reuse.adapters.json;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class ObjectElement extends AbstractJsonElement {
	public AbstractJsonElement parent;
	public JsonObject jsonObject;
	public List<ObjectElement> similarObjects;

	public ObjectElement(AbstractJsonElement parent) {
		this.parent = parent;
		this.jsonObject = null;
		this.similarObjects = new ArrayList<ObjectElement>();
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof ObjectElement) {
			ObjectElement objectElement = (ObjectElement) anotherElement;
			if (this.parent.similarity(objectElement.parent) == 1) {
				this.similarObjects.add(objectElement);
				objectElement.similarObjects.add(this);

				return 1;

			}
		}
		return 0;
	}

	@Override
	public String getText() {
		return parent.getText() + "_{}";
	}

	@Override
	public JsonValue construct(JsonObject root) {
		if (this.jsonObject == null) {
			this.jsonObject = new JsonObject();
			for (ObjectElement objElt : this.similarObjects)
				objElt.jsonObject = this.jsonObject;
			this.parent.construct(root, this.jsonObject);
		}
		return this.jsonObject;
	}

	@Override
	public JsonValue construct(JsonObject root, JsonValue jsonValue) {
		return this.construct(root);
	}

}
