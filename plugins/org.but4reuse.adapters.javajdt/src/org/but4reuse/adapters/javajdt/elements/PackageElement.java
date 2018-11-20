package org.but4reuse.adapters.javajdt.elements;

/**
 * Package Element
 * 
 * @author jabier.martinez
 */
public class PackageElement extends JDTElement {

	@Override
	public String getText() {
		return "Package: " + id;
	}
	
}
