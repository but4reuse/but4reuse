/**
 */
package org.but4reuse.adaptedmodel.impl;

import java.util.Collection;
import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModelPackage;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.artefactmodel.Artefact;
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
 * <em><b>Adapted Artefact</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.but4reuse.adaptedmodel.impl.AdaptedArtefactImpl#getArtefact
 * <em>Artefact</em>}</li>
 * <li>
 * {@link org.but4reuse.adaptedmodel.impl.AdaptedArtefactImpl#getOwnedElementWrappers
 * <em>Owned Element Wrappers</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class AdaptedArtefactImpl extends MinimalEObjectImpl.Container implements AdaptedArtefact {
	/**
	 * The cached value of the '{@link #getArtefact() <em>Artefact</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getArtefact()
	 * @generated
	 * @ordered
	 */
	protected Artefact artefact;

	/**
	 * The cached value of the '{@link #getOwnedElementWrappers()
	 * <em>Owned Element Wrappers</em>}' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOwnedElementWrappers()
	 * @generated
	 * @ordered
	 */
	protected EList<ElementWrapper> ownedElementWrappers;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected AdaptedArtefactImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AdaptedModelPackage.Literals.ADAPTED_ARTEFACT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Artefact getArtefact() {
		if (artefact != null && artefact.eIsProxy()) {
			InternalEObject oldArtefact = (InternalEObject) artefact;
			artefact = (Artefact) eResolveProxy(oldArtefact);
			if (artefact != oldArtefact) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							AdaptedModelPackage.ADAPTED_ARTEFACT__ARTEFACT, oldArtefact, artefact));
			}
		}
		return artefact;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Artefact basicGetArtefact() {
		return artefact;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setArtefact(Artefact newArtefact) {
		Artefact oldArtefact = artefact;
		artefact = newArtefact;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdaptedModelPackage.ADAPTED_ARTEFACT__ARTEFACT,
					oldArtefact, artefact));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<ElementWrapper> getOwnedElementWrappers() {
		if (ownedElementWrappers == null) {
			ownedElementWrappers = new EObjectContainmentEList<ElementWrapper>(ElementWrapper.class, this,
					AdaptedModelPackage.ADAPTED_ARTEFACT__OWNED_ELEMENT_WRAPPERS);
		}
		return ownedElementWrappers;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case AdaptedModelPackage.ADAPTED_ARTEFACT__OWNED_ELEMENT_WRAPPERS:
			return ((InternalEList<?>) getOwnedElementWrappers()).basicRemove(otherEnd, msgs);
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
		case AdaptedModelPackage.ADAPTED_ARTEFACT__ARTEFACT:
			if (resolve)
				return getArtefact();
			return basicGetArtefact();
		case AdaptedModelPackage.ADAPTED_ARTEFACT__OWNED_ELEMENT_WRAPPERS:
			return getOwnedElementWrappers();
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
		case AdaptedModelPackage.ADAPTED_ARTEFACT__ARTEFACT:
			setArtefact((Artefact) newValue);
			return;
		case AdaptedModelPackage.ADAPTED_ARTEFACT__OWNED_ELEMENT_WRAPPERS:
			getOwnedElementWrappers().clear();
			getOwnedElementWrappers().addAll((Collection<? extends ElementWrapper>) newValue);
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
		case AdaptedModelPackage.ADAPTED_ARTEFACT__ARTEFACT:
			setArtefact((Artefact) null);
			return;
		case AdaptedModelPackage.ADAPTED_ARTEFACT__OWNED_ELEMENT_WRAPPERS:
			getOwnedElementWrappers().clear();
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
		case AdaptedModelPackage.ADAPTED_ARTEFACT__ARTEFACT:
			return artefact != null;
		case AdaptedModelPackage.ADAPTED_ARTEFACT__OWNED_ELEMENT_WRAPPERS:
			return ownedElementWrappers != null && !ownedElementWrappers.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // AdaptedArtefactImpl
