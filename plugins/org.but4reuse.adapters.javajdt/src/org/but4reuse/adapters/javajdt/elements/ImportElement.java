package org.but4reuse.adapters.javajdt.elements;

import org.but4reuse.adapters.javajdt.JDTParser;

/**
 * Import element
 * 
 * @author jabier.martinez
 */
public class ImportElement extends JDTElement {
	
	@Override
	public String getText() {
		return "Import: " + id;
	}
	
	@Override
	public boolean isContainment(String dependencyId) {
		return dependencyId.equals(JDTParser.DEPENDENCY_COMPILATION_UNIT);
	}

}
