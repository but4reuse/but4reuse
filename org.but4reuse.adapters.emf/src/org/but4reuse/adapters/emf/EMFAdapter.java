package org.but4reuse.adapters.emf;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.utils.emf.EMFUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * EMF Adapter
 * 
 * @author jabier.martinez
 */
public class EMFAdapter implements IAdapter {

	public static AdapterFactory ADAPTER_FACTORY = EMFUtils.getAllRegisteredAdapterFactories();

	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		EObject eObject = EMFUtils.getEObject(uri);
		if (eObject == null) {
			return false;
		}
		return true;
	}

	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		// First construction primitive is the Resource creation
		List<IElement> elements = new ArrayList<IElement>();
		EObject eObject = EMFUtils.getEObject(uri);
		EMFResourceElement element = new EMFResourceElement();
		element.owner = null;
		element.reference = null;
		element.childEObject = eObject;
		elements.add(element);
		// Then we loop the model to add the rest of the construction primitives
		adapt(eObject, element.childEObject, elements);
		return elements;
	}

	/**
	 * 
	 * @param eObject
	 * @param adaptedEObject
	 * @param elements
	 */
	@SuppressWarnings("unchecked")
	private void adapt(EObject eObject, EObject adaptedEObject, List<IElement> elements) {
		// Current child to set the owner is the last cp
		IElement ownerElement = elements.get(elements.size() - 1);

		// Attributes
		List<EAttribute> attributes = eObject.eClass().getEAllAttributes();
		for (EAttribute attr : attributes) {
			if (!attr.isDerived() && !attr.isVolatile() && !attr.isTransient()) {
				Object o = eObject.eGet(attr);
				if (o != null) {
					EMFAttributeElement cp = new EMFAttributeElement();
					cp.owner = adaptedEObject;
					cp.eAttribute = attr;
					cp.value = o;
					cp.addDependency(attr.getName(), ownerElement);
					elements.add(cp);
				}
			}
		}

		// References
		List<EReference> references = eObject.eClass().getEAllReferences();
		for (EReference ref : references) {
			if (!ref.isContainment() && !ref.isDerived() && !ref.isVolatile() && !ref.isTransient()) {
				List<EObject> refList = new ArrayList<EObject>();
				if (ref.isMany()) {
					refList = (List<EObject>) eObject.eGet(ref);
				} else {
					EObject o = (EObject) eObject.eGet(ref);
					if (o != null) {
						refList.add(o);
					}
				}
				// Add it even if empty. Problem of empty and null.
				if (refList == null) {
					refList = new ArrayList<EObject>();
				}
				EMFReferenceElement element = new EMFReferenceElement();
				element.owner = adaptedEObject;
				element.eReference = ref;
				element.referenced = new ArrayList<EObject>();
				for (EObject r : refList) {
					element.referenced.add(r);
				}
				element.addDependency(ref.getName(), ownerElement);
				elements.add(element);
			}
		}

		// Containments
		List<EReference> containments = eObject.eClass().getEAllContainments();
		// For each containment reference
		for (EReference childReference : containments) {
			// There could be also transient childs
			if (!childReference.isDerived() && !childReference.isVolatile() && !childReference.isTransient()) {

				// Get list of child
				List<EObject> childEObjectList = new ArrayList<EObject>();
				if (childReference.isMany()) {
					childEObjectList = (List<EObject>) eObject.eGet(childReference);
				} else {
					EObject o = (EObject) eObject.eGet(childReference);
					if (o != null) {
						childEObjectList.add(o);
					}
				}

				// If the reference had child
				if (childEObjectList != null && !childEObjectList.isEmpty()) {
					for (EObject child : childEObjectList) {
						EMFClassElement element = new EMFClassElement();
						element.childEObject = child;
						element.owner = adaptedEObject;
						element.reference = childReference;
						element.addDependency(childReference.getName(), ownerElement);
						elements.add(element);
						adapt(child, element.childEObject, elements);
					}
				}
			}
		}
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		// TODO construct EMF
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Construction",
						"For the moment, construction is not available for the EMF adapter");
			}
		});
	}

}
