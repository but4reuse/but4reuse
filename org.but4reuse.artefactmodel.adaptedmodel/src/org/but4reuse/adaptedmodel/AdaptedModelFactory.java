/**
 */
package org.but4reuse.adaptedmodel;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see org.but4reuse.adaptedmodel.AdaptedModelPackage
 * @generated
 */
public interface AdaptedModelFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	AdaptedModelFactory eINSTANCE = org.but4reuse.adaptedmodel.impl.AdaptedModelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Adapted Model</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Adapted Model</em>'.
	 * @generated
	 */
	AdaptedModel createAdaptedModel();

	/**
	 * Returns a new object of class '<em>Adapted Artefact</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Adapted Artefact</em>'.
	 * @generated
	 */
	AdaptedArtefact createAdaptedArtefact();

	/**
	 * Returns a new object of class '<em>Element Wrapper</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Element Wrapper</em>'.
	 * @generated
	 */
	ElementWrapper createElementWrapper();

	/**
	 * Returns a new object of class '<em>Block</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Block</em>'.
	 * @generated
	 */
	Block createBlock();

	/**
	 * Returns a new object of class '<em>Block Element</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Block Element</em>'.
	 * @generated
	 */
	BlockElement createBlockElement();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	AdaptedModelPackage getAdaptedModelPackage();

} // AdaptedModelFactory
