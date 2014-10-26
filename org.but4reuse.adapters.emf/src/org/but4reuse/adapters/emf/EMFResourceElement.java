package org.but4reuse.adapters.emf;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * EMF Resource
 * 
 * @author jabier.martinez
 */
public class EMFResourceElement extends EMFClassElement {
	
	@Override
	public String getText() {
		return "Class: " + childEObject.eClass().getName();
	}
	
	@Override
	public boolean construct(URI uri) {
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.createResource(org.eclipse.emf.common.util.URI.createURI(uri.toString()));
		EFactory eFactory = childEObject.eClass().getEPackage().getEFactoryInstance();
		EObject newChildEObject = eFactory.create(childEObject.eClass());
		resource.getContents().add(newChildEObject);
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}


}
