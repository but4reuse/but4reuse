/**
 */
package org.but4reuse.artefactmodel;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see org.but4reuse.artefactmodel.ArtefactModelPackage
 * @generated
 */
public interface ArtefactModelFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	ArtefactModelFactory eINSTANCE = org.but4reuse.artefactmodel.impl.ArtefactModelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Artefact Model</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Artefact Model</em>'.
	 * @generated
	 */
	ArtefactModel createArtefactModel();

	/**
	 * Returns a new object of class '<em>Artefact</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Artefact</em>'.
	 * @generated
	 */
	Artefact createArtefact();

	/**
	 * Returns a new object of class '<em>Composed Artefact</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Composed Artefact</em>'.
	 * @generated
	 */
	ComposedArtefact createComposedArtefact();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	ArtefactModelPackage getArtefactModelPackage();

} // ArtefactModelFactory
