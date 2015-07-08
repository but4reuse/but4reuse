/**
 */
package org.but4reuse.artefactmodel;

import java.util.Date;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Artefact</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.but4reuse.artefactmodel.Artefact#getName <em>Name</em>}</li>
 * <li>{@link org.but4reuse.artefactmodel.Artefact#getArtefactURI <em>Artefact
 * URI</em>}</li>
 * <li>{@link org.but4reuse.artefactmodel.Artefact#getDescription <em>
 * Description</em>}</li>
 * <li>{@link org.but4reuse.artefactmodel.Artefact#isActive <em>Active</em>}</li>
 * <li>{@link org.but4reuse.artefactmodel.Artefact#getDate <em>Date</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.but4reuse.artefactmodel.ArtefactModelPackage#getArtefact()
 * @model
 * @generated
 */
public interface Artefact extends EObject {
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
	 * @see org.but4reuse.artefactmodel.ArtefactModelPackage#getArtefact_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '
	 * {@link org.but4reuse.artefactmodel.Artefact#getName <em>Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Artefact URI</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Artefact URI</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Artefact URI</em>' attribute.
	 * @see #setArtefactURI(String)
	 * @see org.but4reuse.artefactmodel.ArtefactModelPackage#getArtefact_ArtefactURI()
	 * @model
	 * @generated
	 */
	String getArtefactURI();

	/**
	 * Sets the value of the '
	 * {@link org.but4reuse.artefactmodel.Artefact#getArtefactURI
	 * <em>Artefact URI</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Artefact URI</em>' attribute.
	 * @see #getArtefactURI()
	 * @generated
	 */
	void setArtefactURI(String value);

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
	 * @see org.but4reuse.artefactmodel.ArtefactModelPackage#getArtefact_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '
	 * {@link org.but4reuse.artefactmodel.Artefact#getDescription
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
	 * Returns the value of the '<em><b>Active</b></em>' attribute. The default
	 * value is <code>"true"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Active</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Active</em>' attribute.
	 * @see #setActive(boolean)
	 * @see org.but4reuse.artefactmodel.ArtefactModelPackage#getArtefact_Active()
	 * @model default="true"
	 * @generated
	 */
	boolean isActive();

	/**
	 * Sets the value of the '
	 * {@link org.but4reuse.artefactmodel.Artefact#isActive <em>Active</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Active</em>' attribute.
	 * @see #isActive()
	 * @generated
	 */
	void setActive(boolean value);

	/**
	 * Returns the value of the '<em><b>Date</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Date</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(Date)
	 * @see org.but4reuse.artefactmodel.ArtefactModelPackage#getArtefact_Date()
	 * @model
	 * @generated
	 */
	Date getDate();

	/**
	 * Sets the value of the '
	 * {@link org.but4reuse.artefactmodel.Artefact#getDate <em>Date</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(Date value);

} // Artefact
