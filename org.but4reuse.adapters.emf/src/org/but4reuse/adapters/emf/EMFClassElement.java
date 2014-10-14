package org.but4reuse.adapters.emf;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;

/**
 * EMF Child CP
 * @author jabier.martinez
 */
public class EMFClassElement extends AbstractElement {

	public EObject owner;
	public EReference reference;
	public EObject childEObject;

	
	@Override
	public String getText() {
		return ("Class: " + childEObject.eClass().getName() + " [Owner->" + EMFAdapter.getName(owner) + "] [Ref->" + reference.getName() + "]");
	}
	
	public boolean construct(URI uri) {
		AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(
				EMFAdapter.ADAPTER_FACTORY, new BasicCommandStack());
		EFactory eFactory = childEObject.eClass().getEPackage().getEFactoryInstance();
		EObject newChildEObject = eFactory.create(childEObject.eClass());
		Command command = null;
		// TODO check cardinalities
		if (reference.isMany()) {
			command = AddCommand.create(domain, owner, reference, newChildEObject);
		} else {
			command = SetCommand.create(domain, owner, reference, newChildEObject);
		}
		if (command!=null && command.canExecute()){
			domain.getCommandStack().execute(command);
		} else {
			return false;
		}
		try {
			owner.eResource().save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof EMFClassElement){
			EMFClassElement targetCP = (EMFClassElement)anotherElement;
			// TODO
			return 0;
			//if(DiffMergeUtils.isEqualEObject(childEObject, targetCP.childEObject)){
			//	return 1;
			//}
		}
		return 0;
	}

}
