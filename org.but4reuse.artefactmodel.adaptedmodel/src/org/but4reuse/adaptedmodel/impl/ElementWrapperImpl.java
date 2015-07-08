/**
 */
package org.but4reuse.adaptedmodel.impl;

import java.util.Collection;

import org.but4reuse.adaptedmodel.AdaptedModelPackage;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Element Wrapper</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.but4reuse.adaptedmodel.impl.ElementWrapperImpl#getElement <em>
 * Element</em>}</li>
 * <li>
 * {@link org.but4reuse.adaptedmodel.impl.ElementWrapperImpl#getBlockElements
 * <em>Block Elements</em>}</li>
 * <li>{@link org.but4reuse.adaptedmodel.impl.ElementWrapperImpl#getText <em>
 * Text</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ElementWrapperImpl extends MinimalEObjectImpl.Container implements ElementWrapper {
	/**
	 * The default value of the '{@link #getElement() <em>Element</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getElement()
	 * @generated
	 * @ordered
	 */
	protected static final Object ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getElement() <em>Element</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getElement()
	 * @generated
	 * @ordered
	 */
	protected Object element = ELEMENT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getBlockElements()
	 * <em>Block Elements</em>}' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getBlockElements()
	 * @generated
	 * @ordered
	 */
	protected EList<BlockElement> blockElements;

	/**
	 * The default value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected static final String TEXT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected String text = TEXT_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ElementWrapperImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AdaptedModelPackage.Literals.ELEMENT_WRAPPER;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Object getElement() {
		return element;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setElement(Object newElement) {
		Object oldElement = element;
		element = newElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdaptedModelPackage.ELEMENT_WRAPPER__ELEMENT,
					oldElement, element));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<BlockElement> getBlockElements() {
		if (blockElements == null) {
			blockElements = new EObjectWithInverseResolvingEList.ManyInverse<BlockElement>(BlockElement.class, this,
					AdaptedModelPackage.ELEMENT_WRAPPER__BLOCK_ELEMENTS,
					AdaptedModelPackage.BLOCK_ELEMENT__ELEMENT_WRAPPERS);
		}
		return blockElements;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getText() {
		return text;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setText(String newText) {
		String oldText = text;
		text = newText;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdaptedModelPackage.ELEMENT_WRAPPER__TEXT, oldText,
					text));
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
		case AdaptedModelPackage.ELEMENT_WRAPPER__BLOCK_ELEMENTS:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getBlockElements()).basicAdd(otherEnd, msgs);
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
		case AdaptedModelPackage.ELEMENT_WRAPPER__BLOCK_ELEMENTS:
			return ((InternalEList<?>) getBlockElements()).basicRemove(otherEnd, msgs);
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
		case AdaptedModelPackage.ELEMENT_WRAPPER__ELEMENT:
			return getElement();
		case AdaptedModelPackage.ELEMENT_WRAPPER__BLOCK_ELEMENTS:
			return getBlockElements();
		case AdaptedModelPackage.ELEMENT_WRAPPER__TEXT:
			return getText();
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
		case AdaptedModelPackage.ELEMENT_WRAPPER__ELEMENT:
			setElement(newValue);
			return;
		case AdaptedModelPackage.ELEMENT_WRAPPER__BLOCK_ELEMENTS:
			getBlockElements().clear();
			getBlockElements().addAll((Collection<? extends BlockElement>) newValue);
			return;
		case AdaptedModelPackage.ELEMENT_WRAPPER__TEXT:
			setText((String) newValue);
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
		case AdaptedModelPackage.ELEMENT_WRAPPER__ELEMENT:
			setElement(ELEMENT_EDEFAULT);
			return;
		case AdaptedModelPackage.ELEMENT_WRAPPER__BLOCK_ELEMENTS:
			getBlockElements().clear();
			return;
		case AdaptedModelPackage.ELEMENT_WRAPPER__TEXT:
			setText(TEXT_EDEFAULT);
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
		case AdaptedModelPackage.ELEMENT_WRAPPER__ELEMENT:
			return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
		case AdaptedModelPackage.ELEMENT_WRAPPER__BLOCK_ELEMENTS:
			return blockElements != null && !blockElements.isEmpty();
		case AdaptedModelPackage.ELEMENT_WRAPPER__TEXT:
			return TEXT_EDEFAULT == null ? text != null : !TEXT_EDEFAULT.equals(text);
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
		result.append(" (element: ");
		result.append(element);
		result.append(", text: ");
		result.append(text);
		result.append(')');
		return result.toString();
	}

} // ElementWrapperImpl
