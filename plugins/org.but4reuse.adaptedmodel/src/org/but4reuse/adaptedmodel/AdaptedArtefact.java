/**
 */
package org.but4reuse.adaptedmodel;

import org.but4reuse.artefactmodel.Artefact;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Adapted Artefact</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.but4reuse.adaptedmodel.AdaptedArtefact#getArtefact <em>
 * Artefact</em>}</li>
 * <li>
 * {@link org.but4reuse.adaptedmodel.AdaptedArtefact#getOwnedElementWrappers
 * <em>Owned Element Wrappers</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.but4reuse.adaptedmodel.AdaptedModelPackage#getAdaptedArtefact()
 * @model
 * @generated
 */
public interface AdaptedArtefact extends EObject {
	/**
	 * Returns the value of the '<em><b>Artefact</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Artefact</em>' reference isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Artefact</em>' reference.
	 * @see #setArtefact(Artefact)
	 * @see org.but4reuse.adaptedmodel.AdaptedModelPackage#getAdaptedArtefact_Artefact()
	 * @model
	 * @generated
	 */
	Artefact getArtefact();

	/**
	 * Sets the value of the '
	 * {@link org.but4reuse.adaptedmodel.AdaptedArtefact#getArtefact
	 * <em>Artefact</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *            the new value of the '<em>Artefact</em>' reference.
	 * @see #getArtefact()
	 * @generated
	 */
	void setArtefact(Artefact value);

	/**
	 * Returns the value of the '<em><b>Owned Element Wrappers</b></em>'
	 * containment reference list. The list contents are of type
	 * {@link org.but4reuse.adaptedmodel.ElementWrapper}. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Owned Element Wrappers</em>' reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Owned Element Wrappers</em>' containment
	 *         reference list.
	 * @see org.but4reuse.adaptedmodel.AdaptedModelPackage#getAdaptedArtefact_OwnedElementWrappers()
	 * @model containment="true"
	 * @generated
	 */
	EList<ElementWrapper> getOwnedElementWrappers();

} // AdaptedArtefact
