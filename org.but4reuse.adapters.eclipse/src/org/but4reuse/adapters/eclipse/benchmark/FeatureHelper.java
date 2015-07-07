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
	public static final String FEATURES_FOLDER = "features";

	public static List<ActualFeature> getFeaturesOfEclipse(String eclipseInstallationURI) {
		List<ActualFeature> actualFeatures = new ArrayList<ActualFeature>();
		try {
			File eclipseFile = FileUtils.getFile(new URI(eclipseInstallationURI));
			File featuresFolder = new File(eclipseFile, FEATURES_FOLDER);
			for (File fFolder : featuresFolder.listFiles()) {
				if (isAFeature(fFolder)) {
					ActualFeature f = FeatureInfosExtractor.getFeatureInfos(fFolder.getAbsolutePath());
					// we assume not repeated features
					actualFeatures.add(f);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actualFeatures;
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
