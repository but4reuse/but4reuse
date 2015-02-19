package org.but4reuse.adapters.emf;

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
		return "Reference: [Owner->" + EMFUtils.getText(owner) + "] [Ref->" + this.eReference.getName()
				+ "] [Value->" + referenceText + "]";
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof EMFReferenceElement) {
			EMFReferenceElement targetCP = (EMFReferenceElement) anotherElement;
			if (DiffMergeUtils.isEqualEObjectReference(owner, eReference, referenced, targetCP.owner,
					targetCP.eReference, targetCP.referenced)) {
				return 1;
			}
		}
		return 0;
	}

}
