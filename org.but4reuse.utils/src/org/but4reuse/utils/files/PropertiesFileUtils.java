package org.but4reuse.utils.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Properties file utils
 * 
 * @author jabier.martinez
 * 
 */
public class PropertiesFileUtils {
	


	public static String getValue(File propertiesFile, String key) {
		if (propertiesFile.exists()) {
			try {
				Properties prop = new Properties();
				InputStream input = new FileInputStream(propertiesFile);
				prop.load(input);
				String value = prop.getProperty(key);
				input.close();
				return value;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// For example when %name
	public static String getKey(String keyWithPercentage) {
		return keyWithPercentage.substring(1).replaceAll("\\s", "");
	}

	
}
