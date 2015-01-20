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
	static boolean proceed;
	static int[] nbready;
	
	/**
	 * Adapt an artefact model to create the list of elements of each artefact
	 * @param artefactModel
	 * @param adapters
	 * @param monitor
	 * @return
	 * @throws InterruptedException 
	 * 
	 * 
	 * 
	 */
	
	
	
	
	public static AdaptedModel adapt(ArtefactModel artefactModel,
			List<IAdapter> adapters, IProgressMonitor monitor) throws InterruptedException {
		long startTime = System.nanoTime();
		proceed = false;
		nbready = new int[1];
		nbready[0]=0;
			
		AdaptedModel adaptedModel = AdaptedModelFactory.eINSTANCE.createAdaptedModel();
		ArrayList<AdaptedModelThread> ListThread = new ArrayList<AdaptedModelThread>();
		int i =0;
		AdaptedArtefact[] tabAdp = new AdaptedArtefact[artefactModel.getOwnedArtefacts().size()];
		
		
		for (Artefact artefact : artefactModel.getOwnedArtefacts()) {
			if (artefact.isActive()) {
				
				
				AdaptedModelThread th = new AdaptedModelThread(nbready,proceed,tabAdp, adaptedModel,artefact, adapters, monitor,i);
				ListThread.add(th);
				
				i++;
				
			}
		}
		for(AdaptedModelThread th : ListThread){
			
			th.start();
		}
		
		/*while(nbready[0]!= artefactModel.getOwnedArtefacts().size()){
			
			System.out.println(nbready[0]);
		}*/
		
		for(AdaptedModelThread th : ListThread){
			th.join();
		}
		
		for(int j=0;j<tabAdp.length;j++){
			
			adaptedModel.getOwnedAdaptedArtefacts().add(tabAdp[j]);
			
		}
		
		
		//while (adaptedModel.getOwnedAdaptedArtefacts().size() != artefactModel.getOwnedArtefacts().size()){}
		long endTime = System.nanoTime();

		long duration = (endTime - startTime);
		System.out.println(duration);
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
