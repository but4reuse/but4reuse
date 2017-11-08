package org.but4reuse.adapters.emf.helper;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.emf.EMFAdapter;
import org.but4reuse.adapters.emf.EMFAttributeElement;
import org.but4reuse.adapters.emf.EMFClassElement;
import org.but4reuse.adapters.emf.EMFReferenceElement;
import org.but4reuse.utils.emf.EMFUtils;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.diffmerge.util.ModelImplUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;

/**
 * EMF helper
 * 
 * @author jabier.martinez
 */
public class EMFHelper {

	// TODO add these options to a preferences page
	static boolean KEEP_INTRINSIC_IDS = true;
	static boolean KEEP_EXTRENSIC_IDS = true;

	public static void constructMaximalEMFModel(AdaptedModel adaptedModel, List<IElement> elements,
			URI constructionURI) {
		// Get emf adapter
		// retrieve the common block
		List<Block> commonBlocks = AdaptedModelHelper.getCommonBlocks(adaptedModel);
		List<IElement> commonElements = AdaptedModelHelper.getElementsOfBlocks(commonBlocks);

		// Do not try to fix the name as it might cause problems for other utils
		// that expect the same uri used when you call construction
		// String extension = getFileExtension(commonElements)
		// check if it ends with the correct extension
		// TODO avoid to create ".uml" file
		// if (!constructionURI.toString().endsWith("." + extension)) {
		// try {
		// constructionURI = new URI(constructionURI + "." + extension);
		// } catch (URISyntaxException e) {
		// e.printStackTrace();
		// }
		// }

		// Get the initial class
		EMFClassElement resource = getResourceClassElement(commonElements);
		if (resource == null) {
			System.out.println("Construction failed: No resource found. " + constructionURI);
			return;
		}
		commonElements = new ArrayList<IElement>();

		// A stack that is initialized with the resource EMF Class Element
		Stack<EMFClassElement> stack = new Stack<EMFClassElement>();
		stack.add(resource);
		ResourceSet resSet = new ResourceSetImpl();

		Resource emfResource = resSet.createResource(EMFUtils.uriToEMFURI(constructionURI));
		EObject resourceEObject = EcoreUtil.create(resource.eObject.eClass());
		emfResource.getContents().add(resourceEObject);

		// Map for class AME to BaseModel EObject
		HashMap<EMFClassElement, EObject> mapAMEEobject = new HashMap<EMFClassElement, EObject>();
		// Map for attribute AME to the BaseModel EObject that host it
		HashMap<EMFAttributeElement, EObject> mapAttributeAMEEobject = new HashMap<EMFAttributeElement, EObject>();

		mapAMEEobject.put(resource, resourceEObject);

		AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(EMFAdapter.ADAPTER_FACTORY,
				new BasicCommandStack());

		HashMap<EObject, String> mapNewEObjectsOldIds = new HashMap<EObject, String>();
		HashMap<EObject, String> mapNewEObjectsOldIntrinsicIds = new HashMap<EObject, String>();
		if (KEEP_EXTRENSIC_IDS) {
			mapNewEObjectsOldIds.put(resourceEObject, ModelImplUtil.getXMLID(resource.eObject));
		}
		if (KEEP_INTRINSIC_IDS) {
			mapNewEObjectsOldIntrinsicIds.put(resourceEObject, ModelImplUtil.getIntrinsicID(resource.eObject));
		}

		Map<IElement, BlockElement> iebeMap = AdaptedModelHelper.createMapIEBE(adaptedModel);

		// Preparation
		// First stack to create the model tree structure and setting the
		// attributes
		// Second stack will be for references
		while (!stack.isEmpty()) {

			EMFClassElement peek = stack.pop();
			EObject peekClass = mapAMEEobject.get(peek);

			// Get the EMF Model Elements that depends on it
			Set<IDependencyObject> dependingOnMe = AdaptedModelHelper.getDependingOnIElementBE(adaptedModel, peek,
					iebeMap);
			for (IDependencyObject ame : dependingOnMe) {
				if (ame instanceof EMFClassElement && elements.contains(ame)) {
					EMFClassElement emfclass = (EMFClassElement) ame;
					stack.add(emfclass);

					EReference reference = emfclass.reference;
					EObject child = EcoreUtil.create(emfclass.eObject.eClass());

					// Try to keep extrinsic and intrinsic ids
					String oldId = ModelImplUtil.getXMLID(emfclass.eObject);
					String oldIntrinsicId = ModelImplUtil.getIntrinsicID(emfclass.eObject);
					if (KEEP_EXTRENSIC_IDS) {
						mapNewEObjectsOldIds.put(child, oldId);
					}
					if (KEEP_INTRINSIC_IDS && oldIntrinsicId != null) {
						mapNewEObjectsOldIntrinsicIds.put(child, oldIntrinsicId);
					}

					mapAMEEobject.put(emfclass, child);

					Command command = null;
					if (reference.isMany()) {
						command = AddCommand.create(domain, peekClass, reference, child);
					} else {
						command = SetCommand.create(domain, peekClass, reference, child);
					}
					if (command != null && command.canExecute()) {
						domain.getCommandStack().execute(command);
					}
				} else if (ame instanceof EMFAttributeElement && elements.contains(ame)) {
					// Set the attribute value
					EMFAttributeElement attribute = (EMFAttributeElement) ame;
					peekClass.eSet(attribute.eAttribute, attribute.value);
					mapAttributeAMEEobject.put(attribute, peekClass);
				}
			}
		}

		// BaseModel Tree
		// References
		stack.push(resource);
		while (!stack.isEmpty()) {
			EMFClassElement peek = stack.pop();
			Set<IDependencyObject> dependingOnMe = AdaptedModelHelper.getDependingOnIElementBE(adaptedModel, peek,
					iebeMap);
			for (IDependencyObject ame : dependingOnMe) {
				if (ame instanceof EMFClassElement && elements.contains(ame)) {
					EMFClassElement emfclass = (EMFClassElement) ame;
					stack.add(emfclass);
				} else if (ame instanceof EMFReferenceElement && elements.contains(ame)) {
					EMFReferenceElement referenceAme = (EMFReferenceElement) ame;
					// It is the class that owned the reference
					if (referenceAme.ownerElement.equals(peek)) {
						EObject ownerEObject = mapAMEEobject.get(referenceAme.ownerElement);
						// Add referencedElements
						List<EObject> added = new ArrayList<EObject>();
						for (EMFClassElement referencedAme : referenceAme.referencedElements) {
							added.add(referencedAme.eObject);
							EObject ref = mapAMEEobject.get(referencedAme);
							Command command = null;
							if (referenceAme.eReference.isMany()) {
								command = AddCommand.create(domain, ownerEObject, referenceAme.eReference, ref);
							} else {
								command = SetCommand.create(domain, ownerEObject, referenceAme.eReference, ref);
							}
							if (command != null && command.canExecute()) {
								domain.getCommandStack().execute(command);
							}
						}
						// This means that the referenced were in another
						// resource
						if (added.size() != referenceAme.referenced.size()) {
							for (EObject ref : referenceAme.referenced) {
								if (!added.contains(ref)) {
									Command command = null;
									if (referenceAme.eReference.isMany()) {
										command = AddCommand.create(domain, ownerEObject, referenceAme.eReference, ref);
									} else {
										command = SetCommand.create(domain, ownerEObject, referenceAme.eReference, ref);
									}
									if (command != null && command.canExecute()) {
										domain.getCommandStack().execute(command);
									}
								}
							}
						}
					}
				}
			}
		}

		// BaseModel References
		// Save BaseModel
		// Deactivate ignore dangling to see possible errors
		EMFUtils.saveResourceIgnoringDangling(emfResource);

		// Now that it is saved we can try to keep old ids
		if (KEEP_EXTRENSIC_IDS || KEEP_INTRINSIC_IDS) {
			TreeIterator<EObject> i = emfResource.getAllContents();
			while (i.hasNext()) {
				EObject o = i.next();
				if (KEEP_INTRINSIC_IDS) {
					String oldIntrinsicId = mapNewEObjectsOldIntrinsicIds.get(o);
					ModelImplUtil.setIntrinsicID(o, oldIntrinsicId);
				}
				if (KEEP_EXTRENSIC_IDS) {
					String oldId = mapNewEObjectsOldIds.get(o);
					ModelImplUtil.setXMLID(o, oldId);
				}
			}

			// Save again BaseModel to try to maintain old ids
			EMFUtils.saveResourceIgnoringDangling(emfResource);
		}
	}

	/**
	 * Get the resource emf class element
	 * 
	 * @param elements
	 * @return
	 */
	public static EMFClassElement getResourceClassElement(List<IElement> elements) {
		for (IElement e : elements) {
			if (e instanceof EMFClassElement) {
				EMFClassElement cl = ((EMFClassElement) e);
				if (cl.isResource) {
					return cl;
				}
			}
		}
		return null;
	}

	/**
	 * Get the extension of the model (uml or whatever we are dealing with)
	 * 
	 * @param elements
	 * @return the extension or "model" if nothing found
	 */
	public static String getFileExtension(List<IElement> elements) {
		for (IElement e : elements) {
			if (e instanceof EMFClassElement) {
				String extension = EMFUtils.getModelExtension(((EMFClassElement) e).eObject);
				if (!extension.isEmpty()) {
					return extension;
				}
			}
		}
		// nothing found
		return "model";
	}
}
