/**
 */
package org.but4reuse.variantsmodel;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.but4reuse.variantsmodel.VariantsModelPackage
 * @generated
 */
public interface VariantsModelFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	VariantsModelFactory eINSTANCE = org.but4reuse.variantsmodel.impl.VariantsModelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Variants Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Variants Model</em>'.
	 * @generated
	 */
	VariantsModel createVariantsModel();

	/**
	 * Returns a new object of class '<em>Variant</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Variant</em>'.
	 * @generated
	 */
	Variant createVariant();

	/**
	 * Returns a new object of class '<em>Composed Variant</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Composed Variant</em>'.
	 * @generated
	 */
	ComposedVariant createComposedVariant();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	VariantsModelPackage getVariantsModelPackage();

} //VariantsModelFactory
