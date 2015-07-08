/**
 */
package org.but4reuse.adaptedmodel.impl;

import java.util.Collection;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.AdaptedModelPackage;
import org.but4reuse.adaptedmodel.Block;
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
 * <em><b>Adapted Model</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.but4reuse.adaptedmodel.impl.AdaptedModelImpl#getOwnedBlocks
 * <em>Owned Blocks</em>}</li>
 * <li>
 * {@link org.but4reuse.adaptedmodel.impl.AdaptedModelImpl#getOwnedAdaptedArtefacts
 * <em>Owned Adapted Artefacts</em>}</li>
 * <li>{@link org.but4reuse.adaptedmodel.impl.AdaptedModelImpl#getConstraints
 * <em>Constraints</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class AdaptedModelImpl extends MinimalEObjectImpl.Container implements AdaptedModel {
	/**
	 * The cached value of the '{@link #getOwnedBlocks() <em>Owned Blocks</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOwnedBlocks()
	 * @generated
	 * @ordered
	 */
	protected EList<Block> ownedBlocks;

	/**
	 * The cached value of the '{@link #getOwnedAdaptedArtefacts()
	 * <em>Owned Adapted Artefacts</em>}' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOwnedAdaptedArtefacts()
	 * @generated
	 * @ordered
	 */
	protected EList<AdaptedArtefact> ownedAdaptedArtefacts;

	/**
	 * The default value of the '{@link #getConstraints() <em>Constraints</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getConstraints()
	 * @generated
	 * @ordered
	 */
	protected static final Object CONSTRAINTS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getConstraints() <em>Constraints</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getConstraints()
	 * @generated
	 * @ordered
	 */
	protected Object constraints = CONSTRAINTS_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected AdaptedModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AdaptedModelPackage.Literals.ADAPTED_MODEL;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Block> getOwnedBlocks() {
		if (ownedBlocks == null) {
			ownedBlocks = new EObjectContainmentEList<Block>(Block.class, this,
					AdaptedModelPackage.ADAPTED_MODEL__OWNED_BLOCKS);
		}
		return ownedBlocks;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<AdaptedArtefact> getOwnedAdaptedArtefacts() {
		if (ownedAdaptedArtefacts == null) {
			ownedAdaptedArtefacts = new EObjectContainmentEList<AdaptedArtefact>(AdaptedArtefact.class, this,
					AdaptedModelPackage.ADAPTED_MODEL__OWNED_ADAPTED_ARTEFACTS);
		}
		return ownedAdaptedArtefacts;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Object getConstraints() {
		return constraints;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setConstraints(Object newConstraints) {
		Object oldConstraints = constraints;
		constraints = newConstraints;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdaptedModelPackage.ADAPTED_MODEL__CONSTRAINTS,
					oldConstraints, constraints));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case AdaptedModelPackage.ADAPTED_MODEL__OWNED_BLOCKS:
			return ((InternalEList<?>) getOwnedBlocks()).basicRemove(otherEnd, msgs);
		case AdaptedModelPackage.ADAPTED_MODEL__OWNED_ADAPTED_ARTEFACTS:
			return ((InternalEList<?>) getOwnedAdaptedArtefacts()).basicRemove(otherEnd, msgs);
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
		case AdaptedModelPackage.ADAPTED_MODEL__OWNED_BLOCKS:
			return getOwnedBlocks();
		case AdaptedModelPackage.ADAPTED_MODEL__OWNED_ADAPTED_ARTEFACTS:
			return getOwnedAdaptedArtefacts();
		case AdaptedModelPackage.ADAPTED_MODEL__CONSTRAINTS:
			return getConstraints();
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
		case AdaptedModelPackage.ADAPTED_MODEL__OWNED_BLOCKS:
			getOwnedBlocks().clear();
			getOwnedBlocks().addAll((Collection<? extends Block>) newValue);
			return;
		case AdaptedModelPackage.ADAPTED_MODEL__OWNED_ADAPTED_ARTEFACTS:
			getOwnedAdaptedArtefacts().clear();
			getOwnedAdaptedArtefacts().addAll((Collection<? extends AdaptedArtefact>) newValue);
			return;
		case AdaptedModelPackage.ADAPTED_MODEL__CONSTRAINTS:
			setConstraints(newValue);
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
		case AdaptedModelPackage.ADAPTED_MODEL__OWNED_BLOCKS:
			getOwnedBlocks().clear();
			return;
		case AdaptedModelPackage.ADAPTED_MODEL__OWNED_ADAPTED_ARTEFACTS:
			getOwnedAdaptedArtefacts().clear();
			return;
		case AdaptedModelPackage.ADAPTED_MODEL__CONSTRAINTS:
			setConstraints(CONSTRAINTS_EDEFAULT);
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
		case AdaptedModelPackage.ADAPTED_MODEL__OWNED_BLOCKS:
			return ownedBlocks != null && !ownedBlocks.isEmpty();
		case AdaptedModelPackage.ADAPTED_MODEL__OWNED_ADAPTED_ARTEFACTS:
			return ownedAdaptedArtefacts != null && !ownedAdaptedArtefacts.isEmpty();
		case AdaptedModelPackage.ADAPTED_MODEL__CONSTRAINTS:
			return CONSTRAINTS_EDEFAULT == null ? constraints != null : !CONSTRAINTS_EDEFAULT.equals(constraints);
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
		result.append(" (constraints: ");
		result.append(constraints);
		result.append(')');
		return result.toString();
	}

} // AdaptedModelImpl
