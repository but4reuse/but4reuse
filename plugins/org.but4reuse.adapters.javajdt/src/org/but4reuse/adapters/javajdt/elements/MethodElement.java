package org.but4reuse.adapters.javajdt.elements;

/**
 * Method Element
 * 
 * @author jabier.martinez
 */
public class MethodElement extends JDTElement {

	public MethodElement() {
		super();
		// Only one methodBody allowed per method
		setMaximumDependencies("methodBody", 1);
	}
	
	@Override
	public String getText() {
		return "Method: " + id;
	}
}
