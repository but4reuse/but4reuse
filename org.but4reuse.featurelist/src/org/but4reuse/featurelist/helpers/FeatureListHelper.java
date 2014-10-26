package org.but4reuse.featurelist.helpers;

import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Feature List Helper
 * @author jabier.martinez
 */
public class FeatureListHelper {
	
	/**
	 * Get associated artefact model
	 * @param featureList
	 * @return
	 */
	public static ArtefactModel getArtefactModel(FeatureList featureList) {
		for(Feature feature : featureList.getOwnedFeatures()){
			for(Artefact artefact : feature.getImplementedInArtefacts()){
				return (ArtefactModel)EcoreUtil.getRootContainer(artefact);
			}
		}
		return null;
	}
}
