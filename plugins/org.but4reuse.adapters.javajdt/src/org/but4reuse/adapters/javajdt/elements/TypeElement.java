package org.but4reuse.adapters.javajdt.elements;

import org.but4reuse.adapters.javajdt.JDTParser;

/**
 * Type Element
 * 
 * @author jabier.martinez
 */
public class TypeElement extends JDTElement {

	public TypeElement() {
		super();
		// Only one extended type allowed per type
		setMaximumDependencies("extends", 1);
	}
	
	@Override
	public String getText() {
		return "Type: " + id;
	}
	
	@Override
	public boolean isContainment(String dependencyId) {
		return dependencyId.equals(JDTParser.DEPENDENCY_COMPILATION_UNIT);
	}
	
}
