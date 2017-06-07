package org.but4reuse.adapters.emf;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.emf.diffmerge.DiffMergeUtils;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.utils.emf.EMFUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * EMF Reference
 * 
 * @author jabier.martinez
 */
public class EMFReferenceElement extends AbstractElement {

	public EObject owner;
	public EReference eReference;
	public List<EObject> referenced;
	public IElement ownerElement;
	public List<EMFClassElement> referencedElements;

	@Override
	public String getText() {
		String referenceText = "";
		if (referenced == null || referenced.isEmpty()) {
			referenceText = "Empty";
		} else {
			for (EObject eObject : referenced) {
				referenceText = referenceText + EMFUtils.getText(eObject) + ", ";
			}
			referenceText = referenceText.substring(0, referenceText.length() - ", ".length());
		}
		return "Ref: " + eReference.getName() + " = " + referenceText + " [Owner->" + EMFUtils.getText(owner) + "]";
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof EMFReferenceElement) {
			EMFReferenceElement targetCP = (EMFReferenceElement) anotherElement;
			if (DiffMergeUtils.isEqualEObjectReference(EMFAdapter.getComparisonMethod(), owner, eReference, referenced,
					targetCP.owner, targetCP.eReference, targetCP.referenced)) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public int hashCode() {
		return EMFAdapter.getHashCode(owner);
	}

	@Override
	public List<String> getWords() {
		List<String> words = new ArrayList<String>();
		// empty for the moment
		return words;
	}

}
