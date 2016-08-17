package org.but4reuse.utils.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;

/**
 * Properties file utils
 * 
 * @author jabier.martinez
 * 
 */
public class PropertiesFileUtils {
	public final static int PUCK_PROPERTIES_SIZE = 4;
	public final static String PUCK_PROPERTIES_FILE_NAME = "puck.properties";


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

	public static Map<String,String> getPuckProperties(URI uriRep){
		File f = new File(FileUtils.getFile(uriRep), PUCK_PROPERTIES_FILE_NAME);
		Map<String,String> properties = new HashMap<String,String>();
		if (f.exists()) {
			String[] propertieString = (FileUtils.getStringOfFile(f)).split("[=;]");
			if (propertieString.length != PUCK_PROPERTIES_SIZE) {
				System.out.println("The puck properties file doesn't have the right format");
				properties = getPuckBasicProperties(false,false);
			} else {
				if(propertieString[0].contains("classpath") && propertieString[2].contains("bootclasspath")){
					properties.put("classpath", propertieString[1]);
					properties.put("bootclasspath", propertieString[3]);
				}
				else if (propertieString[0].contains("classpath") && !propertieString[2].contains("bootclasspath")) {
					properties = getPuckBasicProperties(true,false);
					properties.put("classpath", propertieString[1]);
				}
				else if (!propertieString[0].contains("classpath") && propertieString[2].contains("bootclasspath")) {
					properties = getPuckBasicProperties(false,true);
					properties.put("bootclasspath", propertieString[3]);
				}
				else{
					System.out.println("Error in the puck properties file");
					properties = getPuckBasicProperties(false,false);
				}
			}
		}
		else {
				System.out.println("No puck properties file found : puck.properties not found");
				properties = getPuckBasicProperties(false,false);
		}
		return properties;
	}
	
	public static Map<String,String> getPuckBasicProperties(boolean containClasspath,boolean containsBootclasspath){
		Map<String,String> properties = new HashMap<String,String>();
		if (!containClasspath) 
			properties.put("classpath", "none");
		if (!containsBootclasspath)
			properties.put("bootclasspath", FilenameUtils.separatorsToSystem((System.getProperty("java.home")+"\\lib\\rt.jar")));
		return properties;
	}
}
