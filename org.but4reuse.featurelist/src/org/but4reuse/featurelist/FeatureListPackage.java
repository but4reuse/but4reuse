/**
 */
package org.but4reuse.featurelist;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains
 * accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each operation of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see org.but4reuse.featurelist.FeatureListFactory
 * @model kind="package"
 * @generated
 */
public interface FeatureListPackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "featurelist";

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://org.but4reuse.feature.list";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "org.but4reuse.feature.list";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	FeatureListPackage eINSTANCE = org.but4reuse.featurelist.impl.FeatureListPackageImpl.init();

	/**
	 * The meta object id for the '
	 * {@link org.but4reuse.featurelist.impl.FeatureListImpl
	 * <em>Feature List</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.but4reuse.featurelist.impl.FeatureListImpl
	 * @see org.but4reuse.featurelist.impl.FeatureListPackageImpl#getFeatureList()
	 * @generated
	 */
	int FEATURE_LIST = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE_LIST__NAME = 0;

	/**
	 * The feature id for the '<em><b>External Feature Model URI</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE_LIST__EXTERNAL_FEATURE_MODEL_URI = 1;

	/**
	 * The feature id for the '<em><b>Owned Features</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE_LIST__OWNED_FEATURES = 2;

	/**
	 * The feature id for the '<em><b>Artefact Model</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE_LIST__ARTEFACT_MODEL = 3;

	/**
	 * The number of structural features of the '<em>Feature List</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE_LIST_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Feature List</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE_LIST_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '
	 * {@link org.but4reuse.featurelist.impl.FeatureImpl <em>Feature</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.but4reuse.featurelist.impl.FeatureImpl
	 * @see org.but4reuse.featurelist.impl.FeatureListPackageImpl#getFeature()
	 * @generated
	 */
	int FEATURE = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE__NAME = 1;

	/**
	 * The feature id for the '<em><b>Implemented In Artefacts</b></em>'
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE__IMPLEMENTED_IN_ARTEFACTS = 2;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE__DESCRIPTION = 3;

	/**
	 * The feature id for the '<em><b>Negation Feature Of</b></em>' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE__NEGATION_FEATURE_OF = 4;

	/**
	 * The feature id for the '<em><b>Interaction Feature Of</b></em>' reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE__INTERACTION_FEATURE_OF = 5;

	/**
	 * The number of structural features of the '<em>Feature</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Feature</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE_OPERATION_COUNT = 0;

	/**
	 * Returns the meta object for class '
	 * {@link org.but4reuse.featurelist.FeatureList <em>Feature List</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Feature List</em>'.
	 * @see org.but4reuse.featurelist.FeatureList
	 * @generated
	 */
	EClass getFeatureList();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.but4reuse.featurelist.FeatureList#getName <em>Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.but4reuse.featurelist.FeatureList#getName()
	 * @see #getFeatureList()
	 * @generated
	 */
	EAttribute getFeatureList_Name();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.but4reuse.featurelist.FeatureList#getExternalFeatureModelURI
	 * <em>External Feature Model URI</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the attribute '
	 *         <em>External Feature Model URI</em>'.
	 * @see org.but4reuse.featurelist.FeatureList#getExternalFeatureModelURI()
	 * @see #getFeatureList()
	 * @generated
	 */
	EAttribute getFeatureList_ExternalFeatureModelURI();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.but4reuse.featurelist.FeatureList#getOwnedFeatures
	 * <em>Owned Features</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Owned Features</em>'.
	 * @see org.but4reuse.featurelist.FeatureList#getOwnedFeatures()
	 * @see #getFeatureList()
	 * @generated
	 */
	EReference getFeatureList_OwnedFeatures();

	/**
	 * Returns the meta object for the reference '
	 * {@link org.but4reuse.featurelist.FeatureList#getArtefactModel
	 * <em>Artefact Model</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Artefact Model</em>'.
	 * @see org.but4reuse.featurelist.FeatureList#getArtefactModel()
	 * @see #getFeatureList()
	 * @generated
	 */
	EReference getFeatureList_ArtefactModel();

	/**
	 * Returns the meta object for class '
	 * {@link org.but4reuse.featurelist.Feature <em>Feature</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Feature</em>'.
	 * @see org.but4reuse.featurelist.Feature
	 * @generated
	 */
	EClass getFeature();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.but4reuse.featurelist.Feature#getId <em>Id</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.but4reuse.featurelist.Feature#getId()
	 * @see #getFeature()
	 * @generated
	 */
	EAttribute getFeature_Id();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.but4reuse.featurelist.Feature#getName <em>Name</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.but4reuse.featurelist.Feature#getName()
	 * @see #getFeature()
	 * @generated
	 */
	EAttribute getFeature_Name();

	/**
	 * Returns the meta object for the reference list '
	 * {@link org.but4reuse.featurelist.Feature#getImplementedInArtefacts
	 * <em>Implemented In Artefacts</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the reference list '
	 *         <em>Implemented In Artefacts</em>'.
	 * @see org.but4reuse.featurelist.Feature#getImplementedInArtefacts()
	 * @see #getFeature()
	 * @generated
	 */
	EReference getFeature_ImplementedInArtefacts();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.but4reuse.featurelist.Feature#getDescription
	 * <em>Description</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.but4reuse.featurelist.Feature#getDescription()
	 * @see #getFeature()
	 * @generated
	 */
	EAttribute getFeature_Description();

	/**
	 * Returns the meta object for the reference '
	 * {@link org.but4reuse.featurelist.Feature#getNegationFeatureOf
	 * <em>Negation Feature Of</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the reference '<em>Negation Feature Of</em>'.
	 * @see org.but4reuse.featurelist.Feature#getNegationFeatureOf()
	 * @see #getFeature()
	 * @generated
	 */
	EReference getFeature_NegationFeatureOf();

	/**
	 * Returns the meta object for the reference list '
	 * {@link org.but4reuse.featurelist.Feature#getInteractionFeatureOf
	 * <em>Interaction Feature Of</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the reference list '
	 *         <em>Interaction Feature Of</em>'.
	 * @see org.but4reuse.featurelist.Feature#getInteractionFeatureOf()
	 * @see #getFeature()
	 * @generated
	 */
	EReference getFeature_InteractionFeatureOf();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	FeatureListFactory getFeatureListFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that
	 * represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each operation of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '
		 * {@link org.but4reuse.featurelist.impl.FeatureListImpl
		 * <em>Feature List</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.but4reuse.featurelist.impl.FeatureListImpl
		 * @see org.but4reuse.featurelist.impl.FeatureListPackageImpl#getFeatureList()
		 * @generated
		 */
		EClass FEATURE_LIST = eINSTANCE.getFeatureList();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FEATURE_LIST__NAME = eINSTANCE.getFeatureList_Name();

		/**
		 * The meta object literal for the '
		 * <em><b>External Feature Model URI</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FEATURE_LIST__EXTERNAL_FEATURE_MODEL_URI = eINSTANCE.getFeatureList_ExternalFeatureModelURI();

		/**
		 * The meta object literal for the '<em><b>Owned Features</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference FEATURE_LIST__OWNED_FEATURES = eINSTANCE.getFeatureList_OwnedFeatures();

		/**
		 * The meta object literal for the '<em><b>Artefact Model</b></em>'
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference FEATURE_LIST__ARTEFACT_MODEL = eINSTANCE.getFeatureList_ArtefactModel();

		/**
		 * The meta object literal for the '
		 * {@link org.but4reuse.featurelist.impl.FeatureImpl <em>Feature</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.but4reuse.featurelist.impl.FeatureImpl
		 * @see org.but4reuse.featurelist.impl.FeatureListPackageImpl#getFeature()
		 * @generated
		 */
		EClass FEATURE = eINSTANCE.getFeature();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FEATURE__ID = eINSTANCE.getFeature_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FEATURE__NAME = eINSTANCE.getFeature_Name();

		/**
		 * The meta object literal for the '
		 * <em><b>Implemented In Artefacts</b></em>' reference list feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference FEATURE__IMPLEMENTED_IN_ARTEFACTS = eINSTANCE.getFeature_ImplementedInArtefacts();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FEATURE__DESCRIPTION = eINSTANCE.getFeature_Description();

		/**
		 * The meta object literal for the '<em><b>Negation Feature Of</b></em>'
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference FEATURE__NEGATION_FEATURE_OF = eINSTANCE.getFeature_NegationFeatureOf();

		/**
		 * The meta object literal for the '
		 * <em><b>Interaction Feature Of</b></em>' reference list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference FEATURE__INTERACTION_FEATURE_OF = eINSTANCE.getFeature_InteractionFeatureOf();

	}

} // FeatureListPackage
