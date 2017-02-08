/**
 */
package org.but4reuse.artefactmodel.impl;

import org.but4reuse.artefactmodel.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class ArtefactModelFactoryImpl extends EFactoryImpl implements ArtefactModelFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public static ArtefactModelFactory init() {
		try {
			ArtefactModelFactory theArtefactModelFactory = (ArtefactModelFactory) EPackage.Registry.INSTANCE
					.getEFactory(ArtefactModelPackage.eNS_URI);
			if (theArtefactModelFactory != null) {
				return theArtefactModelFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ArtefactModelFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public ArtefactModelFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case ArtefactModelPackage.ARTEFACT_MODEL:
			return createArtefactModel();
		case ArtefactModelPackage.ARTEFACT:
			return createArtefact();
		case ArtefactModelPackage.COMPOSED_ARTEFACT:
			return createComposedArtefact();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ArtefactModel createArtefactModel() {
		ArtefactModelImpl artefactModel = new ArtefactModelImpl();
		return artefactModel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Artefact createArtefact() {
		ArtefactImpl artefact = new ArtefactImpl();
		return artefact;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ComposedArtefact createComposedArtefact() {
		ComposedArtefactImpl composedArtefact = new ComposedArtefactImpl();
		return composedArtefact;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ArtefactModelPackage getArtefactModelPackage() {
		return (ArtefactModelPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ArtefactModelPackage getPackage() {
		return ArtefactModelPackage.eINSTANCE;
	}

} // ArtefactModelFactoryImpl
