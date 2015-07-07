/**
 */
package org.but4reuse.artefactmodel.impl;

import java.util.Collection;

import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModelPackage;
import org.but4reuse.artefactmodel.ComposedArtefact;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Composed Artefact</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link org.but4reuse.artefactmodel.impl.ComposedArtefactImpl#getOwnedArtefacts
 * <em>Owned Artefacts</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ComposedArtefactImpl extends ArtefactImpl implements ComposedArtefact {
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
	protected ComposedArtefactImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ArtefactModelPackage.Literals.COMPOSED_ARTEFACT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Artefact> getOwnedArtefacts() {
		if (ownedArtefacts == null) {
			ownedArtefacts = new EObjectContainmentEList<Artefact>(Artefact.class, this,
					ArtefactModelPackage.COMPOSED_ARTEFACT__OWNED_ARTEFACTS);
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
		case ArtefactModelPackage.COMPOSED_ARTEFACT__OWNED_ARTEFACTS:
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
		case ArtefactModelPackage.COMPOSED_ARTEFACT__OWNED_ARTEFACTS:
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
		case ArtefactModelPackage.COMPOSED_ARTEFACT__OWNED_ARTEFACTS:
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
		case ArtefactModelPackage.COMPOSED_ARTEFACT__OWNED_ARTEFACTS:
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
		case ArtefactModelPackage.COMPOSED_ARTEFACT__OWNED_ARTEFACTS:
			return ownedArtefacts != null && !ownedArtefacts.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // ComposedArtefactImpl
