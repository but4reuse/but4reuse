package org.but4reuse.adapters.javajdt.elements;

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

}
