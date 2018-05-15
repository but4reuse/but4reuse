package org.but4reuse.adapters.eclipse.benchmark;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.utils.files.FileUtils;

/**
 * Feature helper
 * 
 * @author jabier.martinez
 */
public class FeatureHelper {

	public static final String FEATURE_XML = "feature.xml";
	public static final String FEATURE_PROPERTIES = "feature.properties";
	public static final String FEATURES_FOLDER = "features";

	public static List<ActualFeature> getFeaturesOfEclipse(String eclipseInstallationURI) throws Exception {
		List<ActualFeature> actualFeatures = new ArrayList<ActualFeature>();
		File eclipseFile = FileUtils.getFile(new URI(eclipseInstallationURI));
		File featuresFolder = new File(eclipseFile, FEATURES_FOLDER);
		for (File fFolder : featuresFolder.listFiles()) {
			if (isAFeature(fFolder)) {
				ActualFeature f = FeatureInfosExtractor.getFeatureInfos(fFolder.getAbsolutePath());
				// we assume not repeated features
				actualFeatures.add(f);
			}
		}
		return actualFeatures;
	}

	public static List<ActualFeature> getAllIncludedFeatures(List<ActualFeature> allFeatures, ActualFeature feature) {
		if (allFeatures == null || feature == null)
			return null;

		List<ActualFeature> includedFeatures = new ArrayList<ActualFeature>(feature.getIncludedFeatures().size());
		for (String featureId : feature.getIncludedFeatures()) {
			ActualFeature oneIncludedFeature = getFeatureById(allFeatures, featureId);
			if (oneIncludedFeature != null)
				includedFeatures.add(oneIncludedFeature);
		}

		return includedFeatures;
	}

	public static List<ActualFeature> getAllRequiredFeatures(List<ActualFeature> allFeatures, ActualFeature feature) {
		if (allFeatures == null || feature == null)
			return null;

		List<ActualFeature> requiredFeatures = new ArrayList<ActualFeature>(feature.getRequiredFeatures().size());
		for (String featureId : feature.getRequiredFeatures()) {
			ActualFeature oneRequiredFeature = getFeatureById(allFeatures, featureId);
			if (oneRequiredFeature != null)
				requiredFeatures.add(oneRequiredFeature);
		}

		return requiredFeatures;
	}

	public static ActualFeature getFeatureById(List<ActualFeature> allFeatures, String id) {
		if (id == null || id.isEmpty() || allFeatures == null)
			return null;

		for (ActualFeature feature : allFeatures) {
			if (feature.getId().equals(id))
				return feature;
		}
		return null;
	}

	public static boolean isAFeature(File file) {
		if (file.getParentFile().getName().equals(FEATURES_FOLDER)) {
			if (file.isDirectory()) {
				File manif = new File(file, FEATURE_XML);
				if (manif.exists()) {
					return true;
				}
			}
		}
		return false;
	}
}
