/**
 */
package org.but4reuse.featurelist.impl;

import java.util.Collection;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureListPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Feature</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.but4reuse.featurelist.impl.FeatureImpl#getId <em>Id</em>}</li>
 * <li>{@link org.but4reuse.featurelist.impl.FeatureImpl#getName <em>Name</em>}</li>
 * <li>
 * {@link org.but4reuse.featurelist.impl.FeatureImpl#getImplementedInArtefacts
 * <em>Implemented In Artefacts</em>}</li>
 * <li>{@link org.but4reuse.featurelist.impl.FeatureImpl#getDescription <em>
 * Description</em>}</li>
 * <li>{@link org.but4reuse.featurelist.impl.FeatureImpl#getNegationFeatureOf
 * <em>Negation Feature Of</em>}</li>
 * <li>
 * {@link org.but4reuse.featurelist.impl.FeatureImpl#getInteractionFeatureOf
 * <em>Interaction Feature Of</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class FeatureImpl extends MinimalEObjectImpl.Container implements Feature {
	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

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
	 * The cached value of the '{@link #getImplementedInArtefacts()
	 * <em>Implemented In Artefacts</em>}' reference list. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getImplementedInArtefacts()
	 * @generated
	 * @ordered
	 */
	protected EList<Artefact> implementedInArtefacts;

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
	 * The cached value of the '{@link #getNegationFeatureOf()
	 * <em>Negation Feature Of</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getNegationFeatureOf()
	 * @generated
	 * @ordered
	 */
	protected Feature negationFeatureOf;

	/**
	 * The cached value of the '{@link #getInteractionFeatureOf()
	 * <em>Interaction Feature Of</em>}' reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getInteractionFeatureOf()
	 * @generated
	 * @ordered
	 */
	protected EList<Feature> interactionFeatureOf;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected FeatureImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FeatureListPackage.Literals.FEATURE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FeatureListPackage.FEATURE__ID, oldId, id));
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
			eNotify(new ENotificationImpl(this, Notification.SET, FeatureListPackage.FEATURE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Artefact> getImplementedInArtefacts() {
		if (implementedInArtefacts == null) {
			implementedInArtefacts = new EObjectResolvingEList<Artefact>(Artefact.class, this,
					FeatureListPackage.FEATURE__IMPLEMENTED_IN_ARTEFACTS);
		}
		return implementedInArtefacts;
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
			eNotify(new ENotificationImpl(this, Notification.SET, FeatureListPackage.FEATURE__DESCRIPTION,
					oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Feature getNegationFeatureOf() {
		if (negationFeatureOf != null && negationFeatureOf.eIsProxy()) {
			InternalEObject oldNegationFeatureOf = (InternalEObject) negationFeatureOf;
			negationFeatureOf = (Feature) eResolveProxy(oldNegationFeatureOf);
			if (negationFeatureOf != oldNegationFeatureOf) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							FeatureListPackage.FEATURE__NEGATION_FEATURE_OF, oldNegationFeatureOf, negationFeatureOf));
			}
		}
		return negationFeatureOf;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Feature basicGetNegationFeatureOf() {
		return negationFeatureOf;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setNegationFeatureOf(Feature newNegationFeatureOf) {
		Feature oldNegationFeatureOf = negationFeatureOf;
		negationFeatureOf = newNegationFeatureOf;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FeatureListPackage.FEATURE__NEGATION_FEATURE_OF,
					oldNegationFeatureOf, negationFeatureOf));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Feature> getInteractionFeatureOf() {
		if (interactionFeatureOf == null) {
			interactionFeatureOf = new EObjectResolvingEList<Feature>(Feature.class, this,
					FeatureListPackage.FEATURE__INTERACTION_FEATURE_OF);
		}
		return interactionFeatureOf;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case FeatureListPackage.FEATURE__ID:
			return getId();
		case FeatureListPackage.FEATURE__NAME:
			return getName();
		case FeatureListPackage.FEATURE__IMPLEMENTED_IN_ARTEFACTS:
			return getImplementedInArtefacts();
		case FeatureListPackage.FEATURE__DESCRIPTION:
			return getDescription();
		case FeatureListPackage.FEATURE__NEGATION_FEATURE_OF:
			if (resolve)
				return getNegationFeatureOf();
			return basicGetNegationFeatureOf();
		case FeatureListPackage.FEATURE__INTERACTION_FEATURE_OF:
			return getInteractionFeatureOf();
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
		case FeatureListPackage.FEATURE__ID:
			setId((String) newValue);
			return;
		case FeatureListPackage.FEATURE__NAME:
			setName((String) newValue);
			return;
		case FeatureListPackage.FEATURE__IMPLEMENTED_IN_ARTEFACTS:
			getImplementedInArtefacts().clear();
			getImplementedInArtefacts().addAll((Collection<? extends Artefact>) newValue);
			return;
		case FeatureListPackage.FEATURE__DESCRIPTION:
			setDescription((String) newValue);
			return;
		case FeatureListPackage.FEATURE__NEGATION_FEATURE_OF:
			setNegationFeatureOf((Feature) newValue);
			return;
		case FeatureListPackage.FEATURE__INTERACTION_FEATURE_OF:
			getInteractionFeatureOf().clear();
			getInteractionFeatureOf().addAll((Collection<? extends Feature>) newValue);
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
		case FeatureListPackage.FEATURE__ID:
			setId(ID_EDEFAULT);
			return;
		case FeatureListPackage.FEATURE__NAME:
			setName(NAME_EDEFAULT);
			return;
		case FeatureListPackage.FEATURE__IMPLEMENTED_IN_ARTEFACTS:
			getImplementedInArtefacts().clear();
			return;
		case FeatureListPackage.FEATURE__DESCRIPTION:
			setDescription(DESCRIPTION_EDEFAULT);
			return;
		case FeatureListPackage.FEATURE__NEGATION_FEATURE_OF:
			setNegationFeatureOf((Feature) null);
			return;
		case FeatureListPackage.FEATURE__INTERACTION_FEATURE_OF:
			getInteractionFeatureOf().clear();
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
		case FeatureListPackage.FEATURE__ID:
			return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
		case FeatureListPackage.FEATURE__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		case FeatureListPackage.FEATURE__IMPLEMENTED_IN_ARTEFACTS:
			return implementedInArtefacts != null && !implementedInArtefacts.isEmpty();
		case FeatureListPackage.FEATURE__DESCRIPTION:
			return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
		case FeatureListPackage.FEATURE__NEGATION_FEATURE_OF:
			return negationFeatureOf != null;
		case FeatureListPackage.FEATURE__INTERACTION_FEATURE_OF:
			return interactionFeatureOf != null && !interactionFeatureOf.isEmpty();
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
		result.append(" (id: ");
		result.append(id);
		result.append(", name: ");
		result.append(name);
		result.append(", description: ");
		result.append(description);
		result.append(')');
		return result.toString();
	}

} // FeatureImpl
