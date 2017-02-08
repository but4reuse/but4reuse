package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

import com.eclipsesource.json.JsonValue;

public class UnsplittableElement extends AbstractElement {
	public JsonValue content;
	public JsonValue compare;
	public IElement parent;

	public UnsplittableElement(JsonValue content, JsonValue compare, IElement parent) {
		this.content = content;
		this.compare = compare;
		this.parent = parent;
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof UnsplittableElement) {
			UnsplittableElement unsplittableElement = (UnsplittableElement) anotherElement;
			if (this.compare.equals(unsplittableElement.compare)) {
				return this.parent.similarity(unsplittableElement.parent);
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		return parent.getText() + "_/UNSPLITTABLE/_" + content.toString();
	}
}
