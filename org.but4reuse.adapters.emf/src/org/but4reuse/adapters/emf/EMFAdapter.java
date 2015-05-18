package org.but4reuse.adapters.emf;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.emf.activator.Activator;
import org.but4reuse.adapters.emf.diffmerge.DiffMergeUtils;
import org.but4reuse.adapters.emf.preferences.EMFAdapterPreferencePage;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.utils.emf.EMFUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.diffmerge.ui.specification.IComparisonMethod;
import org.eclipse.emf.diffmerge.util.ModelImplUtil;
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

	// This will store the comparison method used during the analysis
	// TODO support different comparison methods for the same analysis
	static IComparisonMethod comparisonMethod = null;
	
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
		EMFClassElement element = new EMFClassElement();
		element.isResource = true;
		element.owner = null;
		element.reference = null;
		element.eObject = eObject;
		elements.add(element);
		// Then we loop the model to add the rest of the construction primitives
		refElements = new ArrayList<EMFReferenceElement>();
		eobjectEMFClassElementMap = new HashMap<EObject, EMFClassElement>();
		eobjectEMFClassElementMap.put(element.eObject, element);
		adapt(eObject, element.eObject, elements);
		// After that we add the EReferences dependencies
		addReferenceDependencies();
		return elements;
	}

	List<EMFReferenceElement> refElements = new ArrayList<EMFReferenceElement>();
	// TODO list or map? it seems that list is also ok
	// List<EMFClassElement> classElements = new ArrayList<EMFClassElement>();
	Map<EObject, EMFClassElement> eobjectEMFClassElementMap = new HashMap<EObject, EMFClassElement>();

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
		// TODO we assume that we always use the same comparison method
		if (comparisonMethod == null) {
			comparisonMethod = DiffMergeUtils.getComparisonMethod(adaptedEObject, adaptedEObject);
		}
		// Current child to set the owner is the last element
		AbstractElement ownerElement = (AbstractElement) elements.get(elements.size() - 1);

		// Attributes
		List<EAttribute> attributes = eObject.eClass().getEAllAttributes();
		for (EAttribute attr : attributes) {
			// is a real attribute
			if (eObject.eIsSet(attr) && !attr.isDerived() && !attr.isVolatile() && !attr.isTransient()) {
				// should it be covered by the diff policy
				if (comparisonMethod.getDiffPolicy().coverFeature(attr)) {
					Object o = eObject.eGet(attr);
					if (o instanceof Enumerator) {
						// because eGet will return a value even if it was
						// undefined
						if (!eObject.eIsSet(attr)) {
							o = null;
						}
					}
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
			if (eObject.eIsSet(ref) && !ref.isContainment() && !ref.isContainer() && !ref.isDerived()
					&& !ref.isVolatile() && !ref.isTransient()) {
				if (comparisonMethod.getDiffPolicy().coverFeature(ref)) {
					List<EObject> refList = EMFUtils.getReferencedEObjects(eObject, ref);
					EMFReferenceElement element = new EMFReferenceElement();
					element.owner = adaptedEObject;
					element.ownerElement = ownerElement;
					element.eReference = ref;
					element.referenced = new ArrayList<EObject>();
					element.referencedElements = new ArrayList<EMFClassElement>();
					for (EObject r : refList) {
						element.referenced.add(r);
					}
					element.addDependency(ref.getName(), ownerElement);
					ownerElement.setMaximumDependencies(ref.getName(), 1);
					refElements.add(element);
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
						// add maximum dependencies
						eobjectEMFClassElementMap.put(element.eObject, element);
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
	 * @param refElements2
	 */
	private void addReferenceDependencies() {
		for (EMFReferenceElement referenceElement : refElements) {
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
				EMFClassElement classElement = findClassElement(referenced);
				if (classElement == null) {
					// this must not happen...
					// Probably the EObject is outside this resource! It
					// references another model. Not supported.
					// TODO provide feedback to user
					// System.out.println("EMFAdapter.addReferencesDependencies() Warning: Referenced element not found "
					// + referenced);
				} else {
					referenceElement.referencedElements.add(classElement);
					// referenceElement.addDependency(referenceElement.eReference.getName(),
					// classElement);
					// hard-coded reference id
					referenceElement.addDependency("referenced", classElement);
				}
			}
		}
	}

	/**
	 * Find the Class Element that contains this eObject
	 * 
	 * @param classElements2
	 * @param eObject
	 * @return an EMFClassElement or null
	 */
	private EMFClassElement findClassElement(EObject eObject) {
		return eobjectEMFClassElementMap.get(eObject);
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		// TODO construct fragments. Using CVL extraction for the moment
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Construction",
						"For the moment, construction is not available. Use CVL extraction action instead.");
			}
		});
	}

	/**
	 * Get the hash code of an eObject based on the extrensic id (e.g. xmi id).
	 * To be activated or deactivated in the preferences page of the adapter.
	 * Use it (it will boost the speed) if we can assume that two eObjects with
	 * the same extrensic id must be the same and that it cannot happen that two
	 * eObjects are different with the same extrensic id.
	 * 
	 * @param eObject
	 * @return
	 */
	public static int getHashCode(EObject eObject) {
		if (Activator.getDefault().getPreferenceStore().getBoolean(EMFAdapterPreferencePage.XML_ID_HASHING)) {
			String id = ModelImplUtil.getXMLID(eObject);
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		} else {
			return 1;
		}
	}

	public static IComparisonMethod getComparisonMethod() {
		return comparisonMethod;
	}

}
