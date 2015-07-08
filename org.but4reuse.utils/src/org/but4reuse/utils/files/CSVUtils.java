package org.but4reuse.utils.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * CSV Utils
 * 
 * @author jabier.martinez
 */
public class CSVUtils {

	/**
	 * export a matrix to a comma separated value (csv) file
	 * 
	 * @param uri
	 * @param a
	 *            matrix (line, columns) of objects
	 */
	public static void exportCSV(URI uri, List<List<Object>> texts) {
		// Create csv file
		StringBuilder csvText = new StringBuilder();
		for (List<?> list : texts) {
			for (Object text : list) {
				String a = "";
				if (text != null) {
					a = text.toString();
				}
				csvText.append(a + ";");
			}
			csvText.setLength(csvText.length() - 1);
			csvText.append("\n");
		}
		// Save file
		File f = FileUtils.getFile(uri);
		try {
			FileUtils.createFile(f);
			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.append(csvText.toString());
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * export a matrix to a comma separated value (csv) file
	 * 
	 * @param uri
	 * @param a
	 *            matrix (line, columns) of objects
	 */
	public static void exportCSV(URI uri, Object[][] texts) {
		// Create csv file
		StringBuilder csvText = new StringBuilder();
		for (Object[] list : texts) {
			for (Object text : list) {
				String a = "";
				if (text != null) {
					a = text.toString();
				}
				csvText.append(a + ";");
			}
			csvText.setLength(csvText.length() - 1);
			csvText.append("\n");
		}
		// Save file
		File f = FileUtils.getFile(uri);
		try {
			FileUtils.createFile(f);
			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.append(csvText.toString());
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
