package org.but4reuse.adaptedmodel.helpers;

import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.AdaptedModelFactory;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.eclipse.core.runtime.IProgressMonitor;

public class AdaptedModelHelper {
	/**
	 * 
	 * @param artefactModel
	 * @param adapters
	 * @param monitor
	 * @return
	 */
	public static AdaptedModel adapt(ArtefactModel artefactModel,
			List<IAdapter> adapters, IProgressMonitor monitor) {
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
				for(IElement ele : list){
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

	public static ElementWrapper findElementWrapper(List<AdaptedArtefact> artefacts, IElement ie) {
		for(AdaptedArtefact artefact : artefacts){
			for(ElementWrapper ew : artefact.getOwnedElementWrappers()){
				if(ew.getElement() == ie){
					return ew;
				}
			}
		}
		return null;
	}
}
