/**
 */
package org.but4reuse.variantsmodel.impl;

import java.util.Collection;

import org.but4reuse.variantsmodel.Variant;
import org.but4reuse.variantsmodel.VariantsModel;
import org.but4reuse.variantsmodel.VariantsModelPackage;

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
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Variants Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.but4reuse.variantsmodel.impl.VariantsModelImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.but4reuse.variantsmodel.impl.VariantsModelImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.but4reuse.variantsmodel.impl.VariantsModelImpl#getOwnedVariants <em>Owned Variants</em>}</li>
 *   <li>{@link org.but4reuse.variantsmodel.impl.VariantsModelImpl#getFeatureModelURI <em>Feature Model URI</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VariantsModelImpl extends MinimalEObjectImpl.Container implements VariantsModel {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getOwnedVariants() <em>Owned Variants</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedVariants()
	 * @generated
	 * @ordered
	 */
	protected EList<Variant> ownedVariants;

	/**
	 * The default value of the '{@link #getFeatureModelURI() <em>Feature Model URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatureModelURI()
	 * @generated
	 * @ordered
	 */
	protected static final String FEATURE_MODEL_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFeatureModelURI() <em>Feature Model URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatureModelURI()
	 * @generated
	 * @ordered
	 */
	protected String featureModelURI = FEATURE_MODEL_URI_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VariantsModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return VariantsModelPackage.Literals.VARIANTS_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, VariantsModelPackage.VARIANTS_MODEL__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, VariantsModelPackage.VARIANTS_MODEL__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Variant> getOwnedVariants() {
		if (ownedVariants == null) {
			ownedVariants = new EObjectContainmentEList<Variant>(Variant.class, this, VariantsModelPackage.VARIANTS_MODEL__OWNED_VARIANTS);
		}
		return ownedVariants;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFeatureModelURI() {
		return featureModelURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFeatureModelURI(String newFeatureModelURI) {
		String oldFeatureModelURI = featureModelURI;
		featureModelURI = newFeatureModelURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, VariantsModelPackage.VARIANTS_MODEL__FEATURE_MODEL_URI, oldFeatureModelURI, featureModelURI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case VariantsModelPackage.VARIANTS_MODEL__OWNED_VARIANTS:
				return ((InternalEList<?>)getOwnedVariants()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case VariantsModelPackage.VARIANTS_MODEL__NAME:
				return getName();
			case VariantsModelPackage.VARIANTS_MODEL__DESCRIPTION:
				return getDescription();
			case VariantsModelPackage.VARIANTS_MODEL__OWNED_VARIANTS:
				return getOwnedVariants();
			case VariantsModelPackage.VARIANTS_MODEL__FEATURE_MODEL_URI:
				return getFeatureModelURI();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case VariantsModelPackage.VARIANTS_MODEL__NAME:
				setName((String)newValue);
				return;
			case VariantsModelPackage.VARIANTS_MODEL__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case VariantsModelPackage.VARIANTS_MODEL__OWNED_VARIANTS:
				getOwnedVariants().clear();
				getOwnedVariants().addAll((Collection<? extends Variant>)newValue);
				return;
			case VariantsModelPackage.VARIANTS_MODEL__FEATURE_MODEL_URI:
				setFeatureModelURI((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case VariantsModelPackage.VARIANTS_MODEL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case VariantsModelPackage.VARIANTS_MODEL__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case VariantsModelPackage.VARIANTS_MODEL__OWNED_VARIANTS:
				getOwnedVariants().clear();
				return;
			case VariantsModelPackage.VARIANTS_MODEL__FEATURE_MODEL_URI:
				setFeatureModelURI(FEATURE_MODEL_URI_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case VariantsModelPackage.VARIANTS_MODEL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case VariantsModelPackage.VARIANTS_MODEL__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case VariantsModelPackage.VARIANTS_MODEL__OWNED_VARIANTS:
				return ownedVariants != null && !ownedVariants.isEmpty();
			case VariantsModelPackage.VARIANTS_MODEL__FEATURE_MODEL_URI:
				return FEATURE_MODEL_URI_EDEFAULT == null ? featureModelURI != null : !FEATURE_MODEL_URI_EDEFAULT.equals(featureModelURI);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", description: ");
		result.append(description);
		result.append(", featureModelURI: ");
		result.append(featureModelURI);
		result.append(')');
		return result.toString();
	}

} //VariantsModelImpl
