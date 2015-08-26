package org.but4reuse.adapters.json;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.json.tools.AdapterTools;

public class ArrayElement extends AbstractElement {
	public IElement parent;
	public int id;
	public List<ArrayElement> similarArrays;

	public ArrayElement(IElement parent) {
		this.parent = parent;
		this.id = AdapterTools.getUniqueId();
		this.similarArrays = new ArrayList<ArrayElement>();
		this.similarArrays.add(this);
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof ArrayElement) {
			ArrayElement arrayElement = (ArrayElement) anotherElement;

			if (this.id == arrayElement.id)
				return 1;

			if (this.parent.similarity(arrayElement.parent) == 1) {
				List<ArrayElement> similarArrays = new ArrayList<ArrayElement>();
				similarArrays.addAll(this.similarArrays);
				similarArrays.addAll(arrayElement.similarArrays);

				for (ArrayElement currentArray : similarArrays) {
					currentArray.id = this.id;
					currentArray.similarArrays = similarArrays;
				}

				return 1;
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		return parent.getText() + "_[]";
	}
}
