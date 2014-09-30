/**
 */
package org.but4reuse.variantsmodel.impl;

import org.but4reuse.variantsmodel.ComposedVariant;
import org.but4reuse.variantsmodel.Variant;
import org.but4reuse.variantsmodel.VariantsModel;
import org.but4reuse.variantsmodel.VariantsModelFactory;
import org.but4reuse.variantsmodel.VariantsModelPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class VariantsModelPackageImpl extends EPackageImpl implements VariantsModelPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass variantsModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass variantEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass composedVariantEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.but4reuse.variantsmodel.VariantsModelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private VariantsModelPackageImpl() {
		super(eNS_URI, VariantsModelFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link VariantsModelPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static VariantsModelPackage init() {
		if (isInited) return (VariantsModelPackage)EPackage.Registry.INSTANCE.getEPackage(VariantsModelPackage.eNS_URI);

		// Obtain or create and register package
		VariantsModelPackageImpl theVariantsModelPackage = (VariantsModelPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof VariantsModelPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new VariantsModelPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theVariantsModelPackage.createPackageContents();

		// Initialize created meta-data
		theVariantsModelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theVariantsModelPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(VariantsModelPackage.eNS_URI, theVariantsModelPackage);
		return theVariantsModelPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVariantsModel() {
		return variantsModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVariantsModel_Name() {
		return (EAttribute)variantsModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVariantsModel_Description() {
		return (EAttribute)variantsModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVariantsModel_OwnedVariants() {
		return (EReference)variantsModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVariantsModel_FeatureModelURI() {
		return (EAttribute)variantsModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVariant() {
		return variantEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVariant_Name() {
		return (EAttribute)variantEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVariant_VariantURI() {
		return (EAttribute)variantEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVariant_Description() {
		return (EAttribute)variantEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVariant_Active() {
		return (EAttribute)variantEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVariant_Features() {
		return (EAttribute)variantEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVariant_Date() {
		return (EAttribute)variantEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getComposedVariant() {
		return composedVariantEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getComposedVariant_OwnedVariants() {
		return (EReference)composedVariantEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VariantsModelFactory getVariantsModelFactory() {
		return (VariantsModelFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		variantsModelEClass = createEClass(VARIANTS_MODEL);
		createEAttribute(variantsModelEClass, VARIANTS_MODEL__NAME);
		createEAttribute(variantsModelEClass, VARIANTS_MODEL__DESCRIPTION);
		createEReference(variantsModelEClass, VARIANTS_MODEL__OWNED_VARIANTS);
		createEAttribute(variantsModelEClass, VARIANTS_MODEL__FEATURE_MODEL_URI);

		variantEClass = createEClass(VARIANT);
		createEAttribute(variantEClass, VARIANT__NAME);
		createEAttribute(variantEClass, VARIANT__VARIANT_URI);
		createEAttribute(variantEClass, VARIANT__DESCRIPTION);
		createEAttribute(variantEClass, VARIANT__ACTIVE);
		createEAttribute(variantEClass, VARIANT__FEATURES);
		createEAttribute(variantEClass, VARIANT__DATE);

		composedVariantEClass = createEClass(COMPOSED_VARIANT);
		createEReference(composedVariantEClass, COMPOSED_VARIANT__OWNED_VARIANTS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		composedVariantEClass.getESuperTypes().add(this.getVariant());

		// Initialize classes, features, and operations; add parameters
		initEClass(variantsModelEClass, VariantsModel.class, "VariantsModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVariantsModel_Name(), ecorePackage.getEString(), "name", null, 0, 1, VariantsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVariantsModel_Description(), ecorePackage.getEString(), "description", null, 0, 1, VariantsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVariantsModel_OwnedVariants(), this.getVariant(), null, "ownedVariants", null, 0, -1, VariantsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVariantsModel_FeatureModelURI(), ecorePackage.getEString(), "featureModelURI", null, 0, 1, VariantsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(variantEClass, Variant.class, "Variant", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVariant_Name(), ecorePackage.getEString(), "name", null, 0, 1, Variant.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVariant_VariantURI(), ecorePackage.getEString(), "variantURI", null, 0, 1, Variant.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVariant_Description(), ecorePackage.getEString(), "description", null, 0, 1, Variant.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVariant_Active(), ecorePackage.getEBoolean(), "active", "true", 0, 1, Variant.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVariant_Features(), ecorePackage.getEString(), "features", null, 0, -1, Variant.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVariant_Date(), ecorePackage.getEDate(), "date", null, 0, 1, Variant.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(composedVariantEClass, ComposedVariant.class, "ComposedVariant", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getComposedVariant_OwnedVariants(), this.getVariant(), null, "ownedVariants", null, 0, -1, ComposedVariant.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //VariantsModelPackageImpl
