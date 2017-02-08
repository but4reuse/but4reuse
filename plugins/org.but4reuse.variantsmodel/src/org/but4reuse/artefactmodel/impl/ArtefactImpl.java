/**
 */
package org.but4reuse.artefactmodel.impl;

import java.util.Date;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModelPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Artefact</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.but4reuse.artefactmodel.impl.ArtefactImpl#getName <em>Name
 * </em>}</li>
 * <li>{@link org.but4reuse.artefactmodel.impl.ArtefactImpl#getArtefactURI <em>
 * Artefact URI</em>}</li>
 * <li>{@link org.but4reuse.artefactmodel.impl.ArtefactImpl#getDescription <em>
 * Description</em>}</li>
 * <li>{@link org.but4reuse.artefactmodel.impl.ArtefactImpl#isActive <em>Active
 * </em>}</li>
 * <li>{@link org.but4reuse.artefactmodel.impl.ArtefactImpl#getDate <em>Date
 * </em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ArtefactImpl extends MinimalEObjectImpl.Container implements Artefact {
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
	 * The default value of the '{@link #getArtefactURI() <em>Artefact URI</em>}
	 * ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getArtefactURI()
	 * @generated
	 * @ordered
	 */
	protected static final String ARTEFACT_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getArtefactURI() <em>Artefact URI</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getArtefactURI()
	 * @generated
	 * @ordered
	 */
	protected String artefactURI = ARTEFACT_URI_EDEFAULT;

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
	 * The default value of the '{@link #isActive() <em>Active</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isActive()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ACTIVE_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isActive() <em>Active</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isActive()
	 * @generated
	 * @ordered
	 */
	protected boolean active = ACTIVE_EDEFAULT;

	/**
	 * The default value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected static final Date DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected Date date = DATE_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ArtefactImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ArtefactModelPackage.Literals.ARTEFACT;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ArtefactModelPackage.ARTEFACT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getArtefactURI() {
		return artefactURI;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setArtefactURI(String newArtefactURI) {
		String oldArtefactURI = artefactURI;
		artefactURI = newArtefactURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArtefactModelPackage.ARTEFACT__ARTEFACT_URI,
					oldArtefactURI, artefactURI));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ArtefactModelPackage.ARTEFACT__DESCRIPTION,
					oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setActive(boolean newActive) {
		boolean oldActive = active;
		active = newActive;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArtefactModelPackage.ARTEFACT__ACTIVE, oldActive,
					active));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDate(Date newDate) {
		Date oldDate = date;
		date = newDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArtefactModelPackage.ARTEFACT__DATE, oldDate, date));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ArtefactModelPackage.ARTEFACT__NAME:
			return getName();
		case ArtefactModelPackage.ARTEFACT__ARTEFACT_URI:
			return getArtefactURI();
		case ArtefactModelPackage.ARTEFACT__DESCRIPTION:
			return getDescription();
		case ArtefactModelPackage.ARTEFACT__ACTIVE:
			return isActive();
		case ArtefactModelPackage.ARTEFACT__DATE:
			return getDate();
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
		case ArtefactModelPackage.ARTEFACT__NAME:
			setName((String) newValue);
			return;
		case ArtefactModelPackage.ARTEFACT__ARTEFACT_URI:
			setArtefactURI((String) newValue);
			return;
		case ArtefactModelPackage.ARTEFACT__DESCRIPTION:
			setDescription((String) newValue);
			return;
		case ArtefactModelPackage.ARTEFACT__ACTIVE:
			setActive((Boolean) newValue);
			return;
		case ArtefactModelPackage.ARTEFACT__DATE:
			setDate((Date) newValue);
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
		case ArtefactModelPackage.ARTEFACT__NAME:
			setName(NAME_EDEFAULT);
			return;
		case ArtefactModelPackage.ARTEFACT__ARTEFACT_URI:
			setArtefactURI(ARTEFACT_URI_EDEFAULT);
			return;
		case ArtefactModelPackage.ARTEFACT__DESCRIPTION:
			setDescription(DESCRIPTION_EDEFAULT);
			return;
		case ArtefactModelPackage.ARTEFACT__ACTIVE:
			setActive(ACTIVE_EDEFAULT);
			return;
		case ArtefactModelPackage.ARTEFACT__DATE:
			setDate(DATE_EDEFAULT);
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
		case ArtefactModelPackage.ARTEFACT__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		case ArtefactModelPackage.ARTEFACT__ARTEFACT_URI:
			return ARTEFACT_URI_EDEFAULT == null ? artefactURI != null : !ARTEFACT_URI_EDEFAULT.equals(artefactURI);
		case ArtefactModelPackage.ARTEFACT__DESCRIPTION:
			return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
		case ArtefactModelPackage.ARTEFACT__ACTIVE:
			return active != ACTIVE_EDEFAULT;
		case ArtefactModelPackage.ARTEFACT__DATE:
			return DATE_EDEFAULT == null ? date != null : !DATE_EDEFAULT.equals(date);
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
		result.append(", artefactURI: ");
		result.append(artefactURI);
		result.append(", description: ");
		result.append(description);
		result.append(", active: ");
		result.append(active);
		result.append(", date: ");
		result.append(date);
		result.append(')');
		return result.toString();
	}

} // ArtefactImpl
