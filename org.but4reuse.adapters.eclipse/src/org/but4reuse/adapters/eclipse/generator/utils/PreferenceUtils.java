package org.but4reuse.adapters.eclipse.generator.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PreferenceUtils {
	
	public static final String PREF_FILE = "preferences.properties";

	
	public static void savePreferencesMap(Map<String,String> mapToSave, Object context) throws IOException {

		Properties prop = new Properties();
		OutputStream output = null;

		try {
			output = new FileOutputStream(PREF_FILE);
			
			for(String key : mapToSave.keySet()){
				// set the properties value
				prop.setProperty(key, mapToSave.get(key));
			}
			// save properties to project root folder
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		
	}
	
	public static Map<String, String> getPreferencesMap(Object context) throws IOException{

		Properties prop = new Properties();
		InputStream input = null;

		Map<String, String> map = new HashMap<>();
		try {

			input = new FileInputStream(PREF_FILE);

			// load a properties file
			prop.load(input);

			map.put("input", prop.getProperty("input"));
			map.put("output", prop.getProperty("output"));
			map.put("numberVariant", prop.getProperty("numberVariant"));
			map.put("randomSelector", prop.getProperty("randomSelector"));
			map.put("onlyMetaData", prop.getProperty("onlyMetaData"));
			map.put("user.name", prop.getProperty("user.name"));

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}
	
}
