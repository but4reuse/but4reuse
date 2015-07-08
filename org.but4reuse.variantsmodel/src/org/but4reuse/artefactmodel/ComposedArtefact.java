/**
 */
package org.but4reuse.artefactmodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Composed Artefact</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.but4reuse.artefactmodel.ComposedArtefact#getOwnedArtefacts
 * <em>Owned Artefacts</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.but4reuse.artefactmodel.ArtefactModelPackage#getComposedArtefact()
 * @model
 * @generated
 */
public interface ComposedArtefact extends Artefact {
	/**
	 * Returns the value of the '<em><b>Owned Artefacts</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.but4reuse.artefactmodel.Artefact}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owned Artefacts</em>' containment reference
	 * list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Owned Artefacts</em>' containment reference
	 *         list.
	 * @see org.but4reuse.artefactmodel.ArtefactModelPackage#getComposedArtefact_OwnedArtefacts()
	 * @model containment="true"
	 * @generated
	 */
	EList<Artefact> getOwnedArtefacts();

} // ComposedArtefact
