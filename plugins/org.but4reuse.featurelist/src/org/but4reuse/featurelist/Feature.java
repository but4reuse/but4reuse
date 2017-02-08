/**
 */
package org.but4reuse.featurelist;

import org.but4reuse.artefactmodel.Artefact;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Feature</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.but4reuse.featurelist.Feature#getId <em>Id</em>}</li>
 * <li>{@link org.but4reuse.featurelist.Feature#getName <em>Name</em>}</li>
 * <li>{@link org.but4reuse.featurelist.Feature#getImplementedInArtefacts <em>
 * Implemented In Artefacts</em>}</li>
 * <li>{@link org.but4reuse.featurelist.Feature#getDescription <em>Description
 * </em>}</li>
 * <li>{@link org.but4reuse.featurelist.Feature#getNegationFeatureOf <em>
 * Negation Feature Of</em>}</li>
 * <li>{@link org.but4reuse.featurelist.Feature#getInteractionFeatureOf <em>
 * Interaction Feature Of</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.but4reuse.featurelist.FeatureListPackage#getFeature()
 * @model
 * @generated
 */
public interface Feature extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.but4reuse.featurelist.FeatureListPackage#getFeature_Id()
	 * @model id="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.but4reuse.featurelist.Feature#getId
	 * <em>Id</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

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
	 * @see org.but4reuse.featurelist.FeatureListPackage#getFeature_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.but4reuse.featurelist.Feature#getName
	 * <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Implemented In Artefacts</b></em>'
	 * reference list. The list contents are of type
	 * {@link org.but4reuse.artefactmodel.Artefact}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Implemented In Artefacts</em>' reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Implemented In Artefacts</em>' reference
	 *         list.
	 * @see org.but4reuse.featurelist.FeatureListPackage#getFeature_ImplementedInArtefacts()
	 * @model
	 * @generated
	 */
	EList<Artefact> getImplementedInArtefacts();

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
	 * @see org.but4reuse.featurelist.FeatureListPackage#getFeature_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '
	 * {@link org.but4reuse.featurelist.Feature#getDescription
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
	 * Returns the value of the '<em><b>Negation Feature Of</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Negation Feature Of</em>' reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Negation Feature Of</em>' reference.
	 * @see #setNegationFeatureOf(Feature)
	 * @see org.but4reuse.featurelist.FeatureListPackage#getFeature_NegationFeatureOf()
	 * @model
	 * @generated
	 */
	Feature getNegationFeatureOf();

	/**
	 * Sets the value of the '
	 * {@link org.but4reuse.featurelist.Feature#getNegationFeatureOf
	 * <em>Negation Feature Of</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Negation Feature Of</em>' reference.
	 * @see #getNegationFeatureOf()
	 * @generated
	 */
	void setNegationFeatureOf(Feature value);

	/**
	 * Returns the value of the '<em><b>Interaction Feature Of</b></em>'
	 * reference list. The list contents are of type
	 * {@link org.but4reuse.featurelist.Feature}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Interaction Feature Of</em>' reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Interaction Feature Of</em>' reference
	 *         list.
	 * @see org.but4reuse.featurelist.FeatureListPackage#getFeature_InteractionFeatureOf()
	 * @model
	 * @generated
	 */
	EList<Feature> getInteractionFeatureOf();

} // Feature
