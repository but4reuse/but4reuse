package org.but4reuse.adapters.emf;

import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.emf.diffmerge.DiffMergeUtils;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.utils.emf.EMFUtils;
import org.but4reuse.utils.strings.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * EMF Class Element
 * 
 * @author jabier.martinez
 */
public class EMFClassElement extends AbstractElement {

	public EObject owner;
	public EReference reference;
	public EObject eObject;
	public boolean isResource = false;
	public IElement ownerElement;

	@Override
	public String getText() {
		if (isResource) {
			return "Class: " + eObject.eClass().getName() + " [Text->" + EMFUtils.getText(eObject) + "]";
		}
		return "Class: " + eObject.eClass().getName() + " [Text->" + EMFUtils.getText(eObject) + "] [Owner->"
				+ EMFUtils.getText(owner) + ", Ref->" + reference.getName() + "]";
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof EMFClassElement) {
			EMFClassElement targetClassElement = (EMFClassElement) anotherElement;
			if (DiffMergeUtils.isEqualEObject(EMFAdapter.getComparisonMethod(), eObject, targetClassElement.eObject)) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public int hashCode() {
		return EMFAdapter.getHashCode(eObject);
	}

	@Override
	public List<String> getWords() {
		String className = EMFUtils.getText(eObject);
		return StringUtils.tokenizeString(className);
	}

}
