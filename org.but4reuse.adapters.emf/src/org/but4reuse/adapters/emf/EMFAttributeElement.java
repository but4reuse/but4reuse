package org.but4reuse.adapters.emf;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.emf.diffmerge.DiffMergeUtils;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.utils.emf.EMFUtils;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * EMF Attribute
 * @author jabier.martinez
 */
public class EMFAttributeElement extends AbstractElement {

	public EAttribute eAttribute;
	public EObject owner;
	public Object value;

	@Override
	public String getText() {
		return "Attribute: [Owner->"  + EMFUtils.getName(owner) + "] [Atr->" + this.eAttribute.getName() +  "] [Value->" + value + "]";
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof EMFAttributeElement){
			EMFAttributeElement targetCP = (EMFAttributeElement)anotherElement;
			if(DiffMergeUtils.isEqualEObjectAttribute(owner, eAttribute, value, targetCP.owner, targetCP.eAttribute, targetCP.value)){
				return 1;
			}
		}
		return 0;
	}
	
}
