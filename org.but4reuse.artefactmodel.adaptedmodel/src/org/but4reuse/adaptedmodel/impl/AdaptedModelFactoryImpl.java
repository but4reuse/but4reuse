/**
 */
package org.but4reuse.adaptedmodel.impl;

import org.but4reuse.adaptedmodel.*;
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
public class AdaptedModelFactoryImpl extends EFactoryImpl implements AdaptedModelFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public static AdaptedModelFactory init() {
		try {
			AdaptedModelFactory theAdaptedModelFactory = (AdaptedModelFactory) EPackage.Registry.INSTANCE
					.getEFactory(AdaptedModelPackage.eNS_URI);
			if (theAdaptedModelFactory != null) {
				return theAdaptedModelFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new AdaptedModelFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public AdaptedModelFactoryImpl() {
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
		case AdaptedModelPackage.ADAPTED_MODEL:
			return createAdaptedModel();
		case AdaptedModelPackage.ADAPTED_ARTEFACT:
			return createAdaptedArtefact();
		case AdaptedModelPackage.ELEMENT_WRAPPER:
			return createElementWrapper();
		case AdaptedModelPackage.BLOCK:
			return createBlock();
		case AdaptedModelPackage.BLOCK_ELEMENT:
			return createBlockElement();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AdaptedModel createAdaptedModel() {
		AdaptedModelImpl adaptedModel = new AdaptedModelImpl();
		return adaptedModel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AdaptedArtefact createAdaptedArtefact() {
		AdaptedArtefactImpl adaptedArtefact = new AdaptedArtefactImpl();
		return adaptedArtefact;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ElementWrapper createElementWrapper() {
		ElementWrapperImpl elementWrapper = new ElementWrapperImpl();
		return elementWrapper;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Block createBlock() {
		BlockImpl block = new BlockImpl();
		return block;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public BlockElement createBlockElement() {
		BlockElementImpl blockElement = new BlockElementImpl();
		return blockElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AdaptedModelPackage getAdaptedModelPackage() {
		return (AdaptedModelPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static AdaptedModelPackage getPackage() {
		return AdaptedModelPackage.eINSTANCE;
	}

} // AdaptedModelFactoryImpl
