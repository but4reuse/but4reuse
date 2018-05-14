package org.but4reuse.featurelist.helpers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.featurelist.FeatureListFactory;
import org.but4reuse.utils.emf.EMFUtils;
import org.eclipse.emf.common.util.EList;
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
		if (featureList.getArtefactModel() != null) {
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
	 * 
	 * @param featureList
	 * @param featureId
	 * @return the feature or null
	 */
	public static Feature getFeature(FeatureList featureList, String featureId) {
		for (Feature feature : featureList.getOwnedFeatures()) {
			if (feature.getId().equals(featureId)) {
				return feature;
			}
		}
		return null;
	}
	
	/**
	 * get feature by name
	 * 
	 * @param featureList
	 * @param feature name
	 * @return the feature or null
	 */
	public static Feature getFeatureByName(FeatureList featureList, String featureName) {
		for (Feature feature : featureList.getOwnedFeatures()) {
			if (feature.getName().equals(featureName)) {
				return feature;
			}
		}
		return null;
	}

	/**
	 * Get the negations of the features
	 * 
	 * @param featureList
	 * @param artefactModel
	 * @return
	 */
	public static List<Feature> getFeatureNegations(List<Feature> featureList, ArtefactModel artefactModel) {
		List<Feature> negations = new ArrayList<Feature>();
		for (Feature f : featureList) {
			Feature noF = FeatureListFactory.eINSTANCE.createFeature();
			noF.setName("No" + f.getName());
			noF.setId("No" + f.getId());
			noF.setNegationFeatureOf(f);
			for (Artefact a : artefactModel.getOwnedArtefacts()) {
				if (!f.getImplementedInArtefacts().contains(a)) {
					noF.getImplementedInArtefacts().add(a);
				}
			}
			if (!noF.getImplementedInArtefacts().isEmpty()) {
				negations.add(noF);
			}
		}
		return negations;
	}

	/**
	 * Get the two wise feature interactions
	 * 
	 * @param features
	 * @param artefactModel
	 * @return
	 */
	public static List<Feature> get2WiseFeatureInteractions(EList<Feature> features, ArtefactModel artefactModel) {
		List<Feature> twoWise = new ArrayList<Feature>();
		for (Feature f1 : features) {
			for (Feature f2 : features) {
				if (f1 != f2 && features.indexOf(f1) < features.indexOf(f2)) {
					Feature newF = FeatureListFactory.eINSTANCE.createFeature();
					newF.setName("I_" + f1.getName() + "_" + f2.getName());
					newF.setId("I_" + f1.getId() + "_" + f2.getId());
					newF.getInteractionFeatureOf().add(f1);
					newF.getInteractionFeatureOf().add(f2);
					for (Artefact a : artefactModel.getOwnedArtefacts()) {
						if (f1.getImplementedInArtefacts().contains(a) && f2.getImplementedInArtefacts().contains(a)) {
							newF.getImplementedInArtefacts().add(a);
						}
					}
					if (!newF.getImplementedInArtefacts().isEmpty()) {
						twoWise.add(newF);
					}
				}
			}
		}
		return twoWise;
	}

	/**
	 * Get the two wise feature interactions
	 * 
	 * @param features
	 * @param artefactModel
	 * @return
	 */
	public static List<Feature> get3WiseFeatureInteractions(EList<Feature> features, ArtefactModel artefactModel) {
		List<Feature> threeWise = new ArrayList<Feature>();
		for (Feature f1 : features) {
			for (Feature f2 : features) {
				if (f1 != f2 && features.indexOf(f1) < features.indexOf(f2)) {
					for (Feature f3 : features) {
						if (f2 != f3 && features.indexOf(f2) < features.indexOf(f3)) {
							Feature newF = FeatureListFactory.eINSTANCE.createFeature();
							newF.setName("I3_" + f1.getName() + "_" + f2.getName() + "_" + f3.getName());
							newF.setId("I3_" + f1.getId() + "_" + f2.getId() + "_" + f3.getId());
							newF.getInteractionFeatureOf().add(f1);
							newF.getInteractionFeatureOf().add(f2);
							newF.getInteractionFeatureOf().add(f3);
							for (Artefact a : artefactModel.getOwnedArtefacts()) {
								if (f1.getImplementedInArtefacts().contains(a)
										&& f2.getImplementedInArtefacts().contains(a)
										&& f3.getImplementedInArtefacts().contains(a)) {
									newF.getImplementedInArtefacts().add(a);
								}
							}
							if (!newF.getImplementedInArtefacts().isEmpty()) {
								threeWise.add(newF);
							}
						}
					}
				}
			}
		}
		return threeWise;
	}

	/**
	 * TODO implement it with N as parameter
	 * 
	 * @param features
	 * @param artefactModel
	 * @return
	 */
	public static List<Feature> get4WiseFeatureInteractions(EList<Feature> features, ArtefactModel artefactModel) {
		List<Feature> fourWise = new ArrayList<Feature>();
		for (Feature f1 : features) {
			for (Feature f2 : features) {
				if (f1 != f2 && features.indexOf(f1) < features.indexOf(f2)) {
					for (Feature f3 : features) {
						if (f2 != f3 && features.indexOf(f2) < features.indexOf(f3)) {
							for (Feature f4 : features) {
								if (f3 != f4 && features.indexOf(f3) < features.indexOf(f4)) {
									Feature newF = FeatureListFactory.eINSTANCE.createFeature();
									newF.setName("I4_" + f1.getName() + "_" + f2.getName() + "_" + f3.getName() + "_"
											+ f4.getName());
									newF.setId("I4_" + f1.getName() + "_" + f2.getName() + "_" + f3.getName() + "_"
											+ f4.getName());
									newF.getInteractionFeatureOf().add(f1);
									newF.getInteractionFeatureOf().add(f2);
									newF.getInteractionFeatureOf().add(f3);
									newF.getInteractionFeatureOf().add(f4);
									for (Artefact a : artefactModel.getOwnedArtefacts()) {
										if (f1.getImplementedInArtefacts().contains(a)
												&& f2.getImplementedInArtefacts().contains(a)
												&& f3.getImplementedInArtefacts().contains(a)
												&& f4.getImplementedInArtefacts().contains(a)) {
											newF.getImplementedInArtefacts().add(a);
										}
									}
									if (!newF.getImplementedInArtefacts().isEmpty()) {
										fourWise.add(newF);
									}
								}
							}
						}
					}
				}
			}
		}
		return fourWise;
	}

	public static void mergeFeaturesImplementedInTheSameArtefacts(FeatureList featureList, ArtefactModel artefactModel) {
		List<Feature> toBeRemoved = new ArrayList<Feature>();
		for (Feature f1 : featureList.getOwnedFeatures()) {
			for (Feature f2 : featureList.getOwnedFeatures()) {
				if (f1 != f2 && !toBeRemoved.contains(f1)) {
					Set<Object> set1 = new HashSet<Object>();
					set1.addAll(f1.getImplementedInArtefacts());
					Set<Object> set2 = new HashSet<Object>();
					set2.addAll(f2.getImplementedInArtefacts());
					if (set1.equals(set2)) {
						toBeRemoved.add(f2);
						f1.setName(f1.getName() + "__" + f2.getName());
						f1.setId(f1.getId() + "__" + f2.getId());
					}
				}
			}
		}
		for (Feature re : toBeRemoved) {
			featureList.getOwnedFeatures().remove(re);
		}
	}

	/**
	 * Check if a feature is a core/base feature?
	 * 
	 * @param artefactModel
	 * @param feature
	 * @return if the feature is present in all artefacts
	 */
	public static boolean isCoreFeature(ArtefactModel artefactModel, Feature f) {
		if (f.getImplementedInArtefacts().containsAll(artefactModel.getOwnedArtefacts())) {
			return true;
		}
		return false;
	}

	/**
	 * Get artefact features
	 * 
	 * @param featureList
	 * @param artefact
	 * @return the list of features of this artifact
	 */
	public static List<Feature> getArtefactFeatures(FeatureList featureList, Artefact a) {
		List<Feature> features = new ArrayList<Feature>();
		for (Feature f : featureList.getOwnedFeatures()) {
			if (f.getImplementedInArtefacts().contains(a)) {
				features.add(f);
			}
		}
		return features;
	}
	
	/**
	 * Average Jaccard similarity
	 * @param featureList
	 * @return Jaccard similarity
	 */
	public static double getJaccardSimilarity(FeatureList featureList) {
		ArtefactModel am = FeatureListHelper.getArtefactModel(featureList);

		List<List<Feature>> artefactAndFeatures = new ArrayList<List<Feature>>();
		for (Artefact a : am.getOwnedArtefacts()) {
			List<Feature> features = FeatureListHelper.getArtefactFeatures(featureList, a);
			artefactAndFeatures.add(features);
		}

		double pairs = 0;
		double accumulatedJaccard = 0;
		for (int i = 0; i < artefactAndFeatures.size(); i++) {
			for (int j = i + 1; j < artefactAndFeatures.size(); j++) {
				pairs += 1;
				double jaccard = getJaccardSimilarity(artefactAndFeatures.get(i), artefactAndFeatures.get(j));
				accumulatedJaccard += jaccard;
			}
		}
		double result = accumulatedJaccard / pairs;
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static double getJaccardSimilarity(Collection<?> s1, Collection<?> s2) {
		double sum = s1.size() + s2.size();
		Set<?> intersection = new HashSet(s1); // use the copy constructor
		intersection.retainAll(s2);
		return 1 - ((sum - 2 * intersection.size()) / (sum - intersection.size()));
	}
}
