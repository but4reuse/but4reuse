package org.but4reuse.adapters.emf;

/**
 * Default EMF Elements Factory
 * 
 * @author jabier.martinez
 */
public class EMFElementsFactory {

	public EMFClassElement createEMFClassElement() {
		return new EMFClassElement();
	}

	public EMFAttributeElement createEMFAttributeElement() {
		return new EMFAttributeElement();
	}

	public EMFReferenceElement createEMFReferenceElement() {
		return new EMFReferenceElement();
	}
}
