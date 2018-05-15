package org.but4reuse.adapters.eclipse.benchmark.generator.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;

/**
 * SPLOT format utils
 * 
 * @author Julien Margarido
 * @author Felix Lima Gorito
 */
public class SplotUtils {

	public static boolean exportToSPLOT(File outputFile, List<ActualFeature> allFeatures) {

		if (allFeatures == null || allFeatures.isEmpty())
			return false;

		StringBuilder splotToReturn = new StringBuilder();

		Map<String, ActualFeature> mapIdWithFeature = new HashMap<String, ActualFeature>();
		for (ActualFeature oneFeature : allFeatures) {
			mapIdWithFeature.put(oneFeature.getId(), oneFeature);
		}

		splotToReturn.append("<feature_model name=\"Eclipse Feature\">\n");
		splotToReturn.append("<meta>\n");
		splotToReturn.append("</meta>\n");
		splotToReturn.append("<feature_tree>\n");
		splotToReturn.append("  :r Eclipse Feature(EclipseFeature)\n");

		for (int i = 0; i < allFeatures.size(); i++) {
			ActualFeature oneFeat = allFeatures.get(i);
			splotToReturn.append("\t:o " + fixIdForNonAcceptedChars(oneFeat.getId()) + "\n");
		}

		splotToReturn.append("</feature_tree>\n");
		splotToReturn.append("<constraints>");
		int nbConstraints = 1;

		for (int i = 0; i < allFeatures.size(); i++) {
			ActualFeature oneFeat = allFeatures.get(i);

			List<String> allDependencies = new ArrayList<String>();
			if (oneFeat.getRequiredFeatures() != null) {
				allDependencies = oneFeat.getRequiredFeatures();
			}
			if (oneFeat.getIncludedFeatures() != null) {
				for (String s : oneFeat.getIncludedFeatures()) {
					if (!allDependencies.contains(s)) {
						allDependencies.add(s);
					}
				}
			}
			for (int j = 0; j < allDependencies.size(); j++) {
				if (allDependencies.get(j) != null) {
					if (mapIdWithFeature.get(allDependencies.get(j)) != null) {
						splotToReturn.append("\n");
						splotToReturn.append("constraint_" + nbConstraints + ":");
						splotToReturn.append("~" + fixIdForNonAcceptedChars(oneFeat.getId()) + " or "
								+ fixIdForNonAcceptedChars(mapIdWithFeature.get(allDependencies.get(j)).getId()));
						nbConstraints++;
					}
				}
			}
		}

		splotToReturn.append("\n</constraints>\n");
		splotToReturn.append("</feature_model>\n");

		try {
			if(!outputFile.getParentFile().exists()){
				outputFile.getParentFile().mkdirs();
			}
			FileWriter fileWriter = new FileWriter(outputFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(splotToReturn.toString());

			bufferedWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// TODO find a cleaner solution to this. dot is not accepted
	public static String fixIdForNonAcceptedChars(String id) {
		return id.replace(".", "___").replace("-", "666666").replace("(", "555555").replace(")", "°°°°°°");
	}

	public static String reverseFixIdForNonAcceptedChars(String id) {
		return id.replace("___", ".").replace("555555", "(").replace("°°°°°°", "(").replace("666666", "-");
	}

}
