/**
 */
package org.but4reuse.artefactmodel.impl;

import java.util.Collection;

import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.artefactmodel.ArtefactModelPackage;

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
 * <em><b>Artefact Model</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.but4reuse.artefactmodel.impl.ArtefactModelImpl#getName <em>
 * Name</em>}</li>
 * <li>{@link org.but4reuse.artefactmodel.impl.ArtefactModelImpl#getDescription
 * <em>Description</em>}</li>
 * <li>{@link org.but4reuse.artefactmodel.impl.ArtefactModelImpl#getAdapters
 * <em>Adapters</em>}</li>
 * <li>
 * {@link org.but4reuse.artefactmodel.impl.ArtefactModelImpl#getOwnedArtefacts
 * <em>Owned Artefacts</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ArtefactModelImpl extends MinimalEObjectImpl.Container implements ArtefactModel {
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
	 * The default value of the '{@link #getDescription() <em>Description</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getAdapters() <em>Adapters</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAdapters()
	 * @generated
	 * @ordered
	 */
	protected static final String ADAPTERS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAdapters() <em>Adapters</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAdapters()
	 * @generated
	 * @ordered
	 */
	protected String adapters = ADAPTERS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getOwnedArtefacts()
	 * <em>Owned Artefacts</em>}' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOwnedArtefacts()
	 * @generated
	 * @ordered
	 */
	protected EList<Artefact> ownedArtefacts;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ArtefactModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ArtefactModelPackage.Literals.ARTEFACT_MODEL;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ArtefactModelPackage.ARTEFACT_MODEL__NAME, oldName,
					name));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArtefactModelPackage.ARTEFACT_MODEL__DESCRIPTION,
					oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getAdapters() {
		return adapters;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAdapters(String newAdapters) {
		String oldAdapters = adapters;
		adapters = newAdapters;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArtefactModelPackage.ARTEFACT_MODEL__ADAPTERS,
					oldAdapters, adapters));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Artefact> getOwnedArtefacts() {
		if (ownedArtefacts == null) {
			ownedArtefacts = new EObjectContainmentEList<Artefact>(Artefact.class, this,
					ArtefactModelPackage.ARTEFACT_MODEL__OWNED_ARTEFACTS);
		}
		return ownedArtefacts;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ArtefactModelPackage.ARTEFACT_MODEL__OWNED_ARTEFACTS:
			return ((InternalEList<?>) getOwnedArtefacts()).basicRemove(otherEnd, msgs);
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
		case ArtefactModelPackage.ARTEFACT_MODEL__NAME:
			return getName();
		case ArtefactModelPackage.ARTEFACT_MODEL__DESCRIPTION:
			return getDescription();
		case ArtefactModelPackage.ARTEFACT_MODEL__ADAPTERS:
			return getAdapters();
		case ArtefactModelPackage.ARTEFACT_MODEL__OWNED_ARTEFACTS:
			return getOwnedArtefacts();
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
		case ArtefactModelPackage.ARTEFACT_MODEL__NAME:
			setName((String) newValue);
			return;
		case ArtefactModelPackage.ARTEFACT_MODEL__DESCRIPTION:
			setDescription((String) newValue);
			return;
		case ArtefactModelPackage.ARTEFACT_MODEL__ADAPTERS:
			setAdapters((String) newValue);
			return;
		case ArtefactModelPackage.ARTEFACT_MODEL__OWNED_ARTEFACTS:
			getOwnedArtefacts().clear();
			getOwnedArtefacts().addAll((Collection<? extends Artefact>) newValue);
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
		case ArtefactModelPackage.ARTEFACT_MODEL__NAME:
			setName(NAME_EDEFAULT);
			return;
		case ArtefactModelPackage.ARTEFACT_MODEL__DESCRIPTION:
			setDescription(DESCRIPTION_EDEFAULT);
			return;
		case ArtefactModelPackage.ARTEFACT_MODEL__ADAPTERS:
			setAdapters(ADAPTERS_EDEFAULT);
			return;
		case ArtefactModelPackage.ARTEFACT_MODEL__OWNED_ARTEFACTS:
			getOwnedArtefacts().clear();
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
		case ArtefactModelPackage.ARTEFACT_MODEL__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		case ArtefactModelPackage.ARTEFACT_MODEL__DESCRIPTION:
			return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
		case ArtefactModelPackage.ARTEFACT_MODEL__ADAPTERS:
			return ADAPTERS_EDEFAULT == null ? adapters != null : !ADAPTERS_EDEFAULT.equals(adapters);
		case ArtefactModelPackage.ARTEFACT_MODEL__OWNED_ARTEFACTS:
			return ownedArtefacts != null && !ownedArtefacts.isEmpty();
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
		result.append(", description: ");
		result.append(description);
		result.append(", adapters: ");
		result.append(adapters);
		result.append(')');
		return result.toString();
	}

} // ArtefactModelImpl
