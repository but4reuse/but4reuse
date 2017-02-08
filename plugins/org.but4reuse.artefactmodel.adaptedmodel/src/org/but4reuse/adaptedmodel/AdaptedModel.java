/**
 */
package org.but4reuse.adaptedmodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Adapted Model</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.but4reuse.adaptedmodel.AdaptedModel#getOwnedBlocks <em>Owned
 * Blocks</em>}</li>
 * <li>{@link org.but4reuse.adaptedmodel.AdaptedModel#getOwnedAdaptedArtefacts
 * <em>Owned Adapted Artefacts</em>}</li>
 * <li>{@link org.but4reuse.adaptedmodel.AdaptedModel#getConstraints <em>
 * Constraints</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.but4reuse.adaptedmodel.AdaptedModelPackage#getAdaptedModel()
 * @model
 * @generated
 */
public interface AdaptedModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Owned Blocks</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.but4reuse.adaptedmodel.Block}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owned Blocks</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Owned Blocks</em>' containment reference
	 *         list.
	 * @see org.but4reuse.adaptedmodel.AdaptedModelPackage#getAdaptedModel_OwnedBlocks()
	 * @model containment="true"
	 * @generated
	 */
	EList<Block> getOwnedBlocks();

	/**
	 * Returns the value of the '<em><b>Owned Adapted Artefacts</b></em>'
	 * containment reference list. The list contents are of type
	 * {@link org.but4reuse.adaptedmodel.AdaptedArtefact}. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Owned Adapted Artefacts</em>' containment
	 * reference list isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Owned Adapted Artefacts</em>' containment
	 *         reference list.
	 * @see org.but4reuse.adaptedmodel.AdaptedModelPackage#getAdaptedModel_OwnedAdaptedArtefacts()
	 * @model containment="true"
	 * @generated
	 */
	EList<AdaptedArtefact> getOwnedAdaptedArtefacts();

	/**
	 * Returns the value of the '<em><b>Constraints</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraints</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Constraints</em>' attribute.
	 * @see #setConstraints(Object)
	 * @see org.but4reuse.adaptedmodel.AdaptedModelPackage#getAdaptedModel_Constraints()
	 * @model transient="true"
	 * @generated
	 */
	Object getConstraints();

	/**
	 * Sets the value of the '
	 * {@link org.but4reuse.adaptedmodel.AdaptedModel#getConstraints
	 * <em>Constraints</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Constraints</em>' attribute.
	 * @see #getConstraints()
	 * @generated
	 */
	void setConstraints(Object value);

} // AdaptedModel
