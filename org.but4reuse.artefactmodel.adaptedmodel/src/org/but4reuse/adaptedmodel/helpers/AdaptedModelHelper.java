package org.but4reuse.adaptedmodel.helpers;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.AdaptedModelFactory;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.eclipse.core.runtime.IProgressMonitor;

public class AdaptedModelHelper {

	/**
	 * Adapt an artefact model to create the list of elements of each artefact
	 * 
	 * @param artefactModel
	 * @param adapters
	 * @param monitor
	 * @return
	 */
	public static AdaptedModel adapt(ArtefactModel artefactModel, List<IAdapter> adapters, IProgressMonitor monitor) {
		AdaptedModel adaptedModel = AdaptedModelFactory.eINSTANCE.createAdaptedModel();

		// TODO implement concurrency to improve performance
		for (Artefact artefact : artefactModel.getOwnedArtefacts()) {
			if (artefact.isActive()) {
				AdaptedArtefact adaptedArtefact = AdaptedModelFactory.eINSTANCE.createAdaptedArtefact();
				adaptedArtefact.setArtefact(artefact);

				String name = artefact.getName();
				if (name == null || name.length() == 0) {
					name = artefact.getArtefactURI();
				}
				monitor.subTask("Adapting: " + name);

				List<IElement> list = AdaptersHelper.getElements(artefact, adapters);
				for (IElement ele : list) {
					ElementWrapper ew = AdaptedModelFactory.eINSTANCE.createElementWrapper();
					ew.setElement(ele);
					adaptedArtefact.getOwnedElementWrappers().add(ew);
				}

				adaptedModel.getOwnedAdaptedArtefacts().add(adaptedArtefact);
				monitor.worked(1);
				if (monitor.isCanceled()) {
					return adaptedModel;
				}
			}
		}
		return adaptedModel;
	}

	public static boolean isIdentical(ElementWrapper e1, ElementWrapper e2) {
		if (e1.getElement() != null && e2.getElement() != null) {
			return ((IElement) e1).similarity((IElement) e2) == 1.0;
		} else {
			return false;
		}
	}

	public static double similarity(ElementWrapper e1, ElementWrapper e2) {
		if (e1.getElement() != null && e2.getElement() != null) {
			return ((IElement) e1).similarity((IElement) e2);
		} else {
			return 0;
		}
	}

	public static List<ElementWrapper> findElementWrappers(List<AdaptedArtefact> artefacts, IElement ie) {
		// expensive task, it is better if you use a hashmap IElement,
		// IElementWrappers instead
		List<ElementWrapper> elementWrappers = new ArrayList<ElementWrapper>();
		for (AdaptedArtefact artefact : artefacts) {
			for (ElementWrapper ew : artefact.getOwnedElementWrappers()) {
				if (ew.getElement().equals(ie)) {
					elementWrappers.add(ew);
				}
			}
		}
		return elementWrappers;
	}

	public static List<Block> checkBlockNames(List<Block> blocks) {
		int i = 0;
		for (Block block : blocks) {
			if (block.getName() == null || block.getName().isEmpty()) {
				block.setName("Block " + getNumberWithZeros(i, blocks.size() - 1));
				i++;
			}
		}
		return blocks;
	}
	
	public static List<Artefact> getArtefactsContainingBlockElement(BlockElement blockElement) {
		List<Artefact> artefacts = new ArrayList<Artefact>();
		for (ElementWrapper ew : blockElement.getElementWrappers()) {
			AdaptedArtefact aa = (AdaptedArtefact) ew.eContainer();
			if (!artefacts.contains(aa.getArtefact())) {
				artefacts.add(aa.getArtefact());
			}
		}
		return artefacts;
	}

	public static String getNumberWithZeros(int number, int maxNumber) {
		String _return = String.valueOf(number);
		for (int zeros = _return.length(); zeros < String.valueOf(maxNumber).length(); zeros++) {
			_return = "0" + _return;
		}
		return _return;
	}

	/**
	 * Get the name of the artefact model
	 * @param adaptedModel
	 * @return the name or null
	 */
	public static String getName(AdaptedModel adaptedModel) {
		for(AdaptedArtefact aa : adaptedModel.getOwnedAdaptedArtefacts()){
			ArtefactModel a = (ArtefactModel)aa.getArtefact().eContainer();
			if(a.getName()!=null && a.getName().length()>0){
				return a.getName();
			}
		}
		return null;
	}

}
