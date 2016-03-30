package org.but4reuse.tests.utils;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;

/**
 * Test Elements creator
 * 
 * @author jabier.martinez
 */
public class TestElementsCreator {

	/**
	 * Create n number of elements with an ascending id starting with 0
	 * 
	 * @param numberOfElements
	 * @return
	 */
	public static List<IElement> createElements(int numberOfElements) {
		List<IElement> elements = new ArrayList<IElement>();
		for (int i = 0; i < numberOfElements; i++) {
			TestElement e = new TestElement();
			e.id = i;
			elements.add(e);
		}
		return elements;
	}

}
