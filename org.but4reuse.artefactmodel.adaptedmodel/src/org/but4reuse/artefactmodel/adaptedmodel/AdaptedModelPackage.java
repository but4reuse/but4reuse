/**
 */
package org.but4reuse.artefactmodel.adaptedmodel;

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
 * @see org.but4reuse.artefactmodel.adaptedmodel.AdaptedModelFactory
 * @model kind="package"
 * @generated
 */
public interface AdaptedModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "adaptedmodel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://org.but4reuse.artefact.model.adapted.model";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.but4reuse.artefact.model.adapted.model";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AdaptedModelPackage eINSTANCE = org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedModelImpl <em>Adapted Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedModelImpl
	 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedModelPackageImpl#getAdaptedModel()
	 * @generated
	 */
	int ADAPTED_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Owned Blocks</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADAPTED_MODEL__OWNED_BLOCKS = 0;

	/**
	 * The feature id for the '<em><b>Owned Adapted Artefacts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADAPTED_MODEL__OWNED_ADAPTED_ARTEFACTS = 1;

	/**
	 * The number of structural features of the '<em>Adapted Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADAPTED_MODEL_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Adapted Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADAPTED_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedArtefactImpl <em>Adapted Artefact</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedArtefactImpl
	 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedModelPackageImpl#getAdaptedArtefact()
	 * @generated
	 */
	int ADAPTED_ARTEFACT = 1;

	/**
	 * The feature id for the '<em><b>Artefact</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADAPTED_ARTEFACT__ARTEFACT = 0;

	/**
	 * The feature id for the '<em><b>Owned Element Wrappers</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADAPTED_ARTEFACT__OWNED_ELEMENT_WRAPPERS = 1;

	/**
	 * The number of structural features of the '<em>Adapted Artefact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADAPTED_ARTEFACT_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Adapted Artefact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADAPTED_ARTEFACT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.but4reuse.artefactmodel.adaptedmodel.impl.ElementWrapperImpl <em>Element Wrapper</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.ElementWrapperImpl
	 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedModelPackageImpl#getElementWrapper()
	 * @generated
	 */
	int ELEMENT_WRAPPER = 2;

	/**
	 * The feature id for the '<em><b>Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_WRAPPER__ELEMENT = 0;

	/**
	 * The feature id for the '<em><b>Block Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_WRAPPER__BLOCK_ELEMENTS = 1;

	/**
	 * The number of structural features of the '<em>Element Wrapper</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_WRAPPER_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Element Wrapper</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_WRAPPER_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.but4reuse.artefactmodel.adaptedmodel.impl.BlockImpl <em>Block</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.BlockImpl
	 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedModelPackageImpl#getBlock()
	 * @generated
	 */
	int BLOCK = 3;

	/**
	 * The feature id for the '<em><b>Owned Block Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLOCK__OWNED_BLOCK_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Corresponding Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLOCK__CORRESPONDING_FEATURE = 1;

	/**
	 * The number of structural features of the '<em>Block</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLOCK_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Block</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLOCK_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.but4reuse.artefactmodel.adaptedmodel.impl.BlockElementImpl <em>Block Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.BlockElementImpl
	 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedModelPackageImpl#getBlockElement()
	 * @generated
	 */
	int BLOCK_ELEMENT = 4;

	/**
	 * The feature id for the '<em><b>Element Wrappers</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLOCK_ELEMENT__ELEMENT_WRAPPERS = 0;

	/**
	 * The number of structural features of the '<em>Block Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLOCK_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Block Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLOCK_ELEMENT_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link org.but4reuse.artefactmodel.adaptedmodel.AdaptedModel <em>Adapted Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Adapted Model</em>'.
	 * @see org.but4reuse.artefactmodel.adaptedmodel.AdaptedModel
	 * @generated
	 */
	EClass getAdaptedModel();

	/**
	 * Returns the meta object for the containment reference list '{@link org.but4reuse.artefactmodel.adaptedmodel.AdaptedModel#getOwnedBlocks <em>Owned Blocks</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Owned Blocks</em>'.
	 * @see org.but4reuse.artefactmodel.adaptedmodel.AdaptedModel#getOwnedBlocks()
	 * @see #getAdaptedModel()
	 * @generated
	 */
	EReference getAdaptedModel_OwnedBlocks();

	/**
	 * Returns the meta object for the containment reference list '{@link org.but4reuse.artefactmodel.adaptedmodel.AdaptedModel#getOwnedAdaptedArtefacts <em>Owned Adapted Artefacts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Owned Adapted Artefacts</em>'.
	 * @see org.but4reuse.artefactmodel.adaptedmodel.AdaptedModel#getOwnedAdaptedArtefacts()
	 * @see #getAdaptedModel()
	 * @generated
	 */
	EReference getAdaptedModel_OwnedAdaptedArtefacts();

	/**
	 * Returns the meta object for class '{@link org.but4reuse.artefactmodel.adaptedmodel.AdaptedArtefact <em>Adapted Artefact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Adapted Artefact</em>'.
	 * @see org.but4reuse.artefactmodel.adaptedmodel.AdaptedArtefact
	 * @generated
	 */
	EClass getAdaptedArtefact();

	/**
	 * Returns the meta object for the reference '{@link org.but4reuse.artefactmodel.adaptedmodel.AdaptedArtefact#getArtefact <em>Artefact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Artefact</em>'.
	 * @see org.but4reuse.artefactmodel.adaptedmodel.AdaptedArtefact#getArtefact()
	 * @see #getAdaptedArtefact()
	 * @generated
	 */
	EReference getAdaptedArtefact_Artefact();

	/**
	 * Returns the meta object for the reference list '{@link org.but4reuse.artefactmodel.adaptedmodel.AdaptedArtefact#getOwnedElementWrappers <em>Owned Element Wrappers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Owned Element Wrappers</em>'.
	 * @see org.but4reuse.artefactmodel.adaptedmodel.AdaptedArtefact#getOwnedElementWrappers()
	 * @see #getAdaptedArtefact()
	 * @generated
	 */
	EReference getAdaptedArtefact_OwnedElementWrappers();

	/**
	 * Returns the meta object for class '{@link org.but4reuse.artefactmodel.adaptedmodel.ElementWrapper <em>Element Wrapper</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element Wrapper</em>'.
	 * @see org.but4reuse.artefactmodel.adaptedmodel.ElementWrapper
	 * @generated
	 */
	EClass getElementWrapper();

	/**
	 * Returns the meta object for the attribute '{@link org.but4reuse.artefactmodel.adaptedmodel.ElementWrapper#getElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Element</em>'.
	 * @see org.but4reuse.artefactmodel.adaptedmodel.ElementWrapper#getElement()
	 * @see #getElementWrapper()
	 * @generated
	 */
	EAttribute getElementWrapper_Element();

	/**
	 * Returns the meta object for the reference list '{@link org.but4reuse.artefactmodel.adaptedmodel.ElementWrapper#getBlockElements <em>Block Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Block Elements</em>'.
	 * @see org.but4reuse.artefactmodel.adaptedmodel.ElementWrapper#getBlockElements()
	 * @see #getElementWrapper()
	 * @generated
	 */
	EReference getElementWrapper_BlockElements();

	/**
	 * Returns the meta object for class '{@link org.but4reuse.artefactmodel.adaptedmodel.Block <em>Block</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Block</em>'.
	 * @see org.but4reuse.artefactmodel.adaptedmodel.Block
	 * @generated
	 */
	EClass getBlock();

	/**
	 * Returns the meta object for the containment reference list '{@link org.but4reuse.artefactmodel.adaptedmodel.Block#getOwnedBlockElements <em>Owned Block Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Owned Block Elements</em>'.
	 * @see org.but4reuse.artefactmodel.adaptedmodel.Block#getOwnedBlockElements()
	 * @see #getBlock()
	 * @generated
	 */
	EReference getBlock_OwnedBlockElements();

	/**
	 * Returns the meta object for the reference '{@link org.but4reuse.artefactmodel.adaptedmodel.Block#getCorrespondingFeature <em>Corresponding Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Corresponding Feature</em>'.
	 * @see org.but4reuse.artefactmodel.adaptedmodel.Block#getCorrespondingFeature()
	 * @see #getBlock()
	 * @generated
	 */
	EReference getBlock_CorrespondingFeature();

	/**
	 * Returns the meta object for class '{@link org.but4reuse.artefactmodel.adaptedmodel.BlockElement <em>Block Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Block Element</em>'.
	 * @see org.but4reuse.artefactmodel.adaptedmodel.BlockElement
	 * @generated
	 */
	EClass getBlockElement();

	/**
	 * Returns the meta object for the reference list '{@link org.but4reuse.artefactmodel.adaptedmodel.BlockElement#getElementWrappers <em>Element Wrappers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Element Wrappers</em>'.
	 * @see org.but4reuse.artefactmodel.adaptedmodel.BlockElement#getElementWrappers()
	 * @see #getBlockElement()
	 * @generated
	 */
	EReference getBlockElement_ElementWrappers();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AdaptedModelFactory getAdaptedModelFactory();

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
		 * The meta object literal for the '{@link org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedModelImpl <em>Adapted Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedModelImpl
		 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedModelPackageImpl#getAdaptedModel()
		 * @generated
		 */
		EClass ADAPTED_MODEL = eINSTANCE.getAdaptedModel();

		/**
		 * The meta object literal for the '<em><b>Owned Blocks</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ADAPTED_MODEL__OWNED_BLOCKS = eINSTANCE.getAdaptedModel_OwnedBlocks();

		/**
		 * The meta object literal for the '<em><b>Owned Adapted Artefacts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ADAPTED_MODEL__OWNED_ADAPTED_ARTEFACTS = eINSTANCE.getAdaptedModel_OwnedAdaptedArtefacts();

		/**
		 * The meta object literal for the '{@link org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedArtefactImpl <em>Adapted Artefact</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedArtefactImpl
		 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedModelPackageImpl#getAdaptedArtefact()
		 * @generated
		 */
		EClass ADAPTED_ARTEFACT = eINSTANCE.getAdaptedArtefact();

		/**
		 * The meta object literal for the '<em><b>Artefact</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ADAPTED_ARTEFACT__ARTEFACT = eINSTANCE.getAdaptedArtefact_Artefact();

		/**
		 * The meta object literal for the '<em><b>Owned Element Wrappers</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ADAPTED_ARTEFACT__OWNED_ELEMENT_WRAPPERS = eINSTANCE.getAdaptedArtefact_OwnedElementWrappers();

		/**
		 * The meta object literal for the '{@link org.but4reuse.artefactmodel.adaptedmodel.impl.ElementWrapperImpl <em>Element Wrapper</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.ElementWrapperImpl
		 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedModelPackageImpl#getElementWrapper()
		 * @generated
		 */
		EClass ELEMENT_WRAPPER = eINSTANCE.getElementWrapper();

		/**
		 * The meta object literal for the '<em><b>Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ELEMENT_WRAPPER__ELEMENT = eINSTANCE.getElementWrapper_Element();

		/**
		 * The meta object literal for the '<em><b>Block Elements</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT_WRAPPER__BLOCK_ELEMENTS = eINSTANCE.getElementWrapper_BlockElements();

		/**
		 * The meta object literal for the '{@link org.but4reuse.artefactmodel.adaptedmodel.impl.BlockImpl <em>Block</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.BlockImpl
		 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedModelPackageImpl#getBlock()
		 * @generated
		 */
		EClass BLOCK = eINSTANCE.getBlock();

		/**
		 * The meta object literal for the '<em><b>Owned Block Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BLOCK__OWNED_BLOCK_ELEMENTS = eINSTANCE.getBlock_OwnedBlockElements();

		/**
		 * The meta object literal for the '<em><b>Corresponding Feature</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BLOCK__CORRESPONDING_FEATURE = eINSTANCE.getBlock_CorrespondingFeature();

		/**
		 * The meta object literal for the '{@link org.but4reuse.artefactmodel.adaptedmodel.impl.BlockElementImpl <em>Block Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.BlockElementImpl
		 * @see org.but4reuse.artefactmodel.adaptedmodel.impl.AdaptedModelPackageImpl#getBlockElement()
		 * @generated
		 */
		EClass BLOCK_ELEMENT = eINSTANCE.getBlockElement();

		/**
		 * The meta object literal for the '<em><b>Element Wrappers</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BLOCK_ELEMENT__ELEMENT_WRAPPERS = eINSTANCE.getBlockElement_ElementWrappers();

	}

} //AdaptedModelPackage
