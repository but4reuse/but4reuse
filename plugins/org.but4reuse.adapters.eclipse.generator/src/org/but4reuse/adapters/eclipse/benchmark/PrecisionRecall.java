package org.but4reuse.adapters.eclipse.benchmark;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.utils.files.FileUtils;

/**
 * Precision and Recall
 * 
 * Precision: Have I retrieved a lot of junk?
 * 
 * Recall: Have I found them?
 * 
 * F1: Accuracy metric that combines precision and recall
 * 
 * @author jabier.martinez
 * 
 */
public class PrecisionRecall {

	/**
	 * Create results file content
	 * 
	 * @param actualFolder
	 *            containing txt files with the actual values
	 * @param retrievedFolder
	 *            containing txt files with the retrieved values
	 */
	public static String createResultsFile(File actualFolder, File retrievedFolder) {
		double precisionAverage = 0;
		double recallAverage = 0;
		double f1measureAverage = 0;
		double precisionCounter = 0;
		double recallCounter = 0;
		double f1measureCounter = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("Name;Precision;Recall;FScore\n");
		int abstractFeatures = 0;
		for (File f : actualFolder.listFiles()) {
			String name = f.getName().substring(0, f.getName().length() - ".txt".length());
			File f2 = new File(retrievedFolder, f.getName());
			List<String> actualLines = FileUtils.getLinesOfFile(f);
			if (!actualLines.isEmpty()) {
				List<String> retrievedLines = FileUtils.getLinesOfFile(f2);
				double precision = getPrecision(actualLines, retrievedLines);
				double recall = getRecall(actualLines, retrievedLines);
				double f1measure = getF1(precision, recall);
				if (!Double.isNaN(precision)) {
					precisionAverage += precision;
					precisionCounter += 1.0;
				}
				if (!Double.isNaN(recall)) {
					recallAverage += recall;
					recallCounter += 1.0;
				}
				if (!Double.isNaN(f1measure)) {
					f1measureAverage += f1measure;
					f1measureCounter += 1.0;
				}
				sb.append(name + ";" + precision + ";" + recall + ";" + f1measure + "\n");
			} else {
				abstractFeatures++;
			}
		}
		sb.append("AVERAGE;" + precisionAverage / precisionCounter + ";" + recallAverage / recallCounter + ";"
				+ f1measureAverage / f1measureCounter + "\n");
		System.out.println(abstractFeatures + " were actually abstract features");

		return sb.toString();
	}

	/**
	 * From the retrieved elements, those that are on the actual list
	 * 
	 * @param actualLines
	 * @param retrievedLines
	 * @return
	 */
	public static List<String> getTruePositives(List<String> actualLines, List<String> retrievedLines) {
		List<String> found = new ArrayList<String>();
		for (String a : retrievedLines) {
			if (actualLines.contains(a)) {
				found.add(a);
			}
		}
		return found;
	}

	/**
	 * From the retrieved elements, those that are not on the actual list
	 * 
	 * @param actualLines
	 * @param retrievedLines
	 * @return
	 */
	public static List<String> getFalsePositives(List<String> actualLines, List<String> retrievedLines) {
		List<String> found = new ArrayList<String>();
		for (String a : retrievedLines) {
			if (!actualLines.contains(a)) {
				found.add(a);
			}
		}
		return found;
	}

	public static double getPrecision(List<String> actualLines, List<String> retrievedLines) {
		List<String> truePositives = getTruePositives(actualLines, retrievedLines);
		List<String> falsePositives = getFalsePositives(actualLines, retrievedLines);
		double precision = (double) truePositives.size()
				/ (double) ((double) truePositives.size() + (double) falsePositives.size());
		return precision;
	}

	public static double getRecall(List<String> actualLines, List<String> retrievedLines) {
		List<String> truePositives = getTruePositives(actualLines, retrievedLines);
		double recall = (double) truePositives.size() / (double) actualLines.size();
		return recall;
	}

	public static double getF1(double precision, double recall) {
		double f1 = 2 * ((precision * recall) / (precision + recall));
		return f1;
	}

}
