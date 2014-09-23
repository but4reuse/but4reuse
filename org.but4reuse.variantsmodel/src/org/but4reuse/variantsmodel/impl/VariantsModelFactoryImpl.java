/**
 */
package org.but4reuse.variantsmodel.impl;

import org.but4reuse.variantsmodel.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class VariantsModelFactoryImpl extends EFactoryImpl implements VariantsModelFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static VariantsModelFactory init() {
		try {
			VariantsModelFactory theVariantsModelFactory = (VariantsModelFactory)EPackage.Registry.INSTANCE.getEFactory(VariantsModelPackage.eNS_URI);
			if (theVariantsModelFactory != null) {
				return theVariantsModelFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new VariantsModelFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VariantsModelFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case VariantsModelPackage.VARIANTS_MODEL: return createVariantsModel();
			case VariantsModelPackage.VARIANT: return createVariant();
			case VariantsModelPackage.COMPOSED_VARIANT: return createComposedVariant();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VariantsModel createVariantsModel() {
		VariantsModelImpl variantsModel = new VariantsModelImpl();
		return variantsModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Variant createVariant() {
		VariantImpl variant = new VariantImpl();
		return variant;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComposedVariant createComposedVariant() {
		ComposedVariantImpl composedVariant = new ComposedVariantImpl();
		return composedVariant;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VariantsModelPackage getVariantsModelPackage() {
		return (VariantsModelPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static VariantsModelPackage getPackage() {
		return VariantsModelPackage.eINSTANCE;
	}

} //VariantsModelFactoryImpl
