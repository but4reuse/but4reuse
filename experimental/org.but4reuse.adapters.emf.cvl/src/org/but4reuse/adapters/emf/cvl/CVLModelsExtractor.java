package org.but4reuse.adapters.emf.cvl;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
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
import org.but4reuse.adapters.emf.helper.EMFHelper;
import org.but4reuse.feature.constraints.BasicExcludesConstraint;
import org.but4reuse.feature.constraints.BasicRequiresConstraint;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.impl.ConstraintsHelper;
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

import cvl.CVLModel;
import cvl.CompositeVariability;
import cvl.CvlFactory;
import cvl.FragmentSubstitution;
import cvl.FromPlacement;
import cvl.Iterator;
import cvl.IteratorResolution;
import cvl.PlacementFragment;
import cvl.PlacementValue;
import cvl.ReplacementFragment;
import cvl.ReplacementValue;
import cvl.ResolutionElement;
import cvl.ToPlacement;
import cvl.ToReplacement;
import cvl.ValueSubstitution;

/**
 * CVL Models Extractor
 * 
 * @author jabier.martinez
 */
public class CVLModelsExtractor {

	// TODO add these options to a preferences page
	static boolean KEEP_INTRINSIC_IDS = true;
	static boolean KEEP_EXTRENSIC_IDS = true;

	public static void createCVLModels(String constructionURI, AdaptedModel adaptedModel) {
		try {

			System.out.println("Start construct Base Model");
			double startTime = System.currentTimeMillis();
			/**
			 * Construct the Base Model
			 */
			// TODO For the construction of the BaseModel refactor to use
			// org.but4reuse.adapters.emf.helper.EMFHelper.constructMaximalEMFModel(AdaptedModel,
			// List<IElement>, URI)
			// Get emf adapter
			// retrieve the common block
			Block baseBlock = AdaptedModelHelper.getCommonBlocks(adaptedModel).get(0);
			List<IElement> elements = AdaptedModelHelper.getElementsOfBlock(baseBlock);
			String extension = EMFHelper.getFileExtension(elements);
			URI baseModelURI = new URI(constructionURI + "BaseModel." + extension);

			// Get the initial class
			EMFClassElement resource = EMFHelper.getResourceClassElement(elements);
			elements = new ArrayList<IElement>();

			// A stack that is initialized with the resource EMF Class Element
			Stack<EMFClassElement> stack = new Stack<EMFClassElement>();
			stack.add(resource);
			ResourceSet resSet = new ResourceSetImpl();

			Resource emfResource = resSet.createResource(EMFUtils.uriToEMFURI(baseModelURI));
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

			// Map<IElement,ElementWrapper> ieewMap =
			// AdaptedModelHelper.createMapIEEW(adaptedModel);
			Map<IElement, BlockElement> iebeMap = AdaptedModelHelper.createMapIEBE(adaptedModel);
			System.out.println("Preparation " + ((System.currentTimeMillis() - startTime) / 1000.0));
			startTime = System.currentTimeMillis();
			// First stack to create the model tree structure and setting the
			// attributes
			// Second stack will be for references
			// int it = 0;
			while (!stack.isEmpty()) {
				// it++;

				EMFClassElement peek = stack.pop();
				EObject peekClass = mapAMEEobject.get(peek);

				// Get the EMF Model Elements that depends on it
				// System.out.println(it);
				Set<IDependencyObject> dependingOnMe = AdaptedModelHelper.getDependingOnIElementBE(adaptedModel, peek,
						iebeMap);
				// System.out.println("next " + it);
				for (IDependencyObject ame : dependingOnMe) {
					if (ame instanceof EMFClassElement) {
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
					} else if (ame instanceof EMFAttributeElement) {
						// Set the attribute value
						EMFAttributeElement attribute = (EMFAttributeElement) ame;
						peekClass.eSet(attribute.eAttribute, attribute.value);
						mapAttributeAMEEobject.put(attribute, peekClass);
					}
				}
			}

			System.out.println("BaseModel Tree " + ((System.currentTimeMillis() - startTime) / 1000.0));
			startTime = System.currentTimeMillis();

			// References
			stack.push(resource);
			while (!stack.isEmpty()) {
				EMFClassElement peek = stack.pop();
				Set<IDependencyObject> dependingOnMe = AdaptedModelHelper.getDependingOnIElementBE(adaptedModel, peek,
						iebeMap);
				for (IDependencyObject ame : dependingOnMe) {
					if (ame instanceof EMFClassElement) {
						EMFClassElement emfclass = (EMFClassElement) ame;
						stack.add(emfclass);
					} else if (ame instanceof EMFReferenceElement) {
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
											command = AddCommand.create(domain, ownerEObject, referenceAme.eReference,
													ref);
										} else {
											command = SetCommand.create(domain, ownerEObject, referenceAme.eReference,
													ref);
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

			System.out.println("BaseModel References " + ((System.currentTimeMillis() - startTime) / 1000.0));
			startTime = System.currentTimeMillis();

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

			System.out.println("BaseModel Saving " + ((System.currentTimeMillis() - startTime) / 1000.0));
			startTime = System.currentTimeMillis();

			System.out.println("Start construct CVL Model");
			/**
			 * Create the CVL model
			 */
			URI modelURI = new URI(constructionURI + "VariabilityAndResolution.cvl");

			CVLModel cvlModel = CvlFactory.eINSTANCE.createCVLModel();
			cvlModel.setName(AdaptedModelHelper.getName(adaptedModel));

			cvlModel.setBase(EMFUtils.getEObject(baseModelURI));

			// Add variability information (the features)
			CompositeVariability hiddenRoot = CvlFactory.eINSTANCE.createCompositeVariability();
			hiddenRoot.setName("varModel");
			cvlModel.setVariabilitySpecification(hiddenRoot);

			Map<String, CompositeVariability> map = new HashMap<String, CompositeVariability>();
			CompositeVariability root = CvlFactory.eINSTANCE.createCompositeVariability();
			root.setName(AdaptedModelHelper.getName(adaptedModel));
			hiddenRoot.getVariabilitySpecification().add(root);

			Iterator iterator = CvlFactory.eINSTANCE.createIterator();
			iterator.setName("OR");
			iterator.setLowerLimit(0);
			// blocks and negations
			iterator.setUpperLimit((adaptedModel.getOwnedBlocks().size() - 1) * 2);
			root.getVariabilitySpecification().add(iterator);

			/**
			 * Features and negations
			 */
			// Add all features
			for (Block block : adaptedModel.getOwnedBlocks()) {
				CompositeVariability var = CvlFactory.eINSTANCE.createCompositeVariability();
				var.setName(block.getName().replaceAll(" ", "_"));
				map.put(block.getName().replaceAll(" ", "_"), var);
				iterator.getVariabilitySpecification().add(var);
			}

			// Add all negation features
			for (Block block : adaptedModel.getOwnedBlocks()) {
				if (block != baseBlock) {
					CompositeVariability varNeg = CvlFactory.eINSTANCE.createCompositeVariability();
					varNeg.setName("No_" + block.getName().replaceAll(" ", "_"));
					map.put("No_" + block.getName().replaceAll(" ", "_"), varNeg);
					iterator.getVariabilitySpecification().add(varNeg);
				}
			}

			// System.out.println("Resolution layer");
			/**
			 * Resolution layer
			 */
			// Let's go for the placement, replacement etc.
			for (Block block : adaptedModel.getOwnedBlocks()) {
				CompositeVariability varNeg = map.get("No_" + block.getName().replaceAll(" ", "_"));
				CompositeVariability var = map.get(block.getName().replaceAll(" ", "_"));
				if (block != baseBlock) {

					List<IElement> blockElements = AdaptedModelHelper.getElementsOfBlock(block);

					// Get all eobjects of this block
					List<EObject> blockEObjects = new ArrayList<EObject>();
					for (IElement ele : blockElements) {
						if (ele instanceof EMFClassElement) {
							EObject e = mapAMEEobject.get((EMFClassElement) ele);
							if (e == null) {
								System.out.println("CVLModelsExtractor.createCVLModels()");
								System.out.println("EMFClassElement without EObject! " + ele.getText());
							} else {
								blockEObjects.add(e);
							}
						}
					}

					// Check that not everything was attribute or reference AMEs
					if (!blockEObjects.isEmpty()) {
						// Create Substitution
						FragmentSubstitution fs = CvlFactory.eINSTANCE.createFragmentSubstitution();
						fs.setName("Substitution");

						// Create placement with elements to delete
						PlacementFragment pf = CvlFactory.eINSTANCE.createPlacementFragment();
						pf.setName("Placement");
						varNeg.getVariabilitySpecification().add(pf);

						// List for all the elements From the placement
						List<EObject> eObjectsForFromPlacement = new ArrayList<EObject>();

						for (IElement ele : blockElements) {
							if (ele instanceof EMFClassElement) {
								EMFClassElement emfclass = ((EMFClassElement) ele);
								EObject eObject = mapAMEEobject.get(emfclass);
								if (eObject != null) {
									// Add it only if parent is not part of this
									// block
									EObject parent = eObject.eContainer();
									if (!blockEObjects.contains(parent)) {
										if (!eObjectsForFromPlacement.contains(parent)) {
											eObjectsForFromPlacement.add(parent);
										}
										ToPlacement toPlacement = CvlFactory.eINSTANCE.createToPlacement();
										toPlacement.getInsideBoundaryElement().add(eObject);
										toPlacement.setOutsideBoundaryElement(eObject.eContainer());
										toPlacement.setName(EMFUtils.getText(emfclass.eObject));
										toPlacement.setPropertyName(emfclass.reference.getName());
										pf.getBoundaryElement().add(toPlacement);
									}
								}
							}
						}

						// For the FromPlacement, add all eobjects that are
						// referenced and not inside the placement
						for (EObject blockEObject : blockEObjects) {
							List<EReference> references = blockEObject.eClass().getEAllReferences();
							for (EReference ref : references) {
								if (!ref.isDerived() && !ref.isVolatile() && !ref.isTransient()) {
									if (ref.isMany()) {
										List<?> refList = (List<?>) blockEObject.eGet(ref);
										for (Object eo : refList) {
											if (eo != null && !blockEObjects.contains(eo)) {
												if (!eObjectsForFromPlacement.contains(eo)) {
													eObjectsForFromPlacement.add((EObject) eo);
												}
											}
										}
									} else {
										EObject eo = (EObject) blockEObject.eGet(ref);
										if (eo != null && !blockEObjects.contains(eo)) {
											if (!eObjectsForFromPlacement.contains(eo)) {
												eObjectsForFromPlacement.add(eo);
											}
										}
									}
								}
							}
						}

						// Add from placement
						FromPlacement fromPlacement = CvlFactory.eINSTANCE.createFromPlacement();
						fromPlacement.getOutsideBoundaryElement().addAll(eObjectsForFromPlacement);
						fromPlacement.setName("From Placement");
						pf.getBoundaryElement().add(fromPlacement);

						// Create Empty replacement
						ReplacementFragment rf = CvlFactory.eINSTANCE.createReplacementFragment();
						rf.setName("Replacement Empty");
						varNeg.getVariabilitySpecification().add(rf);
						// Do nothing to replacement
						ToReplacement toR = CvlFactory.eINSTANCE.createToReplacement();
						toR.setName("Do Nothing");
						rf.getBoundaryElement().add(toR);

						// finish the fragment substitution
						varNeg.getVariabilitySpecification().add(fs);
						fs.setPlacement(pf);
						fs.setReplacement(rf);
					}

					// Attributes and References
					for (IElement ele : blockElements) {
						if (ele instanceof EMFAttributeElement) {
							EMFAttributeElement elea = (EMFAttributeElement) ele;
							EObject e = mapAttributeAMEEobject.get(ele);
							if (!blockEObjects.contains(e)) {
								PlacementValue pv = CvlFactory.eINSTANCE.createPlacementValue();
								pv.setPropertyName(elea.eAttribute.getName());
								pv.setTargetObject(e);
								pv.setName("Value Placement " + EMFUtils.getText(e) + " " + elea.eAttribute.getName());

								ReplacementValue rv = CvlFactory.eINSTANCE.createReplacementValue();
								rv.setValue(elea.value.toString());
								rv.setName("Value Replacement " + elea.value.toString());

								ValueSubstitution vs = CvlFactory.eINSTANCE.createValueSubstitution();
								vs.setPlacement(pv);
								vs.setReplacement(rv);
								vs.setName("Value Substitution");

								var.getVariabilitySpecification().add(pv);
								var.getVariabilitySpecification().add(rv);
								var.getVariabilitySpecification().add(vs);
							}
						}
					}

				}
			}

			/**
			 * Add the constraints
			 */
			for (IConstraint constraint : ConstraintsHelper.getCalculatedConstraints(adaptedModel)) {
				if (constraint instanceof BasicRequiresConstraint) {
					BasicRequiresConstraint c = (BasicRequiresConstraint)constraint;
					// Implies
					CVLUtils.addRequiresConstraint(root, map.get(c.getBlock1().getName().replaceAll(" ", "_")),
							map.get(c.getBlock2().getName().replaceAll(" ", "_")));
				} else if (constraint instanceof BasicExcludesConstraint) {
					BasicExcludesConstraint c = (BasicExcludesConstraint)constraint;
					// Mutually excludes
					CVLUtils.addMutualExclusionConstraint(root,
							map.get(c.getBlock1().getName().replaceAll(" ", "_")),
							map.get(c.getBlock2().getName().replaceAll(" ", "_")));
				}
			}

			/**
			 * Negations
			 */
			for (Block block : adaptedModel.getOwnedBlocks()) {
				if (block != baseBlock) {
					CompositeVariability var = map.get(block.getName().replaceAll(" ", "_"));
					CompositeVariability negVar = map.get("No_" + block.getName().replaceAll(" ", "_"));
					CVLUtils.addMutualExclusionConstraint(root, var, negVar);
				}
			}

			/**
			 * Add resolution elements (the variants)
			 */
			for (AdaptedArtefact adaptedArtefact : adaptedModel.getOwnedAdaptedArtefacts()) {
				ResolutionElement artefactVariant = CvlFactory.eINSTANCE.createResolutionElement();
				artefactVariant.setName(adaptedArtefact.getArtefact().getName());
				cvlModel.getResolutionSpecification().add(artefactVariant);

				IteratorResolution iteratorResolution = CvlFactory.eINSTANCE.createIteratorResolution();
				iteratorResolution.setIterator(iterator);
				artefactVariant.getResolution().add(iteratorResolution);

				List<Block> blocks = AdaptedModelHelper.getBlocksOfAdaptedArtefact(adaptedArtefact);
				for (Block block : adaptedModel.getOwnedBlocks()) {
					ResolutionElement re = CvlFactory.eINSTANCE.createResolutionElement();
					if (blocks.contains(block)) {
						CompositeVariability cv = map.get(block.getName().replaceAll(" ", "_"));
						re.setElement(cv);
						iteratorResolution.getChoice().add(re);
					} else {
						CompositeVariability cv = map.get("No_" + block.getName().replaceAll(" ", "_"));
						re.setElement(cv);
						iteratorResolution.getChoice().add(re);
					}
				}
			}

			// Save it and it is ready
			EMFUtils.saveEObject(modelURI, cvlModel);
			System.out.println("CVL Model finished " + ((System.currentTimeMillis() - startTime) / 1000.0));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
