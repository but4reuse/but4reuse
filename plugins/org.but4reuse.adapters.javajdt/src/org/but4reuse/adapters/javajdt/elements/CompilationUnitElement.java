package org.but4reuse.adapters.javajdt.elements;

/**
 * Compilation unit element
 * 
 * @author jabier.martinez
 */
public class CompilationUnitElement extends JDTElement {
	
	@Override
	public String getText() {
		return "CompilationUnit: " + id;
	}

}
