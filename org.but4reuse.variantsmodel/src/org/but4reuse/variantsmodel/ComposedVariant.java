/**
 */
package org.but4reuse.variantsmodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Composed Variant</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.but4reuse.variantsmodel.ComposedVariant#getOwnedVariants <em>Owned Variants</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.but4reuse.variantsmodel.VariantsModelPackage#getComposedVariant()
 * @model
 * @generated
 */
public interface ComposedVariant extends Variant {
	/**
	 * Returns the value of the '<em><b>Owned Variants</b></em>' containment reference list.
	 * The list contents are of type {@link org.but4reuse.variantsmodel.Variant}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owned Variants</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Variants</em>' containment reference list.
	 * @see org.but4reuse.variantsmodel.VariantsModelPackage#getComposedVariant_OwnedVariants()
	 * @model containment="true"
	 * @generated
	 */
	EList<Variant> getOwnedVariants();

} // ComposedVariant
