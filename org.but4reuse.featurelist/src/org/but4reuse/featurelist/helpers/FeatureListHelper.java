package org.but4reuse.featurelist.helpers;

import java.net.URI;
import java.net.URISyntaxException;

import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.emf.EMFUtils;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Feature List Helper
 * 
 * @author jabier.martinez
 */
public class FeatureListHelper {

	/**
	 * Get associated artefact model
	 * 
	 * @param featureList
	 * @return
	 */
	public static ArtefactModel getArtefactModel(FeatureList featureList) {
		// get it from direct link
		if(featureList.getArtefactModel()!=null){
			return featureList.getArtefactModel();
		}
		// get it from the associated artefacts
		for (Feature feature : featureList.getOwnedFeatures()) {
			for (Artefact artefact : feature.getImplementedInArtefacts()) {
				return (ArtefactModel) EcoreUtil.getRootContainer(artefact);
			}
		}
		// get it from the resource
		if (featureList.eResource().getResourceSet().getResources().size() > 1) {
			org.eclipse.emf.common.util.URI artefactModelURI = featureList.eResource().getResourceSet().getResources()
					.get(1).getURI();
			try {
				Object o = EMFUtils.getEObject(new URI(artefactModelURI.toString()));
				if (o instanceof ArtefactModel) {
					return (ArtefactModel) o;
				}
			} catch (URISyntaxException e) {
				return null;
			}
		}

		return null;
	}
	
	/**
	 * get feature by id
	 * @param featureList
	 * @param featureId
	 * @return the feature or null
	 */
	public static Feature getFeature(FeatureList featureList, String featureId){
		for (Feature feature : featureList.getOwnedFeatures()) {
			if(feature.getId().equals(featureId)){
				return feature;
			}
		}
		return null;
	}
}
