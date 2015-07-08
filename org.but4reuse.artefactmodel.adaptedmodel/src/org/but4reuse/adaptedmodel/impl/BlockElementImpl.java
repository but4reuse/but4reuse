/**
 */
package org.but4reuse.adaptedmodel.impl;

import java.util.Collection;

import org.but4reuse.adaptedmodel.AdaptedModelPackage;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Block Element</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link org.but4reuse.adaptedmodel.impl.BlockElementImpl#getElementWrappers
 * <em>Element Wrappers</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class BlockElementImpl extends MinimalEObjectImpl.Container implements BlockElement {
	/**
	 * The cached value of the '{@link #getElementWrappers()
	 * <em>Element Wrappers</em>}' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getElementWrappers()
	 * @generated
	 * @ordered
	 */
	protected EList<ElementWrapper> elementWrappers;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected BlockElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AdaptedModelPackage.Literals.BLOCK_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<ElementWrapper> getElementWrappers() {
		if (elementWrappers == null) {
			elementWrappers = new EObjectWithInverseResolvingEList.ManyInverse<ElementWrapper>(ElementWrapper.class,
					this, AdaptedModelPackage.BLOCK_ELEMENT__ELEMENT_WRAPPERS,
					AdaptedModelPackage.ELEMENT_WRAPPER__BLOCK_ELEMENTS);
		}
		return elementWrappers;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case AdaptedModelPackage.BLOCK_ELEMENT__ELEMENT_WRAPPERS:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getElementWrappers()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case AdaptedModelPackage.BLOCK_ELEMENT__ELEMENT_WRAPPERS:
			return ((InternalEList<?>) getElementWrappers()).basicRemove(otherEnd, msgs);
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
		case AdaptedModelPackage.BLOCK_ELEMENT__ELEMENT_WRAPPERS:
			return getElementWrappers();
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
		case AdaptedModelPackage.BLOCK_ELEMENT__ELEMENT_WRAPPERS:
			getElementWrappers().clear();
			getElementWrappers().addAll((Collection<? extends ElementWrapper>) newValue);
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
		case AdaptedModelPackage.BLOCK_ELEMENT__ELEMENT_WRAPPERS:
			getElementWrappers().clear();
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
		case AdaptedModelPackage.BLOCK_ELEMENT__ELEMENT_WRAPPERS:
			return elementWrappers != null && !elementWrappers.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // BlockElementImpl
