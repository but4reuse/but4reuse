/**
 */
package org.but4reuse.variantsmodel.impl;

import java.util.Collection;

import org.but4reuse.variantsmodel.ComposedVariant;
import org.but4reuse.variantsmodel.Variant;
import org.but4reuse.variantsmodel.VariantsModelPackage;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Composed Variant</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.but4reuse.variantsmodel.impl.ComposedVariantImpl#getOwnedVariants <em>Owned Variants</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ComposedVariantImpl extends VariantImpl implements ComposedVariant {
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComposedVariantImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return VariantsModelPackage.Literals.COMPOSED_VARIANT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Variant> getOwnedVariants() {
		if (ownedVariants == null) {
			ownedVariants = new EObjectContainmentEList<Variant>(Variant.class, this, VariantsModelPackage.COMPOSED_VARIANT__OWNED_VARIANTS);
		}
		return ownedVariants;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case VariantsModelPackage.COMPOSED_VARIANT__OWNED_VARIANTS:
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
			case VariantsModelPackage.COMPOSED_VARIANT__OWNED_VARIANTS:
				return getOwnedVariants();
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
			case VariantsModelPackage.COMPOSED_VARIANT__OWNED_VARIANTS:
				getOwnedVariants().clear();
				getOwnedVariants().addAll((Collection<? extends Variant>)newValue);
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
			case VariantsModelPackage.COMPOSED_VARIANT__OWNED_VARIANTS:
				getOwnedVariants().clear();
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
			case VariantsModelPackage.COMPOSED_VARIANT__OWNED_VARIANTS:
				return ownedVariants != null && !ownedVariants.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ComposedVariantImpl
