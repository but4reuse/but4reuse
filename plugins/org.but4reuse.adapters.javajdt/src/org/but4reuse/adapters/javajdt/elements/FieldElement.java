package org.but4reuse.adapters.javajdt.elements;

import org.but4reuse.adapters.javajdt.JDTParser;

/**
 * Field element
 * 
 * @author jabier.martinez
 */
public class FieldElement extends JDTElement {
	
	@Override
	public String getText() {
		return "Field: " + id;
	}
	
	@Override
	public boolean isContainment(String dependencyId) {
		return dependencyId.equals(JDTParser.DEPENDENCY_TYPE);
	}

}
