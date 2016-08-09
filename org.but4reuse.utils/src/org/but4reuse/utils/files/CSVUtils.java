package org.but4reuse.utils.files;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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

	/**
	 * Import a CSV comma separated to a matrix of String
	 * 
	 * @param uri
	 * @return
	 */
	public static String[][] importCSV(URI uri) {
		try {
			File f = new File(uri);
			FileReader input;
			input = new FileReader(f);
			BufferedReader bufRead = new BufferedReader(input);
			String myLine = null;
			int lines = countLines(f);
			int tabs = countTabs(f);
			String[][] stringMatrix = new String[lines][tabs];
			int i = 0;
			while ((myLine = bufRead.readLine()) != null) {
				String[] array1 = myLine.split(";");
				if (array1.length == tabs) {
					for (int j = 0; j < tabs; j++) {
						stringMatrix[i][j] = array1[j];
					}
				}
				if (array1.length < tabs) {
					int j;
					for (j = 0; j < tabs; j++) {
						if(j<array1.length)
							stringMatrix[i][j] = array1[j];
						else
							stringMatrix[i][j] = "";
					}
					
				}
				i++;
			}
			bufRead.close();
			return stringMatrix;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Count the number of line in a csv file
	 * */
	private static int countLines(File file) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

	/**
	 * Count the umber of tabs in a csv file
	 */
	private static int countTabs(File file) throws IOException {

		int count = 0;
		FileReader input = new FileReader(file);
		BufferedReader bufRead = new BufferedReader(input);
		String myLine = bufRead.readLine();

		count = myLine.split(";").length;
		bufRead.close();
		return count;

	}

}
