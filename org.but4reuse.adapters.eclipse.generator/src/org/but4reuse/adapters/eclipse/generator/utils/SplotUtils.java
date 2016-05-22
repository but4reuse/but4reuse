package org.but4reuse.adapters.eclipse.generator.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;

public class SplotUtils {

	public static File exportToSPLOT(List<ActualFeature> allFeatures) {

		if (allFeatures == null || allFeatures.isEmpty())
			return null;

		String splotToReturn = "";

		Map<String, ActualFeature> mapIdWithFeature = new HashMap<>();
		for (ActualFeature oneFeature : allFeatures) {
			mapIdWithFeature.put(oneFeature.getId(), oneFeature);
		}

		splotToReturn += "<feature_model name=\"Eclipse Feature\">\n";
		splotToReturn += "<meta>\n";
		splotToReturn += "</meta>\n";
		splotToReturn += "<feature_tree>\n";
		splotToReturn += "  :r Eclipse Feature(EclipseFeature)\n";

		for (int i = 0; i < allFeatures.size(); i++) {
			ActualFeature oneFeat = allFeatures.get(i);
			String name = oneFeat.getName().replace("(", "");
			name = name.replace(")", "");

			String id = oneFeat.getId().replace("(", "555555").replace(")", "°°°°°°").replace(".", "111111")
					.replace("-", "666666");

			splotToReturn += "\t:o " + name + "(" + id + ")";
			splotToReturn += "\n";
		}

		splotToReturn += "</feature_tree>\n";
		splotToReturn += "<constraints>";
		int nbConstraints = 1;

		for (int i = 0; i < allFeatures.size(); i++) {
			ActualFeature oneFeat = allFeatures.get(i);

			String id = oneFeat.getId().replace("(", "555555").replace(")", "°°°°°°").replace(".", "111111")
					.replace("-", "666666");

			List<String> allDependencies = new ArrayList<String>();
			if (oneFeat.getRequiredFeatures() != null) {
				allDependencies = oneFeat.getRequiredFeatures();
			}
			if (oneFeat.getIncludedFeatures() != null) {
				for (String s : oneFeat.getIncludedFeatures()) {
					if (!allDependencies.contains(s))
						allDependencies.add(s);
				}
			}
			for (int j = 0; j < allDependencies.size(); j++) {
				if (allDependencies.get(j) != null) {
					if (mapIdWithFeature.get(allDependencies.get(j)) != null) {
						splotToReturn += "\n";
						splotToReturn += "constraint_" + nbConstraints + ":";
						splotToReturn += "~"
								+ id
								+ " or "
								+ mapIdWithFeature.get(allDependencies.get(j)).getId().replace(".", "111111")
										.replace("-", "666666").replace("(", "555555").replace(")", "°°°°°°");
						nbConstraints++;
					}
				}
			}
		}

		splotToReturn += "\n</constraints>\n";
		splotToReturn += "</feature_model>\n";

		String filename = "EclipseFeature.xml";
		Map<String, String> prefMap = null;
		String output = "";
		try {
			prefMap = PreferenceUtils.getPreferences();
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
		output = prefMap.get(PreferenceUtils.PREF_OUTPUT) + File.separator + filename;

		BufferedWriter bufferedWriter = null;
		try {
			FileWriter fileWriter = new FileWriter(output);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(splotToReturn);

			bufferedWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new File(output);
	}

}
