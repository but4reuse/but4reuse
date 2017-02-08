/**
 */
package org.but4reuse.artefactmodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Artefact Model</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.but4reuse.artefactmodel.ArtefactModel#getName <em>Name</em>}</li>
 * <li>{@link org.but4reuse.artefactmodel.ArtefactModel#getDescription <em>
 * Description</em>}</li>
 * <li>{@link org.but4reuse.artefactmodel.ArtefactModel#getAdapters <em>Adapters
 * </em>}</li>
 * <li>{@link org.but4reuse.artefactmodel.ArtefactModel#getOwnedArtefacts <em>
 * Owned Artefacts</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.but4reuse.artefactmodel.ArtefactModelPackage#getArtefactModel()
 * @model
 * @generated
 */
public interface ArtefactModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.but4reuse.artefactmodel.ArtefactModelPackage#getArtefactModel_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '
	 * {@link org.but4reuse.artefactmodel.ArtefactModel#getName <em>Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.but4reuse.artefactmodel.ArtefactModelPackage#getArtefactModel_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '
	 * {@link org.but4reuse.artefactmodel.ArtefactModel#getDescription
	 * <em>Description</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Adapters</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Adapters</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Adapters</em>' attribute.
	 * @see #setAdapters(String)
	 * @see org.but4reuse.artefactmodel.ArtefactModelPackage#getArtefactModel_Adapters()
	 * @model
	 * @generated
	 */
	String getAdapters();

	/**
	 * Sets the value of the '
	 * {@link org.but4reuse.artefactmodel.ArtefactModel#getAdapters
	 * <em>Adapters</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *            the new value of the '<em>Adapters</em>' attribute.
	 * @see #getAdapters()
	 * @generated
	 */
	void setAdapters(String value);

	/**
	 * Returns the value of the '<em><b>Owned Artefacts</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.but4reuse.artefactmodel.Artefact}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owned Artefacts</em>' containment reference
	 * list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Owned Artefacts</em>' containment reference
	 *         list.
	 * @see org.but4reuse.artefactmodel.ArtefactModelPackage#getArtefactModel_OwnedArtefacts()
	 * @model containment="true"
	 * @generated
	 */
	EList<Artefact> getOwnedArtefacts();

} // ArtefactModel
