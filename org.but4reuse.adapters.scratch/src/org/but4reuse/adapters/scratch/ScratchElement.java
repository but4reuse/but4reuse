package org.but4reuse.adapters.scratch;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.json.UnsplittableElement;
import org.but4reuse.utils.strings.StringUtils;

/**
 * A json element wrapper
 */
public class ScratchElement extends AbstractElement {

	public IElement element;

	public ScratchElement(IElement element) {
		super();
		this.element = element;
	}

	@Override
	public double similarity(IElement anotherElement) {
		return this.element.similarity(((ScratchElement) anotherElement).element);
	}

	@Override
	public String getText() {
		return this.element.getText();
	}

	private static final String OBJ_NAME = "\"objName\":\"";

	@Override
	public List<String> getWords() {
		// Get words from the object names, which for the moment are
		// unsplittable elements
		List<String> words = new ArrayList<String>();
		if (element instanceof UnsplittableElement) {
			int index = element.getText().indexOf(OBJ_NAME);
			if (index >= 0) {
				int endIndex = element.getText().indexOf("\"", index + OBJ_NAME.length());
				String text = element.getText().substring(index + OBJ_NAME.length(), endIndex);
				return StringUtils.tokenizeAndCamelCase(text);
			}
		}
		return words;
	}

}
