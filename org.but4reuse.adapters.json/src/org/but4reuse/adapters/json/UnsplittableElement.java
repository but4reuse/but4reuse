package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class UnsplittableElement extends AbstractJsonElement {
	public JsonValue content;
	public JsonValue compare;
	public AbstractJsonElement parent;

	public UnsplittableElement(JsonValue content, JsonValue compare, AbstractJsonElement parent) {
		this.content = content;
		this.compare = compare;
		this.parent = parent;
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof UnsplittableElement) {
			UnsplittableElement unsplittableElement = (UnsplittableElement) anotherElement;
			if (this.compare.equals(unsplittableElement.compare))
				return this.parent.similarity(unsplittableElement.parent);
		}
		return 0;
	}

	@Override
	public String getText() {
		return parent.getText() + "_/UNSPLITTABLE/_" + content.toString();
	}

	@Override
	public JsonValue construct(JsonObject root) {
		return this.parent.construct(root, this.content);
	}

	@Override
	public JsonValue construct(JsonObject root, JsonValue jsonValue) {
		return this.construct(root);
	}

}
