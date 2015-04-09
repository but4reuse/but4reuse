package org.but4reuse.adapters.emf;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.emf.diffmerge.DiffMergeUtils;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.utils.emf.EMFUtils;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.diffmerge.ui.specification.IComparisonMethod;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * EMF Adapter
 * 
 * @author jabier.martinez
 */
public class EMFAdapter implements IAdapter {

	public static AdapterFactory ADAPTER_FACTORY = EMFUtils.getAllRegisteredAdapterFactories();

	/**
	 * Adaptable if we can load an EObject from the URI
	 */
	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		EObject eObject = EMFUtils.getEObject(uri);
		if (eObject == null) {
			return false;
		}
		return true;
	}

	/**
	 * Recursively adapt the model
	 */
	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		// First construction primitive is the Resource creation
		List<IElement> elements = new ArrayList<IElement>();
		EObject eObject = EMFUtils.getEObject(uri);
		EMFResourceElement element = new EMFResourceElement();
		element.owner = null;
		element.reference = null;
		element.eObject = eObject;
		elements.add(element);
		// Then we loop the model to add the rest of the construction primitives
		adapt(eObject, element.eObject, elements);
		// After that we add the EReferences dependencies
		addReferencesDependencies(elements);
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
		// If it is not covered by the diff policy we are not going to consider
		// it
		IComparisonMethod comparisonMethod = DiffMergeUtils.getComparisonMethod(adaptedEObject, adaptedEObject);
		// Current child to set the owner is the last element
		AbstractElement ownerElement = (AbstractElement) elements.get(elements.size() - 1);

		// Attributes
		List<EAttribute> attributes = eObject.eClass().getEAllAttributes();
		for (EAttribute attr : attributes) {
			if (comparisonMethod.getDiffPolicy().coverFeature(attr)) {
				if (!attr.isDerived() && !attr.isVolatile() && !attr.isTransient()) {
					Object o = eObject.eGet(attr);
					if (o != null) {
						EMFAttributeElement element = new EMFAttributeElement();
						element.owner = adaptedEObject;
						element.eAttribute = attr;
						element.value = o;
						element.addDependency(attr.getName(), ownerElement);
						ownerElement.setMaximumDependencies(attr.getName(), 1);
						elements.add(element);
					}
				}
			}
		}

		// References
		List<EReference> references = eObject.eClass().getEAllReferences();
		for (EReference ref : references) {
			if (comparisonMethod.getDiffPolicy().coverFeature(ref)) {
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
					// if (refList != null) {
					element.referenced = new ArrayList<EObject>();
					for (EObject r : refList) {
						element.referenced.add(r);
					}
					// }
					element.addDependency(ref.getName(), ownerElement);

					ownerElement.setMaximumDependencies(ref.getName(), 1);
					elements.add(element);
				}
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
						element.eObject = child;
						element.owner = adaptedEObject;
						element.reference = childReference;
						element.addDependency(childReference.getName(), ownerElement);
						// TODO add maximum dependencies
						elements.add(element);
						adapt(child, element.eObject, elements);
					}
				}
			}
		}
	}

	/**
	 * Once we have all the Elements, we prepare the dependencies
	 * 
	 * @param elements
	 */
	private void addReferencesDependencies(List<IElement> elements) {
		for (IElement element : elements) {
			if (element instanceof EMFReferenceElement) {
				EMFReferenceElement referenceElement = (EMFReferenceElement) element;
				// set max and min number of dependencies
				// if -1 or -2 keep max integer
				if (referenceElement.eReference.getUpperBound() > 0) {
					referenceElement.setMaximumDependencies(referenceElement.eReference.getName(),
							referenceElement.eReference.getUpperBound());
				}
				referenceElement.setMinimumDependencies(referenceElement.eReference.getName(),
						referenceElement.eReference.getLowerBound());
				// Now add the dependencies
				for (EObject referenced : referenceElement.referenced) {
					EMFClassElement classElement = findClassElement(elements, referenced);
					if (classElement == null) {
						// this must not happen
						System.out.println("EMFAdapter.addReferencesDependencies() not found!");
					}
					referenceElement.addDependency(referenceElement.eReference.getName(), classElement);
				}
			}
		}
	}

	/**
	 * Find the Class Element that contains this eObject
	 * 
	 * @param elements
	 * @param eObject
	 * @return an EMFClassElement or null
	 */
	private EMFClassElement findClassElement(List<IElement> elements, EObject eObject) {
		for (IElement element : elements) {
			if (element instanceof EMFClassElement) {
				EMFClassElement classElement = (EMFClassElement) element;
				if (eObject.equals(classElement.eObject)) {
					return classElement;
				}
			}
		}
		return null;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		try {
			URI original = null;
			// Check if there is a resource element
			EMFResourceElement res = null;
			for (IElement element : elements) {
				if (element instanceof EMFResourceElement) {
					res = (EMFResourceElement) element;
					original = new URI(res.eObject.eResource().getURI().toString());
					File sourceFile = FileUtils.getFile(original);
					File destinationFile = FileUtils.getFile(uri);
					FileUtils.copyFile(sourceFile, destinationFile);
					break;
				}
			}
			// in uri we have the copy of the model
			// We remove all the Elements from the copy that are not in the list
			// of elements
			if (res != null) {
				EObject sourceModel = res.eObject;
				EObject destinationModel = EMFUtils.getEObject(uri);
				// the iterators will have the same order
				TreeIterator<EObject> sourceIterator = sourceModel.eAllContents();
				TreeIterator<EObject> destinationIterator = destinationModel.eAllContents();
				List<EObject> toBeRemoved = new ArrayList<EObject>();
				while (sourceIterator.hasNext()) {
					EObject se = sourceIterator.next();
					EObject de = destinationIterator.next();
					if (!containedInTheElements(se, elements)) {
						toBeRemoved.add(de);
					}
				}
				// remove them
				for(EObject e : toBeRemoved){
					EcoreUtil.delete(e, true);
				}
				EMFUtils.saveResource(destinationModel.eResource());
			} else {
				// TODO construct EMF
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Construction",
								"For the moment, construction is not available for the EMF adapter");
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean containedInTheElements(EObject se, List<IElement> elements) {
		for (IElement element : elements) {
			if (element instanceof EMFClassElement) {
				EObject e = ((EMFClassElement) element).eObject;
				if (e.equals(se)) {
					return true;
				}
			}
		}
		return false;
	}

}
