/**
 */
package org.but4reuse.adaptedmodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Block Element</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.but4reuse.adaptedmodel.BlockElement#getElementWrappers <em>
 * Element Wrappers</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.but4reuse.adaptedmodel.AdaptedModelPackage#getBlockElement()
 * @model
 * @generated
 */
public interface BlockElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Element Wrappers</b></em>' reference
	 * list. The list contents are of type
	 * {@link org.but4reuse.adaptedmodel.ElementWrapper}. It is bidirectional
	 * and its opposite is '
	 * {@link org.but4reuse.adaptedmodel.ElementWrapper#getBlockElements
	 * <em>Block Elements</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Element Wrappers</em>' reference list isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Element Wrappers</em>' reference list.
	 * @see org.but4reuse.adaptedmodel.AdaptedModelPackage#getBlockElement_ElementWrappers()
	 * @see org.but4reuse.adaptedmodel.ElementWrapper#getBlockElements
	 * @model opposite="blockElements"
	 * @generated
	 */
	EList<ElementWrapper> getElementWrappers();

} // BlockElement
