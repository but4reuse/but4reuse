/**
 */
package org.but4reuse.adaptedmodel;

import org.but4reuse.featurelist.Feature;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Block</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.but4reuse.adaptedmodel.Block#getOwnedBlockElements <em>Owned Block Elements</em>}</li>
 *   <li>{@link org.but4reuse.adaptedmodel.Block#getCorrespondingFeature <em>Corresponding Feature</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.but4reuse.adaptedmodel.AdaptedModelPackage#getBlock()
 * @model
 * @generated
 */
public interface Block extends EObject {
	/**
	 * Returns the value of the '<em><b>Owned Block Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.but4reuse.adaptedmodel.BlockElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owned Block Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Block Elements</em>' containment reference list.
	 * @see org.but4reuse.adaptedmodel.AdaptedModelPackage#getBlock_OwnedBlockElements()
	 * @model containment="true"
	 * @generated
	 */
	EList<BlockElement> getOwnedBlockElements();

	/**
	 * Returns the value of the '<em><b>Corresponding Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Corresponding Feature</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Corresponding Feature</em>' reference.
	 * @see #setCorrespondingFeature(Feature)
	 * @see org.but4reuse.adaptedmodel.AdaptedModelPackage#getBlock_CorrespondingFeature()
	 * @model
	 * @generated
	 */
	Feature getCorrespondingFeature();

	/**
	 * Sets the value of the '{@link org.but4reuse.adaptedmodel.Block#getCorrespondingFeature <em>Corresponding Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Corresponding Feature</em>' reference.
	 * @see #getCorrespondingFeature()
	 * @generated
	 */
	void setCorrespondingFeature(Feature value);

} // Block
