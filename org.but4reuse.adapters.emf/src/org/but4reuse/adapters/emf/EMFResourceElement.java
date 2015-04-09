package org.but4reuse.adapters.emf;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;

import org.but4reuse.utils.emf.EMFUtils;
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
		return "Class: " + eObject.eClass().getName();
	}
	
	@Override
	public boolean construct(URI uri) {
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.createResource(EMFUtils.uriToEMFURI(uri));
		EFactory eFactory = EMFUtils.getEFactory(eObject);
		EObject newChildEObject = eFactory.create(eObject.eClass());
		resource.getContents().add(newChildEObject);
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}


}
