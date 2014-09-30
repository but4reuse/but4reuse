/**
 */
package org.but4reuse.variantsmodel.impl;

import java.util.Collection;

import java.util.Date;
import org.but4reuse.variantsmodel.Variant;
import org.but4reuse.variantsmodel.VariantsModelPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Variant</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.but4reuse.variantsmodel.impl.VariantImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.but4reuse.variantsmodel.impl.VariantImpl#getVariantURI <em>Variant URI</em>}</li>
 *   <li>{@link org.but4reuse.variantsmodel.impl.VariantImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.but4reuse.variantsmodel.impl.VariantImpl#isActive <em>Active</em>}</li>
 *   <li>{@link org.but4reuse.variantsmodel.impl.VariantImpl#getFeatures <em>Features</em>}</li>
 *   <li>{@link org.but4reuse.variantsmodel.impl.VariantImpl#getDate <em>Date</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VariantImpl extends MinimalEObjectImpl.Container implements Variant {
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
	 * The default value of the '{@link #getVariantURI() <em>Variant URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVariantURI()
	 * @generated
	 * @ordered
	 */
	protected static final String VARIANT_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVariantURI() <em>Variant URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVariantURI()
	 * @generated
	 * @ordered
	 */
	protected String variantURI = VARIANT_URI_EDEFAULT;

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
	 * The default value of the '{@link #isActive() <em>Active</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isActive()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ACTIVE_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isActive() <em>Active</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isActive()
	 * @generated
	 * @ordered
	 */
	protected boolean active = ACTIVE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getFeatures() <em>Features</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatures()
	 * @generated
	 * @ordered
	 */
	protected EList<String> features;

	/**
	 * The default value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected static final Date DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected Date date = DATE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VariantImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return VariantsModelPackage.Literals.VARIANT;
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
			eNotify(new ENotificationImpl(this, Notification.SET, VariantsModelPackage.VARIANT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVariantURI() {
		return variantURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVariantURI(String newVariantURI) {
		String oldVariantURI = variantURI;
		variantURI = newVariantURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, VariantsModelPackage.VARIANT__VARIANT_URI, oldVariantURI, variantURI));
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
			eNotify(new ENotificationImpl(this, Notification.SET, VariantsModelPackage.VARIANT__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setActive(boolean newActive) {
		boolean oldActive = active;
		active = newActive;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, VariantsModelPackage.VARIANT__ACTIVE, oldActive, active));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getFeatures() {
		if (features == null) {
			features = new EDataTypeUniqueEList<String>(String.class, this, VariantsModelPackage.VARIANT__FEATURES);
		}
		return features;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDate(Date newDate) {
		Date oldDate = date;
		date = newDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, VariantsModelPackage.VARIANT__DATE, oldDate, date));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case VariantsModelPackage.VARIANT__NAME:
				return getName();
			case VariantsModelPackage.VARIANT__VARIANT_URI:
				return getVariantURI();
			case VariantsModelPackage.VARIANT__DESCRIPTION:
				return getDescription();
			case VariantsModelPackage.VARIANT__ACTIVE:
				return isActive();
			case VariantsModelPackage.VARIANT__FEATURES:
				return getFeatures();
			case VariantsModelPackage.VARIANT__DATE:
				return getDate();
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
			case VariantsModelPackage.VARIANT__NAME:
				setName((String)newValue);
				return;
			case VariantsModelPackage.VARIANT__VARIANT_URI:
				setVariantURI((String)newValue);
				return;
			case VariantsModelPackage.VARIANT__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case VariantsModelPackage.VARIANT__ACTIVE:
				setActive((Boolean)newValue);
				return;
			case VariantsModelPackage.VARIANT__FEATURES:
				getFeatures().clear();
				getFeatures().addAll((Collection<? extends String>)newValue);
				return;
			case VariantsModelPackage.VARIANT__DATE:
				setDate((Date)newValue);
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
			case VariantsModelPackage.VARIANT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case VariantsModelPackage.VARIANT__VARIANT_URI:
				setVariantURI(VARIANT_URI_EDEFAULT);
				return;
			case VariantsModelPackage.VARIANT__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case VariantsModelPackage.VARIANT__ACTIVE:
				setActive(ACTIVE_EDEFAULT);
				return;
			case VariantsModelPackage.VARIANT__FEATURES:
				getFeatures().clear();
				return;
			case VariantsModelPackage.VARIANT__DATE:
				setDate(DATE_EDEFAULT);
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
			case VariantsModelPackage.VARIANT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case VariantsModelPackage.VARIANT__VARIANT_URI:
				return VARIANT_URI_EDEFAULT == null ? variantURI != null : !VARIANT_URI_EDEFAULT.equals(variantURI);
			case VariantsModelPackage.VARIANT__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case VariantsModelPackage.VARIANT__ACTIVE:
				return active != ACTIVE_EDEFAULT;
			case VariantsModelPackage.VARIANT__FEATURES:
				return features != null && !features.isEmpty();
			case VariantsModelPackage.VARIANT__DATE:
				return DATE_EDEFAULT == null ? date != null : !DATE_EDEFAULT.equals(date);
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
		result.append(", variantURI: ");
		result.append(variantURI);
		result.append(", description: ");
		result.append(description);
		result.append(", active: ");
		result.append(active);
		result.append(", features: ");
		result.append(features);
		result.append(", date: ");
		result.append(date);
		result.append(')');
		return result.toString();
	}

} //VariantImpl
