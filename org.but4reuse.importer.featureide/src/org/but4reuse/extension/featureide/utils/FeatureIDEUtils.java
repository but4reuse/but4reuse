package org.but4reuse.extension.featureide.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.extension.featureide.IFeatureModelCreator;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.core.resources.IFile;
import org.prop4j.Node;
import org.prop4j.NodeReader;

import de.ovgu.featureide.fm.core.Constraint;
import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.core.io.FeatureModelWriterIFileWrapper;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelWriter;

/**
 * Feature IDE Utils
 * @author jabier.martinez
 */
public class FeatureIDEUtils {

	public static void exportFeatureModel(URI featureModelURI, AdaptedModel adaptedModel, IFeatureModelCreator fmCreator) {
		File fmFile = FileUtils.getFile(featureModelURI);
		try {
			FileUtils.createFile(fmFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		FeatureModel fm = fmCreator.createFeatureModel(adaptedModel);
		save(fm, fmFile);
		// Refresh in case of workspace
		IFile file = WorkbenchUtils.getIFileFromFile(fmFile);
		if (file != null) {
			WorkbenchUtils.refreshIResource(file);
		}
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
		for (String feature : features) {
			output.append(validFeatureName(feature));
			output.newLine();
		}
		output.close();
	}

	/**
	 * Get string representation of the constraint
	 * @param constraint
	 * @return
	 */
	public static String getConstraintString(IConstraint constraint) {
		String type = constraint.getType();
		// only this two supported for the moment
		if (type.equals(IConstraint.REQUIRES) || type.equals(IConstraint.EXCLUDES)) {
			String text = validFeatureName(constraint.getBlock1().getName()) + " implies ";
			if (type.equals(IConstraint.EXCLUDES)) {
				text += "not ";
			}
			text += validFeatureName(constraint.getBlock2().getName());
			return text;
		}
		return null;
	}

	/**
	 * Save
	 * @param featureModel
	 * @param file
	 */
	public static void save(FeatureModel featureModel, File file) {
		new FeatureModelWriterIFileWrapper(new XmlFeatureModelWriter(featureModel)).writeToFile(file);
	}

	/**
	 * Add constraint
	 * @param featureModel
	 * @param constraint
	 */
	public static void addConstraint(FeatureModel featureModel, String constraint) {
		NodeReader nodeReader = new NodeReader();
		List<String> featureList = new ArrayList<String>(featureModel.getFeatureNames());
		Node node = nodeReader.stringToNode(constraint, featureList);
		// TODO report error if node is null
		if(node!=null){
			Constraint c = new Constraint(featureModel, node);
			featureModel.addConstraint(c);
		}
	}
	
	/**
	 * Feature IDE has some restrictions like no whitespaces etc.
	 * They say that the feature name must be a valid Java identifier.
	 * @param name
	 * @return
	 */
	public static String validFeatureName(String name){
		// TODO improve checks
		return name.replaceAll(" ", "");
	}

}
