package org.but4reuse.adaptedmodel.helpers;

import java.util.ArrayList;
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


public class AdaptedModelThread extends Thread{

	AdaptedArtefact adaptedArtefact;
	AdaptedModel adaptedModel;
	Artefact artefact;
	List<IAdapter> adapters;
	IProgressMonitor monitor;
	
	AdaptedModelThread(AdaptedArtefact adaptedArt, AdaptedModel adaptedMod, Artefact art,List<IAdapter> adapts, IProgressMonitor mon){
		adaptedArtefact=adaptedArt;
		adaptedModel=adaptedMod;
		artefact = art;
		adapters = adapts;
		monitor = mon;
	}
	
	public void run() {
	    
		adaptedArtefact.setArtefact(artefact);
		
		String name = artefact.getName();
		if (name == null || name.length() == 0) {
			name = artefact.getArtefactURI();
		}
		monitor.subTask("Adapting: " + name);

		List<IElement> list = AdaptersHelper.getElements(artefact, adapters);
		for(IElement ele : list){
			ElementWrapper ew = AdaptedModelFactory.eINSTANCE.createElementWrapper();
			ew.setElement(ele);
			adaptedArtefact.getOwnedElementWrappers().add(ew);
		}
		
		adaptedModel.getOwnedAdaptedArtefacts().add(adaptedArtefact);
		monitor.worked(1);
		if (monitor.isCanceled()) {
			this.stop();
		}
		
	  }
	
}
