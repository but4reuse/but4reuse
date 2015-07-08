/**
 */
package org.but4reuse.adaptedmodel.impl;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.AdaptedModelFactory;
import org.but4reuse.adaptedmodel.AdaptedModelPackage;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.artefactmodel.ArtefactModelPackage;
import org.but4reuse.featurelist.FeatureListPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class AdaptedModelPackageImpl extends EPackageImpl implements AdaptedModelPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass adaptedModelEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass adaptedArtefactEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass elementWrapperEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass blockEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass blockElementEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the
	 * package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory
	 * method {@link #init init()}, which also performs initialization of the
	 * package, or returns the registered package, if one already exists. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.but4reuse.adaptedmodel.AdaptedModelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private AdaptedModelPackageImpl() {
		super(eNS_URI, AdaptedModelFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model,
	 * and for any others upon which it depends.
	 * 
	 * <p>
	 * This method is used to initialize {@link AdaptedModelPackage#eINSTANCE}
	 * when that field is accessed. Clients should not invoke it directly.
	 * Instead, they should simply access that field to obtain the package. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static AdaptedModelPackage init() {
		if (isInited)
			return (AdaptedModelPackage) EPackage.Registry.INSTANCE.getEPackage(AdaptedModelPackage.eNS_URI);

		// Obtain or create and register package
		AdaptedModelPackageImpl theAdaptedModelPackage = (AdaptedModelPackageImpl) (EPackage.Registry.INSTANCE
				.get(eNS_URI) instanceof AdaptedModelPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
				: new AdaptedModelPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		FeatureListPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theAdaptedModelPackage.createPackageContents();

		// Initialize created meta-data
		theAdaptedModelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theAdaptedModelPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(AdaptedModelPackage.eNS_URI, theAdaptedModelPackage);
		return theAdaptedModelPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getAdaptedModel() {
		return adaptedModelEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAdaptedModel_OwnedBlocks() {
		return (EReference) adaptedModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAdaptedModel_OwnedAdaptedArtefacts() {
		return (EReference) adaptedModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAdaptedModel_Constraints() {
		return (EAttribute) adaptedModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getAdaptedArtefact() {
		return adaptedArtefactEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAdaptedArtefact_Artefact() {
		return (EReference) adaptedArtefactEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAdaptedArtefact_OwnedElementWrappers() {
		return (EReference) adaptedArtefactEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getElementWrapper() {
		return elementWrapperEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getElementWrapper_Element() {
		return (EAttribute) elementWrapperEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getElementWrapper_BlockElements() {
		return (EReference) elementWrapperEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getElementWrapper_Text() {
		return (EAttribute) elementWrapperEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getBlock() {
		return blockEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getBlock_OwnedBlockElements() {
		return (EReference) blockEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getBlock_CorrespondingFeatures() {
		return (EReference) blockEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getBlock_Name() {
		return (EAttribute) blockEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getBlockElement() {
		return blockElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getBlockElement_ElementWrappers() {
		return (EReference) blockElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AdaptedModelFactory getAdaptedModelFactory() {
		return (AdaptedModelFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is guarded to
	 * have no affect on any invocation but its first. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		adaptedModelEClass = createEClass(ADAPTED_MODEL);
		createEReference(adaptedModelEClass, ADAPTED_MODEL__OWNED_BLOCKS);
		createEReference(adaptedModelEClass, ADAPTED_MODEL__OWNED_ADAPTED_ARTEFACTS);
		createEAttribute(adaptedModelEClass, ADAPTED_MODEL__CONSTRAINTS);

		adaptedArtefactEClass = createEClass(ADAPTED_ARTEFACT);
		createEReference(adaptedArtefactEClass, ADAPTED_ARTEFACT__ARTEFACT);
		createEReference(adaptedArtefactEClass, ADAPTED_ARTEFACT__OWNED_ELEMENT_WRAPPERS);

		elementWrapperEClass = createEClass(ELEMENT_WRAPPER);
		createEAttribute(elementWrapperEClass, ELEMENT_WRAPPER__ELEMENT);
		createEReference(elementWrapperEClass, ELEMENT_WRAPPER__BLOCK_ELEMENTS);
		createEAttribute(elementWrapperEClass, ELEMENT_WRAPPER__TEXT);

		blockEClass = createEClass(BLOCK);
		createEReference(blockEClass, BLOCK__OWNED_BLOCK_ELEMENTS);
		createEReference(blockEClass, BLOCK__CORRESPONDING_FEATURES);
		createEAttribute(blockEClass, BLOCK__NAME);

		blockElementEClass = createEClass(BLOCK_ELEMENT);
		createEReference(blockElementEClass, BLOCK_ELEMENT__ELEMENT_WRAPPERS);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This
	 * method is guarded to have no affect on any invocation but its first. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		ArtefactModelPackage theArtefactModelPackage = (ArtefactModelPackage) EPackage.Registry.INSTANCE
				.getEPackage(ArtefactModelPackage.eNS_URI);
		FeatureListPackage theFeatureListPackage = (FeatureListPackage) EPackage.Registry.INSTANCE
				.getEPackage(FeatureListPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(adaptedModelEClass, AdaptedModel.class, "AdaptedModel", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAdaptedModel_OwnedBlocks(), this.getBlock(), null, "ownedBlocks", null, 0, -1,
				AdaptedModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAdaptedModel_OwnedAdaptedArtefacts(), this.getAdaptedArtefact(), null,
				"ownedAdaptedArtefacts", null, 0, -1, AdaptedModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAdaptedModel_Constraints(), ecorePackage.getEJavaObject(), "constraints", null, 0, 1,
				AdaptedModel.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEClass(adaptedArtefactEClass, AdaptedArtefact.class, "AdaptedArtefact", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAdaptedArtefact_Artefact(), theArtefactModelPackage.getArtefact(), null, "artefact", null, 0,
				1, AdaptedArtefact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAdaptedArtefact_OwnedElementWrappers(), this.getElementWrapper(), null,
				"ownedElementWrappers", null, 0, -1, AdaptedArtefact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(elementWrapperEClass, ElementWrapper.class, "ElementWrapper", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getElementWrapper_Element(), ecorePackage.getEJavaObject(), "element", null, 0, 1,
				ElementWrapper.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getElementWrapper_BlockElements(), this.getBlockElement(),
				this.getBlockElement_ElementWrappers(), "blockElements", null, 0, -1, ElementWrapper.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElementWrapper_Text(), ecorePackage.getEString(), "text", null, 0, 1, ElementWrapper.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(blockEClass, Block.class, "Block", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBlock_OwnedBlockElements(), this.getBlockElement(), null, "ownedBlockElements", null, 0, -1,
				Block.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBlock_CorrespondingFeatures(), theFeatureListPackage.getFeature(), null,
				"correspondingFeatures", null, 0, -1, Block.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBlock_Name(), ecorePackage.getEString(), "name", null, 0, 1, Block.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(blockElementEClass, BlockElement.class, "BlockElement", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBlockElement_ElementWrappers(), this.getElementWrapper(),
				this.getElementWrapper_BlockElements(), "elementWrappers", null, 0, -1, BlockElement.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} // AdaptedModelPackageImpl
