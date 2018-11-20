package org.but4reuse.adapters.javajdt.elements;

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

}
