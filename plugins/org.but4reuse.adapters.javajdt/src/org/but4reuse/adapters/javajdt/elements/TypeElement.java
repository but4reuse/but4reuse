package org.but4reuse.adapters.javajdt.elements;

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
	
}
