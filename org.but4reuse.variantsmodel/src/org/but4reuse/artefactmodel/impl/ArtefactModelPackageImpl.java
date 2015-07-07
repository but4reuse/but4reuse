/**
 */
package org.but4reuse.artefactmodel.impl;

import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.artefactmodel.ArtefactModelFactory;
import org.but4reuse.artefactmodel.ArtefactModelPackage;
import org.but4reuse.artefactmodel.ComposedArtefact;
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
public class ArtefactModelPackageImpl extends EPackageImpl implements ArtefactModelPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass artefactModelEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass artefactEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass composedArtefactEClass = null;

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
	 * @see org.but4reuse.artefactmodel.ArtefactModelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ArtefactModelPackageImpl() {
		super(eNS_URI, ArtefactModelFactory.eINSTANCE);
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
	 * This method is used to initialize {@link ArtefactModelPackage#eINSTANCE}
	 * when that field is accessed. Clients should not invoke it directly.
	 * Instead, they should simply access that field to obtain the package. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ArtefactModelPackage init() {
		if (isInited)
			return (ArtefactModelPackage) EPackage.Registry.INSTANCE.getEPackage(ArtefactModelPackage.eNS_URI);

		// Obtain or create and register package
		ArtefactModelPackageImpl theArtefactModelPackage = (ArtefactModelPackageImpl) (EPackage.Registry.INSTANCE
				.get(eNS_URI) instanceof ArtefactModelPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
				: new ArtefactModelPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theArtefactModelPackage.createPackageContents();

		// Initialize created meta-data
		theArtefactModelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theArtefactModelPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ArtefactModelPackage.eNS_URI, theArtefactModelPackage);
		return theArtefactModelPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getArtefactModel() {
		return artefactModelEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getArtefactModel_Name() {
		return (EAttribute) artefactModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getArtefactModel_Description() {
		return (EAttribute) artefactModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getArtefactModel_Adapters() {
		return (EAttribute) artefactModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getArtefactModel_OwnedArtefacts() {
		return (EReference) artefactModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getArtefact() {
		return artefactEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getArtefact_Name() {
		return (EAttribute) artefactEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getArtefact_ArtefactURI() {
		return (EAttribute) artefactEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getArtefact_Description() {
		return (EAttribute) artefactEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getArtefact_Active() {
		return (EAttribute) artefactEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getArtefact_Date() {
		return (EAttribute) artefactEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getComposedArtefact() {
		return composedArtefactEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getComposedArtefact_OwnedArtefacts() {
		return (EReference) composedArtefactEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ArtefactModelFactory getArtefactModelFactory() {
		return (ArtefactModelFactory) getEFactoryInstance();
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
		artefactModelEClass = createEClass(ARTEFACT_MODEL);
		createEAttribute(artefactModelEClass, ARTEFACT_MODEL__NAME);
		createEAttribute(artefactModelEClass, ARTEFACT_MODEL__DESCRIPTION);
		createEAttribute(artefactModelEClass, ARTEFACT_MODEL__ADAPTERS);
		createEReference(artefactModelEClass, ARTEFACT_MODEL__OWNED_ARTEFACTS);

		artefactEClass = createEClass(ARTEFACT);
		createEAttribute(artefactEClass, ARTEFACT__NAME);
		createEAttribute(artefactEClass, ARTEFACT__ARTEFACT_URI);
		createEAttribute(artefactEClass, ARTEFACT__DESCRIPTION);
		createEAttribute(artefactEClass, ARTEFACT__ACTIVE);
		createEAttribute(artefactEClass, ARTEFACT__DATE);

		composedArtefactEClass = createEClass(COMPOSED_ARTEFACT);
		createEReference(composedArtefactEClass, COMPOSED_ARTEFACT__OWNED_ARTEFACTS);
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

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		composedArtefactEClass.getESuperTypes().add(this.getArtefact());

		// Initialize classes, features, and operations; add parameters
		initEClass(artefactModelEClass, ArtefactModel.class, "ArtefactModel", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getArtefactModel_Name(), ecorePackage.getEString(), "name", null, 0, 1, ArtefactModel.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getArtefactModel_Description(), ecorePackage.getEString(), "description", null, 0, 1,
				ArtefactModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getArtefactModel_Adapters(), ecorePackage.getEString(), "adapters", null, 0, 1,
				ArtefactModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getArtefactModel_OwnedArtefacts(), this.getArtefact(), null, "ownedArtefacts", null, 0, -1,
				ArtefactModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(artefactEClass, Artefact.class, "Artefact", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getArtefact_Name(), ecorePackage.getEString(), "name", null, 0, 1, Artefact.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getArtefact_ArtefactURI(), ecorePackage.getEString(), "artefactURI", null, 0, 1, Artefact.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getArtefact_Description(), ecorePackage.getEString(), "description", null, 0, 1, Artefact.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getArtefact_Active(), ecorePackage.getEBoolean(), "active", "true", 0, 1, Artefact.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getArtefact_Date(), ecorePackage.getEDate(), "date", null, 0, 1, Artefact.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(composedArtefactEClass, ComposedArtefact.class, "ComposedArtefact", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getComposedArtefact_OwnedArtefacts(), this.getArtefact(), null, "ownedArtefacts", null, 0, -1,
				ComposedArtefact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} // ArtefactModelPackageImpl
