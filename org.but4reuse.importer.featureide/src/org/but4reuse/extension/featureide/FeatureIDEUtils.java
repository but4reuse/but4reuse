package org.but4reuse.extension.featureide;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.but4reuse.utils.files.FileUtils;

/**
 * 
 * @author jabier.martinez
 */
public class FeatureIDEUtils {

	/**
	 * 
	 * @param featureModelURI
	 * @param features
	 * @throws IOException
	 */
	public static void createFeatureModel(URI featureModelURI, List<String> features) throws IOException {
		// TODO Use FeatureIDE API
		File fmFile = FileUtils.getFile(featureModelURI);
		FileUtils.createFile(fmFile);
		BufferedWriter output = new BufferedWriter(new FileWriter(fmFile));
		output.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		output.newLine();
		output.append("<featureModel chosenLayoutAlgorithm=\"1\">");
		output.newLine();
		output.append("<struct>");
		output.newLine();
		output.append("<and abstract=\"true\" mandatory=\"true\" name=\"SPL\">");
		output.newLine();
		for (String feature : features) {
			output.append("<feature name=\"" + feature.replaceAll(" ", "") + "\"/>");
			output.newLine();
		}
		output.append("</and>");
		output.newLine();
		output.append("</struct>");
		output.newLine();
		output.append("<constraints/>");
		output.newLine();
		output.append("<calculations Auto=\"true\" Constraints=\"true\" Features=\"true\" Redundant=\"true\"/>");
		output.newLine();
		output.append("<comments/>");
		output.newLine();
		output.append("<featureOrder userDefined=\"false\"/>");
		output.newLine();
		output.append("</featureModel>");
		output.close();
	}

	/**
	 * 
	 * @param configsFolderURI
	 * @param configurations
	 * @throws URISyntaxException
	 * @throws IOException 
	 */
	public static void createConfigurations(URI configsFolderURI, Map<String, List<String>> configurations)
			throws URISyntaxException, IOException {
		for (String key : configurations.keySet()) {
			URI configURI = new URI(configsFolderURI + key + ".config");
			List<String> features = configurations.get(key);
			createConfiguration(configURI, features);
		}
	}

	/**
	 * 
	 * @param configURI
	 * @param feature
	 * @throws IOException 
	 */
	public static void createConfiguration(URI configURI, List<String> features) throws IOException {
		File configFile = FileUtils.getFile(configURI);
		FileUtils.createFile(configFile);
		BufferedWriter output = new BufferedWriter(new FileWriter(configFile));
		for (String feature : features){
			output.append(feature.replaceAll(" ", ""));
			output.newLine();
		}
		output.close();
	}

}
