/**
 */
package org.but4reuse.featurelist.impl;

import java.util.Collection;

import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.featurelist.FeatureListPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Feature List</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.but4reuse.featurelist.impl.FeatureListImpl#getName <em>Name
 * </em>}</li>
 * <li>
 * {@link org.but4reuse.featurelist.impl.FeatureListImpl#getExternalFeatureModelURI
 * <em>External Feature Model URI</em>}</li>
 * <li>{@link org.but4reuse.featurelist.impl.FeatureListImpl#getOwnedFeatures
 * <em>Owned Features</em>}</li>
 * <li>{@link org.but4reuse.featurelist.impl.FeatureListImpl#getArtefactModel
 * <em>Artefact Model</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class FeatureListImpl extends MinimalEObjectImpl.Container implements FeatureList {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;
	/**
	 * The default value of the '{@link #getExternalFeatureModelURI()
	 * <em>External Feature Model URI</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getExternalFeatureModelURI()
	 * @generated
	 * @ordered
	 */
	protected static final String EXTERNAL_FEATURE_MODEL_URI_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getExternalFeatureModelURI()
	 * <em>External Feature Model URI</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getExternalFeatureModelURI()
	 * @generated
	 * @ordered
	 */
	protected String externalFeatureModelURI = EXTERNAL_FEATURE_MODEL_URI_EDEFAULT;
	/**
	 * The cached value of the '{@link #getOwnedFeatures()
	 * <em>Owned Features</em>}' containment reference list. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getOwnedFeatures()
	 * @generated
	 * @ordered
	 */
	protected EList<Feature> ownedFeatures;

	/**
	 * The cached value of the '{@link #getArtefactModel()
	 * <em>Artefact Model</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getArtefactModel()
	 * @generated
	 * @ordered
	 */
	protected ArtefactModel artefactModel;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected FeatureListImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FeatureListPackage.Literals.FEATURE_LIST;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FeatureListPackage.FEATURE_LIST__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getExternalFeatureModelURI() {
		return externalFeatureModelURI;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setExternalFeatureModelURI(String newExternalFeatureModelURI) {
		String oldExternalFeatureModelURI = externalFeatureModelURI;
		externalFeatureModelURI = newExternalFeatureModelURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					FeatureListPackage.FEATURE_LIST__EXTERNAL_FEATURE_MODEL_URI, oldExternalFeatureModelURI,
					externalFeatureModelURI));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Feature> getOwnedFeatures() {
		if (ownedFeatures == null) {
			ownedFeatures = new EObjectContainmentEList<Feature>(Feature.class, this,
					FeatureListPackage.FEATURE_LIST__OWNED_FEATURES);
		}
		return ownedFeatures;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ArtefactModel getArtefactModel() {
		if (artefactModel != null && artefactModel.eIsProxy()) {
			InternalEObject oldArtefactModel = (InternalEObject) artefactModel;
			artefactModel = (ArtefactModel) eResolveProxy(oldArtefactModel);
			if (artefactModel != oldArtefactModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							FeatureListPackage.FEATURE_LIST__ARTEFACT_MODEL, oldArtefactModel, artefactModel));
			}
		}
		return artefactModel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ArtefactModel basicGetArtefactModel() {
		return artefactModel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setArtefactModel(ArtefactModel newArtefactModel) {
		ArtefactModel oldArtefactModel = artefactModel;
		artefactModel = newArtefactModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FeatureListPackage.FEATURE_LIST__ARTEFACT_MODEL,
					oldArtefactModel, artefactModel));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case FeatureListPackage.FEATURE_LIST__OWNED_FEATURES:
			return ((InternalEList<?>) getOwnedFeatures()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case FeatureListPackage.FEATURE_LIST__NAME:
			return getName();
		case FeatureListPackage.FEATURE_LIST__EXTERNAL_FEATURE_MODEL_URI:
			return getExternalFeatureModelURI();
		case FeatureListPackage.FEATURE_LIST__OWNED_FEATURES:
			return getOwnedFeatures();
		case FeatureListPackage.FEATURE_LIST__ARTEFACT_MODEL:
			if (resolve)
				return getArtefactModel();
			return basicGetArtefactModel();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case FeatureListPackage.FEATURE_LIST__NAME:
			setName((String) newValue);
			return;
		case FeatureListPackage.FEATURE_LIST__EXTERNAL_FEATURE_MODEL_URI:
			setExternalFeatureModelURI((String) newValue);
			return;
		case FeatureListPackage.FEATURE_LIST__OWNED_FEATURES:
			getOwnedFeatures().clear();
			getOwnedFeatures().addAll((Collection<? extends Feature>) newValue);
			return;
		case FeatureListPackage.FEATURE_LIST__ARTEFACT_MODEL:
			setArtefactModel((ArtefactModel) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case FeatureListPackage.FEATURE_LIST__NAME:
			setName(NAME_EDEFAULT);
			return;
		case FeatureListPackage.FEATURE_LIST__EXTERNAL_FEATURE_MODEL_URI:
			setExternalFeatureModelURI(EXTERNAL_FEATURE_MODEL_URI_EDEFAULT);
			return;
		case FeatureListPackage.FEATURE_LIST__OWNED_FEATURES:
			getOwnedFeatures().clear();
			return;
		case FeatureListPackage.FEATURE_LIST__ARTEFACT_MODEL:
			setArtefactModel((ArtefactModel) null);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case FeatureListPackage.FEATURE_LIST__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		case FeatureListPackage.FEATURE_LIST__EXTERNAL_FEATURE_MODEL_URI:
			return EXTERNAL_FEATURE_MODEL_URI_EDEFAULT == null ? externalFeatureModelURI != null
					: !EXTERNAL_FEATURE_MODEL_URI_EDEFAULT.equals(externalFeatureModelURI);
		case FeatureListPackage.FEATURE_LIST__OWNED_FEATURES:
			return ownedFeatures != null && !ownedFeatures.isEmpty();
		case FeatureListPackage.FEATURE_LIST__ARTEFACT_MODEL:
			return artefactModel != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", externalFeatureModelURI: ");
		result.append(externalFeatureModelURI);
		result.append(')');
		return result.toString();
	}

} // FeatureListImpl
