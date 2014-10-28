package org.but4reuse.adaptedmodel.helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.AdaptedModelFactory;
import org.but4reuse.adaptedmodel.Block;
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
	 * @param artefactModel
	 * @param adapters
	 * @param monitor
	 * @return
	 * @throws InterruptedException 
	 */
	public static AdaptedModel adapt(ArtefactModel artefactModel,
			List<IAdapter> adapters, IProgressMonitor monitor) throws InterruptedException {
		Date date = new Date();String datestart = date.toString();
		
		AdaptedModel adaptedModel = AdaptedModelFactory.eINSTANCE.createAdaptedModel();
		ArrayList<AdaptedModelThread> ListThread = new ArrayList<AdaptedModelThread>();
		
		for (Artefact artefact : artefactModel.getOwnedArtefacts()) {
			if (artefact.isActive()) {
				AdaptedArtefact adaptedArtefact = AdaptedModelFactory.eINSTANCE.createAdaptedArtefact();
				AdaptedModelThread th = new AdaptedModelThread(adaptedArtefact, adaptedModel,artefact, adapters, monitor);
				ListThread.add(th);
				
				
			}
		}
		for(AdaptedModelThread th : ListThread){
		th.start();
		}
		
		for(AdaptedModelThread th : ListThread){
			th.join();
		}
		
		//while (adaptedModel.getOwnedAdaptedArtefacts().size() != artefactModel.getOwnedArtefacts().size()){}
		System.out.println(datestart);
		Date dateend = new Date();
		System.out.println(dateend.toString());
		return adaptedModel;
		
	}
	
	public static boolean isIdentical(ElementWrapper e1, ElementWrapper e2){
		if(e1.getElement()!=null && e2.getElement()!=null){
			return ((IElement)e1).similarity((IElement)e2)==1.0;
		} else {
			return false;
		}
	}
	
	public static double similarity(ElementWrapper e1, ElementWrapper e2){
		if(e1.getElement()!=null && e2.getElement()!=null){
			return ((IElement)e1).similarity((IElement)e2);
		} else {
			return 0;
		}
	}

	
	public static List<ElementWrapper> findElementWrappers(List<AdaptedArtefact> artefacts, IElement ie) {
		// expensive task, it is better if you use a hashmap IElement, IElementWrappers instead
		List<ElementWrapper> elementWrappers = new ArrayList<ElementWrapper>();
		for(AdaptedArtefact artefact : artefacts){
			for(ElementWrapper ew : artefact.getOwnedElementWrappers()){
				if(ew.getElement().equals(ie)){
					elementWrappers.add(ew);
				}
			}
		}
		return elementWrappers;
	}

	public static List<Block> checkBlockNames(List<Block> blocks) {
		int i = 0;
		for(Block block : blocks){
			if(block.getName()==null || block.getName().isEmpty()){
				block.setName("Block " + getNumberWithZeros(i,blocks.size() - 1));
				i++;
			}
		}
		return blocks;
	}
	
	public static String getNumberWithZeros(int number, int maxNumber) {
		String _return = String.valueOf(number);
		for (int zeros = _return.length(); zeros < String.valueOf(maxNumber).length(); zeros++) {
			_return = "0" + _return;
		}
		return _return;
	}

}
