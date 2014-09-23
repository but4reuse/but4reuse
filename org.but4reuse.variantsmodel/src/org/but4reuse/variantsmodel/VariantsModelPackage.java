/**
 */
package org.but4reuse.variantsmodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.but4reuse.variantsmodel.VariantsModelFactory
 * @model kind="package"
 * @generated
 */
public interface VariantsModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "variantsmodel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://org.but4reuse.variants.model";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.but4reuse.variants.model";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	VariantsModelPackage eINSTANCE = org.but4reuse.variantsmodel.impl.VariantsModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.but4reuse.variantsmodel.impl.VariantsModelImpl <em>Variants Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.but4reuse.variantsmodel.impl.VariantsModelImpl
	 * @see org.but4reuse.variantsmodel.impl.VariantsModelPackageImpl#getVariantsModel()
	 * @generated
	 */
	int VARIANTS_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIANTS_MODEL__NAME = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIANTS_MODEL__DESCRIPTION = 1;

	/**
	 * The feature id for the '<em><b>Owned Variants</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIANTS_MODEL__OWNED_VARIANTS = 2;

	/**
	 * The feature id for the '<em><b>Feature Model URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIANTS_MODEL__FEATURE_MODEL_URI = 3;

	/**
	 * The number of structural features of the '<em>Variants Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIANTS_MODEL_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Variants Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIANTS_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.but4reuse.variantsmodel.impl.VariantImpl <em>Variant</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.but4reuse.variantsmodel.impl.VariantImpl
	 * @see org.but4reuse.variantsmodel.impl.VariantsModelPackageImpl#getVariant()
	 * @generated
	 */
	int VARIANT = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIANT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Variant URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIANT__VARIANT_URI = 1;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIANT__DESCRIPTION = 2;

	/**
	 * The feature id for the '<em><b>Active</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIANT__ACTIVE = 3;

	/**
	 * The feature id for the '<em><b>Features</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIANT__FEATURES = 4;

	/**
	 * The number of structural features of the '<em>Variant</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIANT_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Variant</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIANT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.but4reuse.variantsmodel.impl.ComposedVariantImpl <em>Composed Variant</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.but4reuse.variantsmodel.impl.ComposedVariantImpl
	 * @see org.but4reuse.variantsmodel.impl.VariantsModelPackageImpl#getComposedVariant()
	 * @generated
	 */
	int COMPOSED_VARIANT = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSED_VARIANT__NAME = VARIANT__NAME;

	/**
	 * The feature id for the '<em><b>Variant URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSED_VARIANT__VARIANT_URI = VARIANT__VARIANT_URI;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSED_VARIANT__DESCRIPTION = VARIANT__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Active</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSED_VARIANT__ACTIVE = VARIANT__ACTIVE;

	/**
	 * The feature id for the '<em><b>Features</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSED_VARIANT__FEATURES = VARIANT__FEATURES;

	/**
	 * The feature id for the '<em><b>Owned Variants</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSED_VARIANT__OWNED_VARIANTS = VARIANT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Composed Variant</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSED_VARIANT_FEATURE_COUNT = VARIANT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Composed Variant</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSED_VARIANT_OPERATION_COUNT = VARIANT_OPERATION_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link org.but4reuse.variantsmodel.VariantsModel <em>Variants Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Variants Model</em>'.
	 * @see org.but4reuse.variantsmodel.VariantsModel
	 * @generated
	 */
	EClass getVariantsModel();

	/**
	 * Returns the meta object for the attribute '{@link org.but4reuse.variantsmodel.VariantsModel#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.but4reuse.variantsmodel.VariantsModel#getName()
	 * @see #getVariantsModel()
	 * @generated
	 */
	EAttribute getVariantsModel_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.but4reuse.variantsmodel.VariantsModel#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.but4reuse.variantsmodel.VariantsModel#getDescription()
	 * @see #getVariantsModel()
	 * @generated
	 */
	EAttribute getVariantsModel_Description();

	/**
	 * Returns the meta object for the containment reference list '{@link org.but4reuse.variantsmodel.VariantsModel#getOwnedVariants <em>Owned Variants</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Owned Variants</em>'.
	 * @see org.but4reuse.variantsmodel.VariantsModel#getOwnedVariants()
	 * @see #getVariantsModel()
	 * @generated
	 */
	EReference getVariantsModel_OwnedVariants();

	/**
	 * Returns the meta object for the attribute '{@link org.but4reuse.variantsmodel.VariantsModel#getFeatureModelURI <em>Feature Model URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Feature Model URI</em>'.
	 * @see org.but4reuse.variantsmodel.VariantsModel#getFeatureModelURI()
	 * @see #getVariantsModel()
	 * @generated
	 */
	EAttribute getVariantsModel_FeatureModelURI();

	/**
	 * Returns the meta object for class '{@link org.but4reuse.variantsmodel.Variant <em>Variant</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Variant</em>'.
	 * @see org.but4reuse.variantsmodel.Variant
	 * @generated
	 */
	EClass getVariant();

	/**
	 * Returns the meta object for the attribute '{@link org.but4reuse.variantsmodel.Variant#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.but4reuse.variantsmodel.Variant#getName()
	 * @see #getVariant()
	 * @generated
	 */
	EAttribute getVariant_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.but4reuse.variantsmodel.Variant#getVariantURI <em>Variant URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Variant URI</em>'.
	 * @see org.but4reuse.variantsmodel.Variant#getVariantURI()
	 * @see #getVariant()
	 * @generated
	 */
	EAttribute getVariant_VariantURI();

	/**
	 * Returns the meta object for the attribute '{@link org.but4reuse.variantsmodel.Variant#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.but4reuse.variantsmodel.Variant#getDescription()
	 * @see #getVariant()
	 * @generated
	 */
	EAttribute getVariant_Description();

	/**
	 * Returns the meta object for the attribute '{@link org.but4reuse.variantsmodel.Variant#isActive <em>Active</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Active</em>'.
	 * @see org.but4reuse.variantsmodel.Variant#isActive()
	 * @see #getVariant()
	 * @generated
	 */
	EAttribute getVariant_Active();

	/**
	 * Returns the meta object for the attribute list '{@link org.but4reuse.variantsmodel.Variant#getFeatures <em>Features</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Features</em>'.
	 * @see org.but4reuse.variantsmodel.Variant#getFeatures()
	 * @see #getVariant()
	 * @generated
	 */
	EAttribute getVariant_Features();

	/**
	 * Returns the meta object for class '{@link org.but4reuse.variantsmodel.ComposedVariant <em>Composed Variant</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Composed Variant</em>'.
	 * @see org.but4reuse.variantsmodel.ComposedVariant
	 * @generated
	 */
	EClass getComposedVariant();

	/**
	 * Returns the meta object for the containment reference list '{@link org.but4reuse.variantsmodel.ComposedVariant#getOwnedVariants <em>Owned Variants</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Owned Variants</em>'.
	 * @see org.but4reuse.variantsmodel.ComposedVariant#getOwnedVariants()
	 * @see #getComposedVariant()
	 * @generated
	 */
	EReference getComposedVariant_OwnedVariants();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	VariantsModelFactory getVariantsModelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.but4reuse.variantsmodel.impl.VariantsModelImpl <em>Variants Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.but4reuse.variantsmodel.impl.VariantsModelImpl
		 * @see org.but4reuse.variantsmodel.impl.VariantsModelPackageImpl#getVariantsModel()
		 * @generated
		 */
		EClass VARIANTS_MODEL = eINSTANCE.getVariantsModel();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VARIANTS_MODEL__NAME = eINSTANCE.getVariantsModel_Name();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VARIANTS_MODEL__DESCRIPTION = eINSTANCE.getVariantsModel_Description();

		/**
		 * The meta object literal for the '<em><b>Owned Variants</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VARIANTS_MODEL__OWNED_VARIANTS = eINSTANCE.getVariantsModel_OwnedVariants();

		/**
		 * The meta object literal for the '<em><b>Feature Model URI</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VARIANTS_MODEL__FEATURE_MODEL_URI = eINSTANCE.getVariantsModel_FeatureModelURI();

		/**
		 * The meta object literal for the '{@link org.but4reuse.variantsmodel.impl.VariantImpl <em>Variant</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.but4reuse.variantsmodel.impl.VariantImpl
		 * @see org.but4reuse.variantsmodel.impl.VariantsModelPackageImpl#getVariant()
		 * @generated
		 */
		EClass VARIANT = eINSTANCE.getVariant();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VARIANT__NAME = eINSTANCE.getVariant_Name();

		/**
		 * The meta object literal for the '<em><b>Variant URI</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VARIANT__VARIANT_URI = eINSTANCE.getVariant_VariantURI();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VARIANT__DESCRIPTION = eINSTANCE.getVariant_Description();

		/**
		 * The meta object literal for the '<em><b>Active</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VARIANT__ACTIVE = eINSTANCE.getVariant_Active();

		/**
		 * The meta object literal for the '<em><b>Features</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VARIANT__FEATURES = eINSTANCE.getVariant_Features();

		/**
		 * The meta object literal for the '{@link org.but4reuse.variantsmodel.impl.ComposedVariantImpl <em>Composed Variant</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.but4reuse.variantsmodel.impl.ComposedVariantImpl
		 * @see org.but4reuse.variantsmodel.impl.VariantsModelPackageImpl#getComposedVariant()
		 * @generated
		 */
		EClass COMPOSED_VARIANT = eINSTANCE.getComposedVariant();

		/**
		 * The meta object literal for the '<em><b>Owned Variants</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPOSED_VARIANT__OWNED_VARIANTS = eINSTANCE.getComposedVariant_OwnedVariants();

	}

} //VariantsModelPackage
