package org.but4reuse.adapters.emf;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.emf.activator.Activator;
import org.but4reuse.adapters.emf.diffmerge.DiffMergeUtils;
import org.but4reuse.adapters.emf.preferences.EMFAdapterPreferencePage;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.utils.emf.EMFUtils;
import org.eclipse.emf.diffmerge.util.ModelImplUtil;
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
		return "Attribute: [Owner->"  + EMFUtils.getText(owner) + "] [Atr->" + this.eAttribute.getName() +  "] [Value->" + value + "]";
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
	
	@Override
	public int hashCode() {
		if (Activator.getDefault().getPreferenceStore().getBoolean(EMFAdapterPreferencePage.XML_ID_HASHING)) {
			String id = ModelImplUtil.getXMLID(owner);
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		} else {
			return super.hashCode();
		}
	}
	
}
