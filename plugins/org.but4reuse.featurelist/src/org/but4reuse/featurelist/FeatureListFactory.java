/**
 */
package org.but4reuse.featurelist;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see org.but4reuse.featurelist.FeatureListPackage
 * @generated
 */
public interface FeatureListFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	FeatureListFactory eINSTANCE = org.but4reuse.featurelist.impl.FeatureListFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Feature List</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Feature List</em>'.
	 * @generated
	 */
	FeatureList createFeatureList();

	/**
	 * Returns a new object of class '<em>Feature</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Feature</em>'.
	 * @generated
	 */
	Feature createFeature();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	FeatureListPackage getFeatureListPackage();

} // FeatureListFactory
