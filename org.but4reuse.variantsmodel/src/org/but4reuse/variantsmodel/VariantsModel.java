/**
 */
package org.but4reuse.variantsmodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Variants Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.but4reuse.variantsmodel.VariantsModel#getName <em>Name</em>}</li>
 *   <li>{@link org.but4reuse.variantsmodel.VariantsModel#getDescription <em>Description</em>}</li>
 *   <li>{@link org.but4reuse.variantsmodel.VariantsModel#getOwnedVariants <em>Owned Variants</em>}</li>
 *   <li>{@link org.but4reuse.variantsmodel.VariantsModel#getFeatureModelURI <em>Feature Model URI</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.but4reuse.variantsmodel.VariantsModelPackage#getVariantsModel()
 * @model
 * @generated
 */
public interface VariantsModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.but4reuse.variantsmodel.VariantsModelPackage#getVariantsModel_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.but4reuse.variantsmodel.VariantsModel#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.but4reuse.variantsmodel.VariantsModelPackage#getVariantsModel_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.but4reuse.variantsmodel.VariantsModel#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Owned Variants</b></em>' containment reference list.
	 * The list contents are of type {@link org.but4reuse.variantsmodel.Variant}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owned Variants</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Variants</em>' containment reference list.
	 * @see org.but4reuse.variantsmodel.VariantsModelPackage#getVariantsModel_OwnedVariants()
	 * @model containment="true"
	 * @generated
	 */
	EList<Variant> getOwnedVariants();

	/**
	 * Returns the value of the '<em><b>Feature Model URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature Model URI</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature Model URI</em>' attribute.
	 * @see #setFeatureModelURI(String)
	 * @see org.but4reuse.variantsmodel.VariantsModelPackage#getVariantsModel_FeatureModelURI()
	 * @model
	 * @generated
	 */
	String getFeatureModelURI();

	/**
	 * Sets the value of the '{@link org.but4reuse.variantsmodel.VariantsModel#getFeatureModelURI <em>Feature Model URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Feature Model URI</em>' attribute.
	 * @see #getFeatureModelURI()
	 * @generated
	 */
	void setFeatureModelURI(String value);

} // VariantsModel
