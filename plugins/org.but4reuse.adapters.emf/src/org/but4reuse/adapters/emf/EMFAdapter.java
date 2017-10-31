package org.but4reuse.adapters.emf;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.AdaptedModelFactory;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.emf.activator.Activator;
import org.but4reuse.adapters.emf.diffmerge.DiffMergeUtils;
import org.but4reuse.adapters.emf.helper.EMFHelper;
import org.but4reuse.adapters.emf.preferences.EMFAdapterPreferencePage;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.utils.emf.EMFUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.diffmerge.ui.specification.IComparisonMethod;
import org.eclipse.emf.diffmerge.ui.specification.IComparisonMethodFactory;
import org.eclipse.emf.diffmerge.ui.specification.ext.EObjectScopeDefinition;
import org.eclipse.emf.diffmerge.util.ModelImplUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * EMF Adapter
 * 
 * @author jabier.martinez
 */
public class EMFAdapter implements IAdapter {

	public static AdapterFactory ADAPTER_FACTORY = EMFUtils.getAllRegisteredAdapterFactories();

	// This will store the comparison method used during the analysis
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
		EMFClassElement element = getEMFElementsFactory().createEMFClassElement();
		element.isResource = true;
		element.owner = null;
		element.ownerElement = null;
		element.reference = null;
		element.eObject = eObject;
		elements.add(element);
		// Then we loop the model to add the rest of the construction primitives
		refElements = new ArrayList<EMFReferenceElement>();
		eobjectEMFClassElementMap = new HashMap<EObject, EMFClassElement>();
		eobjectEMFClassElementMap.put(element.eObject, element);

		// Initialize the comparison method
		if (getComparisonMethod() == null) {
			initializeComparisonMethod(element.eObject);
		}

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

		// Current child to set the owner is the last element
		AbstractElement ownerElement = (AbstractElement) elements.get(elements.size() - 1);

		// Attributes
		List<EAttribute> attributes = eObject.eClass().getEAllAttributes();
		for (EAttribute attr : attributes) {
			// is a real attribute
			if (considerEStructuralFeature(eObject, attr)) {
				// should it be covered by the diff policy
				if (getComparisonMethod().getDiffPolicy().coverFeature(attr)) {
					Object o = eObject.eGet(attr);
					if (o instanceof Enumerator) {
						// because eGet will return a value even if it was
						// undefined
						if (!eObject.eIsSet(attr)) {
							o = null;
						}
					}
					if (o != null) {
						EMFAttributeElement element = getEMFElementsFactory().createEMFAttributeElement();
						element.owner = adaptedEObject;
						element.ownerElement = ownerElement;
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
			if (considerEStructuralFeature(eObject, ref) && !ref.isContainment() && !ref.isContainer()) {
				if (getComparisonMethod().getDiffPolicy().coverFeature(ref)) {
					List<EObject> refList = EMFUtils.getReferencedEObjects(eObject, ref);
					EMFReferenceElement element = getEMFElementsFactory().createEMFReferenceElement();
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
			// There could be also transient child
			if (considerEStructuralFeature(eObject, childReference)) {
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
						EMFClassElement element = getEMFElementsFactory().createEMFClassElement();
						element.eObject = child;
						element.owner = adaptedEObject;
						element.ownerElement = ownerElement;
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
	 * Initialize the comparison method that will be used
	 * 
	 * @param adaptedEObject
	 */
	private void initializeComparisonMethod(EObject adaptedEObject) {
		// TODO once initialised, the same will be used always. It is not
		// possible to change it, the user will need to relaunch the tool
		List<IComparisonMethod> comparisonMethods = DiffMergeUtils.getApplicableComparisonMethods(adaptedEObject,
				adaptedEObject);
		if (comparisonMethods.size() == 1) {
			comparisonMethod = comparisonMethods.get(0);
		} else {
			// There are several applicable comparison methods. Check if there
			// is one user-defined
			String selected = Activator.getDefault().getPreferenceStore()
					.getString(EMFAdapterPreferencePage.COMPARISON_METHOD);
			if (selected == null || selected.isEmpty()) {
				// No user-defined selection. Take the first one
				comparisonMethod = comparisonMethods.get(0);
			} else {
				// Take the one defined by the user
				List<IComparisonMethodFactory> factories = DiffMergeUtils
						.getApplicableComparisonMethodFactories(adaptedEObject, adaptedEObject);
				for (IComparisonMethodFactory factory : factories) {
					if (factory.getLabel().equalsIgnoreCase(selected)) {
						EObjectScopeDefinition left = new EObjectScopeDefinition(adaptedEObject, "left", true);
						EObjectScopeDefinition right = new EObjectScopeDefinition(adaptedEObject, "right", true);
						comparisonMethod = factory.createComparisonMethod(left, right, null);
						break;
					}
				}
				// The one that the user selected is not found...
				if (comparisonMethod == null) {
					// TODO report error
					System.err.println("User-defined comparison method in the preferences is not found.");
					// Take the first one
					comparisonMethod = comparisonMethods.get(0);
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
					// System.out.println("EMFAdapter.addReferencesDependencies()
					// Warning: Referenced element not found "
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
	public EMFClassElement findClassElement(EObject eObject) {
		return eobjectEMFClassElementMap.get(eObject);
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		System.out.println("Basic EMF models construct (only BaseModel). Use the CVL construct instead!");
		// TODO construct fragments. Right now there must be a resource element
		// (EMFClassElement with isResource)
		// Get current adaptedModel with all elements
		AdaptedModel adaptedModel = EcoreUtil.copy(AdaptedModelManager.getAdaptedModel());

		// Construct only the elements of the selected blocks
		AdaptedModel newAdaptedModel = AdaptedModelFactory.eINSTANCE.createAdaptedModel();
		for (AdaptedArtefact aa : adaptedModel.getOwnedAdaptedArtefacts()) {
			AdaptedArtefact newaa = AdaptedModelFactory.eINSTANCE.createAdaptedArtefact();
			newAdaptedModel.getOwnedAdaptedArtefacts().add(newaa);
			newaa.getOwnedElementWrappers().addAll(aa.getOwnedElementWrappers());
		}
		for (Block block : adaptedModel.getOwnedBlocks()) {
			if (elements.containsAll(AdaptedModelHelper.getElementsOfBlock(block))) {
				Block newBlock = AdaptedModelFactory.eINSTANCE.createBlock();
				newBlock.setName(block.getName());
				newBlock.getOwnedBlockElements().addAll(block.getOwnedBlockElements());
				newAdaptedModel.getOwnedBlocks().add(newBlock);
			}
		}
		EMFHelper.constructMaximalEMFModel(newAdaptedModel, elements, uri);
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
		} else if (Activator.getDefault().getPreferenceStore().getBoolean(EMFAdapterPreferencePage.MATCH_ID_HASHING)) {
			String id = getComparisonMethod().getMatchPolicy().getMatchID(eObject, null).toString();
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		} else {
			return 1;
		}
	}

	/**
	 * To override if needed.
	 * 
	 * @return
	 */
	public static IComparisonMethod getComparisonMethod() {
		return comparisonMethod;
	}

	/**
	 * get the EMF elements factory. This can be used to override and provide
	 * your own elements that extend the default elements
	 * 
	 * @return emf elements factory
	 */
	public EMFElementsFactory getEMFElementsFactory() {
		return new EMFElementsFactory();
	}

	public boolean considerEStructuralFeature(EObject eObject, EStructuralFeature structuralFeature) {
		return eObject.eIsSet(structuralFeature) && !structuralFeature.isDerived() && !structuralFeature.isVolatile()
				&& !structuralFeature.isTransient();
	}

}
