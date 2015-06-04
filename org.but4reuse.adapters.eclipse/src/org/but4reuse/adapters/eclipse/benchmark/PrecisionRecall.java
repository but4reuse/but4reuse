package org.but4reuse.adapters.eclipse.benchmark;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PrecisionRecall {

	public static void createResultsFile(File actualFolder, File retrievedFolder) {
		StringBuilder sb = new StringBuilder();
		sb.append("Name;Precision;Recall;FScore\n");
		for (File f : actualFolder.listFiles()) {
			String name = f.getName().substring(0, f.getName().length() - ".txt".length());
			File f2 = new File(retrievedFolder, f.getName());
			List<String> actualLines = getLinesOfFile(f);
			if (!actualLines.isEmpty()) {
				List<String> retrievedLines = getLinesOfFile(f2);
				double precision = getPrecision(actualLines, retrievedLines);
				double recall = getRecall(actualLines, retrievedLines);
				double f1measure = getF1(precision, recall);
				sb.append(name + ";" + precision + ";" + recall + ";" + f1measure + "\n");
			}
		}
		try {
			writeFile(new File(actualFolder.getParentFile(), "resultPrecisionRecall_" + System.currentTimeMillis()
					+ ".csv"), sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		return (double) truePositives.size()
				/ (double) ((double) truePositives.size() + (double) falsePositives.size());
	}

	public static double getRecall(List<String> actualLines, List<String> retrievedLines) {
		List<String> truePositives = getTruePositives(actualLines, retrievedLines);
		return (double) truePositives.size() / (double) actualLines.size();
	}

	public static double getF1(double precision, double recall) {
		return 2 * ((precision * recall) / (precision + recall));
	}

	/**
	 * Get lines of a file
	 * 
	 * @param file
	 * @return list of strings
	 */
	public static List<String> getLinesOfFile(File file) {
		List<String> lines = new ArrayList<String>();
		try {
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				lines.add(strLine);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}

	public static void writeFile(File file, String text) throws Exception {
		BufferedWriter output;
		output = new BufferedWriter(new FileWriter(file, false));
		output.append(text);
		output.close();
	}

}
