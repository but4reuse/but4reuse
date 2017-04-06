/**
 */
package org.but4reuse.artefactmodel;

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
 * @see org.but4reuse.artefactmodel.ArtefactModelFactory
 * @model kind="package"
 * @generated
 */
public interface ArtefactModelPackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "artefactmodel";

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://org.but4reuse.artefact.model";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "org.but4reuse.artefact.model";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	ArtefactModelPackage eINSTANCE = org.but4reuse.artefactmodel.impl.ArtefactModelPackageImpl.init();

	/**
	 * The meta object id for the '
	 * {@link org.but4reuse.artefactmodel.impl.ArtefactModelImpl
	 * <em>Artefact Model</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.but4reuse.artefactmodel.impl.ArtefactModelImpl
	 * @see org.but4reuse.artefactmodel.impl.ArtefactModelPackageImpl#getArtefactModel()
	 * @generated
	 */
	int ARTEFACT_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ARTEFACT_MODEL__NAME = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ARTEFACT_MODEL__DESCRIPTION = 1;

	/**
	 * The feature id for the '<em><b>Adapters</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ARTEFACT_MODEL__ADAPTERS = 2;

	/**
	 * The feature id for the '<em><b>Owned Artefacts</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ARTEFACT_MODEL__OWNED_ARTEFACTS = 3;

	/**
	 * The number of structural features of the '<em>Artefact Model</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ARTEFACT_MODEL_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Artefact Model</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ARTEFACT_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '
	 * {@link org.but4reuse.artefactmodel.impl.ArtefactImpl <em>Artefact</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.but4reuse.artefactmodel.impl.ArtefactImpl
	 * @see org.but4reuse.artefactmodel.impl.ArtefactModelPackageImpl#getArtefact()
	 * @generated
	 */
	int ARTEFACT = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ARTEFACT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Artefact URI</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ARTEFACT__ARTEFACT_URI = 1;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ARTEFACT__DESCRIPTION = 2;

	/**
	 * The feature id for the '<em><b>Active</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ARTEFACT__ACTIVE = 3;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ARTEFACT__DATE = 4;

	/**
	 * The number of structural features of the '<em>Artefact</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ARTEFACT_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Artefact</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ARTEFACT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '
	 * {@link org.but4reuse.artefactmodel.impl.ComposedArtefactImpl
	 * <em>Composed Artefact</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.but4reuse.artefactmodel.impl.ComposedArtefactImpl
	 * @see org.but4reuse.artefactmodel.impl.ArtefactModelPackageImpl#getComposedArtefact()
	 * @generated
	 */
	int COMPOSED_ARTEFACT = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSED_ARTEFACT__NAME = ARTEFACT__NAME;

	/**
	 * The feature id for the '<em><b>Artefact URI</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSED_ARTEFACT__ARTEFACT_URI = ARTEFACT__ARTEFACT_URI;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSED_ARTEFACT__DESCRIPTION = ARTEFACT__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Active</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSED_ARTEFACT__ACTIVE = ARTEFACT__ACTIVE;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSED_ARTEFACT__DATE = ARTEFACT__DATE;

	/**
	 * The feature id for the '<em><b>Owned Artefacts</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSED_ARTEFACT__OWNED_ARTEFACTS = ARTEFACT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Composed Artefact</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSED_ARTEFACT_FEATURE_COUNT = ARTEFACT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Composed Artefact</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSED_ARTEFACT_OPERATION_COUNT = ARTEFACT_OPERATION_COUNT + 0;

	/**
	 * Returns the meta object for class '
	 * {@link org.but4reuse.artefactmodel.ArtefactModel <em>Artefact Model</em>}
	 * '. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Artefact Model</em>'.
	 * @see org.but4reuse.artefactmodel.ArtefactModel
	 * @generated
	 */
	EClass getArtefactModel();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.but4reuse.artefactmodel.ArtefactModel#getName <em>Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.but4reuse.artefactmodel.ArtefactModel#getName()
	 * @see #getArtefactModel()
	 * @generated
	 */
	EAttribute getArtefactModel_Name();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.but4reuse.artefactmodel.ArtefactModel#getDescription
	 * <em>Description</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.but4reuse.artefactmodel.ArtefactModel#getDescription()
	 * @see #getArtefactModel()
	 * @generated
	 */
	EAttribute getArtefactModel_Description();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.but4reuse.artefactmodel.ArtefactModel#getAdapters
	 * <em>Adapters</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Adapters</em>'.
	 * @see org.but4reuse.artefactmodel.ArtefactModel#getAdapters()
	 * @see #getArtefactModel()
	 * @generated
	 */
	EAttribute getArtefactModel_Adapters();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.but4reuse.artefactmodel.ArtefactModel#getOwnedArtefacts
	 * <em>Owned Artefacts</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Owned Artefacts</em>'.
	 * @see org.but4reuse.artefactmodel.ArtefactModel#getOwnedArtefacts()
	 * @see #getArtefactModel()
	 * @generated
	 */
	EReference getArtefactModel_OwnedArtefacts();

	/**
	 * Returns the meta object for class '
	 * {@link org.but4reuse.artefactmodel.Artefact <em>Artefact</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Artefact</em>'.
	 * @see org.but4reuse.artefactmodel.Artefact
	 * @generated
	 */
	EClass getArtefact();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.but4reuse.artefactmodel.Artefact#getName <em>Name</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.but4reuse.artefactmodel.Artefact#getName()
	 * @see #getArtefact()
	 * @generated
	 */
	EAttribute getArtefact_Name();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.but4reuse.artefactmodel.Artefact#getArtefactURI
	 * <em>Artefact URI</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Artefact URI</em>'.
	 * @see org.but4reuse.artefactmodel.Artefact#getArtefactURI()
	 * @see #getArtefact()
	 * @generated
	 */
	EAttribute getArtefact_ArtefactURI();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.but4reuse.artefactmodel.Artefact#getDescription
	 * <em>Description</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.but4reuse.artefactmodel.Artefact#getDescription()
	 * @see #getArtefact()
	 * @generated
	 */
	EAttribute getArtefact_Description();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.but4reuse.artefactmodel.Artefact#isActive <em>Active</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Active</em>'.
	 * @see org.but4reuse.artefactmodel.Artefact#isActive()
	 * @see #getArtefact()
	 * @generated
	 */
	EAttribute getArtefact_Active();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.but4reuse.artefactmodel.Artefact#getDate <em>Date</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see org.but4reuse.artefactmodel.Artefact#getDate()
	 * @see #getArtefact()
	 * @generated
	 */
	EAttribute getArtefact_Date();

	/**
	 * Returns the meta object for class '
	 * {@link org.but4reuse.artefactmodel.ComposedArtefact
	 * <em>Composed Artefact</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for class '<em>Composed Artefact</em>'.
	 * @see org.but4reuse.artefactmodel.ComposedArtefact
	 * @generated
	 */
	EClass getComposedArtefact();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.but4reuse.artefactmodel.ComposedArtefact#getOwnedArtefacts
	 * <em>Owned Artefacts</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Owned Artefacts</em>'.
	 * @see org.but4reuse.artefactmodel.ComposedArtefact#getOwnedArtefacts()
	 * @see #getComposedArtefact()
	 * @generated
	 */
	EReference getComposedArtefact_OwnedArtefacts();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ArtefactModelFactory getArtefactModelFactory();

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
		 * {@link org.but4reuse.artefactmodel.impl.ArtefactModelImpl
		 * <em>Artefact Model</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.but4reuse.artefactmodel.impl.ArtefactModelImpl
		 * @see org.but4reuse.artefactmodel.impl.ArtefactModelPackageImpl#getArtefactModel()
		 * @generated
		 */
		EClass ARTEFACT_MODEL = eINSTANCE.getArtefactModel();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ARTEFACT_MODEL__NAME = eINSTANCE.getArtefactModel_Name();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ARTEFACT_MODEL__DESCRIPTION = eINSTANCE.getArtefactModel_Description();

		/**
		 * The meta object literal for the '<em><b>Adapters</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ARTEFACT_MODEL__ADAPTERS = eINSTANCE.getArtefactModel_Adapters();

		/**
		 * The meta object literal for the '<em><b>Owned Artefacts</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ARTEFACT_MODEL__OWNED_ARTEFACTS = eINSTANCE.getArtefactModel_OwnedArtefacts();

		/**
		 * The meta object literal for the '
		 * {@link org.but4reuse.artefactmodel.impl.ArtefactImpl
		 * <em>Artefact</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see org.but4reuse.artefactmodel.impl.ArtefactImpl
		 * @see org.but4reuse.artefactmodel.impl.ArtefactModelPackageImpl#getArtefact()
		 * @generated
		 */
		EClass ARTEFACT = eINSTANCE.getArtefact();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ARTEFACT__NAME = eINSTANCE.getArtefact_Name();

		/**
		 * The meta object literal for the '<em><b>Artefact URI</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ARTEFACT__ARTEFACT_URI = eINSTANCE.getArtefact_ArtefactURI();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ARTEFACT__DESCRIPTION = eINSTANCE.getArtefact_Description();

		/**
		 * The meta object literal for the '<em><b>Active</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ARTEFACT__ACTIVE = eINSTANCE.getArtefact_Active();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ARTEFACT__DATE = eINSTANCE.getArtefact_Date();

		/**
		 * The meta object literal for the '
		 * {@link org.but4reuse.artefactmodel.impl.ComposedArtefactImpl
		 * <em>Composed Artefact</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.but4reuse.artefactmodel.impl.ComposedArtefactImpl
		 * @see org.but4reuse.artefactmodel.impl.ArtefactModelPackageImpl#getComposedArtefact()
		 * @generated
		 */
		EClass COMPOSED_ARTEFACT = eINSTANCE.getComposedArtefact();

		/**
		 * The meta object literal for the '<em><b>Owned Artefacts</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference COMPOSED_ARTEFACT__OWNED_ARTEFACTS = eINSTANCE.getComposedArtefact_OwnedArtefacts();

	}

} // ArtefactModelPackage
