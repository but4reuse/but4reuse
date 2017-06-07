package org.but4reuse.adapters.emf;

import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.emf.diffmerge.DiffMergeUtils;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.utils.emf.EMFUtils;
import org.but4reuse.utils.strings.StringUtils;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * EMF Attribute
 * 
 * @author jabier.martinez
 */
public class EMFAttributeElement extends AbstractElement {

	public EAttribute eAttribute;
	public EObject owner;
	public IElement ownerElement;
	public Object value;

	@Override
	public String getText() {
		return "Attr: " + eAttribute.getName() + " = " + value + " [Owner->" + EMFUtils.getText(owner) + "]";
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof EMFAttributeElement) {
			EMFAttributeElement targetElement = (EMFAttributeElement) anotherElement;
			if (DiffMergeUtils.isEqualEObjectAttribute(EMFAdapter.getComparisonMethod(), owner, eAttribute, value,
					targetElement.owner, targetElement.eAttribute, targetElement.value)) {
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
		String valueName = value.toString();
		return StringUtils.tokenizeString(valueName);
	}
}
