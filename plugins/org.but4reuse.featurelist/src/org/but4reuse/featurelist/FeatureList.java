/**
 */
package org.but4reuse.featurelist;

import org.but4reuse.artefactmodel.ArtefactModel;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Feature List</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.but4reuse.featurelist.FeatureList#getName <em>Name</em>}</li>
 * <li>{@link org.but4reuse.featurelist.FeatureList#getExternalFeatureModelURI
 * <em>External Feature Model URI</em>}</li>
 * <li>{@link org.but4reuse.featurelist.FeatureList#getOwnedFeatures <em>Owned
 * Features</em>}</li>
 * <li>{@link org.but4reuse.featurelist.FeatureList#getArtefactModel <em>
 * Artefact Model</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.but4reuse.featurelist.FeatureListPackage#getFeatureList()
 * @model
 * @generated
 */
public interface FeatureList extends EObject {
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
	 * @see org.but4reuse.featurelist.FeatureListPackage#getFeatureList_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '
	 * {@link org.but4reuse.featurelist.FeatureList#getName <em>Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>External Feature Model URI</b></em>'
	 * attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>External Feature Model URI</em>' attribute
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>External Feature Model URI</em>' attribute.
	 * @see #setExternalFeatureModelURI(String)
	 * @see org.but4reuse.featurelist.FeatureListPackage#getFeatureList_ExternalFeatureModelURI()
	 * @model
	 * @generated
	 */
	String getExternalFeatureModelURI();

	/**
	 * Sets the value of the '
	 * {@link org.but4reuse.featurelist.FeatureList#getExternalFeatureModelURI
	 * <em>External Feature Model URI</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>External Feature Model URI</em>'
	 *            attribute.
	 * @see #getExternalFeatureModelURI()
	 * @generated
	 */
	void setExternalFeatureModelURI(String value);

	/**
	 * Returns the value of the '<em><b>Owned Features</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.but4reuse.featurelist.Feature}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owned Features</em>' containment reference
	 * list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Owned Features</em>' containment reference
	 *         list.
	 * @see org.but4reuse.featurelist.FeatureListPackage#getFeatureList_OwnedFeatures()
	 * @model containment="true"
	 * @generated
	 */
	EList<Feature> getOwnedFeatures();

	/**
	 * Returns the value of the '<em><b>Artefact Model</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Artefact Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Artefact Model</em>' reference.
	 * @see #setArtefactModel(ArtefactModel)
	 * @see org.but4reuse.featurelist.FeatureListPackage#getFeatureList_ArtefactModel()
	 * @model
	 * @generated
	 */
	ArtefactModel getArtefactModel();

	/**
	 * Sets the value of the '
	 * {@link org.but4reuse.featurelist.FeatureList#getArtefactModel
	 * <em>Artefact Model</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Artefact Model</em>' reference.
	 * @see #getArtefactModel()
	 * @generated
	 */
	void setArtefactModel(ArtefactModel value);

} // FeatureList
