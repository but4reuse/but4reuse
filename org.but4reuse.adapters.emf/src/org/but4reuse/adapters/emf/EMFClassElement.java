package org.but4reuse.adapters.emf;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.emf.diffmerge.DiffMergeUtils;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.utils.emf.EMFUtils;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;

/**
 * EMF Class Element
 * @author jabier.martinez
 */
public class EMFClassElement extends AbstractElement {

	public EObject owner;
	public EReference reference;
	public EObject eObject;

	
	@Override
	public String getText() {
		return ("Class: " + eObject.eClass().getName() +  " [Text->" + EMFUtils.getText(eObject) + "] [Owner->" + EMFUtils.getText(owner) + "] [Ref->" + reference.getName() + "]");
	}
	
	public boolean construct(URI uri) {
		AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(
				EMFAdapter.ADAPTER_FACTORY, new BasicCommandStack());
		EFactory eFactory = eObject.eClass().getEPackage().getEFactoryInstance();
		EObject newChildEObject = eFactory.create(eObject.eClass());
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
			EMFClassElement targetClassElement = (EMFClassElement)anotherElement;
			if(DiffMergeUtils.isEqualEObject(eObject, targetClassElement.eObject)){
				return 1;
			}
		}
		return 0;
	}

}
