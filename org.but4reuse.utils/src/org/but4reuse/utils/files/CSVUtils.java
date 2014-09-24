package org.but4reuse.utils.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * CSV Utils
 * @author jabier.martinez
 */
public class CSVUtils {
	
	/**
	 * export a matrix to a comma separated value (csv) file
	 * @param uri
	 * @param a matrix (line, columns) of objects
	 */
	public static void exportCSV(URI uri, List<List<?>> texts){
		// Create csv file
		String csvText = "";
		for (List<?> list : texts) {
			for(Object text : list){
				String a = "";
				if(text !=null){
					a = text.toString();
				}
				csvText = csvText + a + ";";
			}
			csvText = csvText.substring(0, csvText.length()-1);
			csvText = csvText + "\n";
		}
		// Save file
		File f = FileUtils.getFile(uri);
		try {
			FileUtils.createFile(f);
			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.append(csvText);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
