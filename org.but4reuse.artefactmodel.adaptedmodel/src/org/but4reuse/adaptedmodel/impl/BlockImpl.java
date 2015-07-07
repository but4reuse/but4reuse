/**
 */
package org.but4reuse.adaptedmodel.impl;

import java.util.Collection;

import org.but4reuse.adaptedmodel.AdaptedModelPackage;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.featurelist.Feature;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Block</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.but4reuse.adaptedmodel.impl.BlockImpl#getOwnedBlockElements
 * <em>Owned Block Elements</em>}</li>
 * <li>
 * {@link org.but4reuse.adaptedmodel.impl.BlockImpl#getCorrespondingFeatures
 * <em>Corresponding Features</em>}</li>
 * <li>{@link org.but4reuse.adaptedmodel.impl.BlockImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class BlockImpl extends MinimalEObjectImpl.Container implements Block {
	/**
	 * The cached value of the '{@link #getOwnedBlockElements()
	 * <em>Owned Block Elements</em>}' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOwnedBlockElements()
	 * @generated
	 * @ordered
	 */
	protected EList<BlockElement> ownedBlockElements;

	/**
	 * The cached value of the '{@link #getCorrespondingFeatures()
	 * <em>Corresponding Features</em>}' reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getCorrespondingFeatures()
	 * @generated
	 * @ordered
	 */
	protected EList<Feature> correspondingFeatures;

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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected BlockImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AdaptedModelPackage.Literals.BLOCK;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<BlockElement> getOwnedBlockElements() {
		if (ownedBlockElements == null) {
			ownedBlockElements = new EObjectContainmentEList<BlockElement>(BlockElement.class, this,
					AdaptedModelPackage.BLOCK__OWNED_BLOCK_ELEMENTS);
		}
		return ownedBlockElements;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Feature> getCorrespondingFeatures() {
		if (correspondingFeatures == null) {
			correspondingFeatures = new EObjectResolvingEList<Feature>(Feature.class, this,
					AdaptedModelPackage.BLOCK__CORRESPONDING_FEATURES);
		}
		return correspondingFeatures;
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
			eNotify(new ENotificationImpl(this, Notification.SET, AdaptedModelPackage.BLOCK__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case AdaptedModelPackage.BLOCK__OWNED_BLOCK_ELEMENTS:
			return ((InternalEList<?>) getOwnedBlockElements()).basicRemove(otherEnd, msgs);
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
		case AdaptedModelPackage.BLOCK__OWNED_BLOCK_ELEMENTS:
			return getOwnedBlockElements();
		case AdaptedModelPackage.BLOCK__CORRESPONDING_FEATURES:
			return getCorrespondingFeatures();
		case AdaptedModelPackage.BLOCK__NAME:
			return getName();
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
		case AdaptedModelPackage.BLOCK__OWNED_BLOCK_ELEMENTS:
			getOwnedBlockElements().clear();
			getOwnedBlockElements().addAll((Collection<? extends BlockElement>) newValue);
			return;
		case AdaptedModelPackage.BLOCK__CORRESPONDING_FEATURES:
			getCorrespondingFeatures().clear();
			getCorrespondingFeatures().addAll((Collection<? extends Feature>) newValue);
			return;
		case AdaptedModelPackage.BLOCK__NAME:
			setName((String) newValue);
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
		case AdaptedModelPackage.BLOCK__OWNED_BLOCK_ELEMENTS:
			getOwnedBlockElements().clear();
			return;
		case AdaptedModelPackage.BLOCK__CORRESPONDING_FEATURES:
			getCorrespondingFeatures().clear();
			return;
		case AdaptedModelPackage.BLOCK__NAME:
			setName(NAME_EDEFAULT);
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
		case AdaptedModelPackage.BLOCK__OWNED_BLOCK_ELEMENTS:
			return ownedBlockElements != null && !ownedBlockElements.isEmpty();
		case AdaptedModelPackage.BLOCK__CORRESPONDING_FEATURES:
			return correspondingFeatures != null && !correspondingFeatures.isEmpty();
		case AdaptedModelPackage.BLOCK__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
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
		result.append(')');
		return result.toString();
	}

} // BlockImpl
