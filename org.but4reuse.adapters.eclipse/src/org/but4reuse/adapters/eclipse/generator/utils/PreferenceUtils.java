package org.but4reuse.adapters.eclipse.generator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PreferenceUtils {
	
	private static final String PREF_FILE = "preferences.properties";

	public static String getPrefFilePath(){
		String path = PreferenceUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath(); //Give org.but4reuse.adapters.eclipse
		path += "src"+File.separator+"resources"+File.separator+PREF_FILE;
		return path;
	}
	
	public static void savePreferencesMap(Map<String,String> mapToSave, Object context) throws IOException {

		Properties prop = new Properties();
		OutputStream output = null;

		output = new FileOutputStream(getPrefFilePath());
		for(String key : mapToSave.keySet()){
			prop.setProperty(key, mapToSave.get(key));
		}
		prop.store(output, null);

		if (output != null) {
			output.close();
		}

	}
	
	public static Map<String, String> getPreferencesMap(Object context) throws IOException{

		Properties prop = new Properties();
		InputStream input = null;

		Map<String, String> map = new HashMap<>();
		try {

			input = new FileInputStream(getPrefFilePath());
			prop.load(input);

			map.put("input", prop.getProperty("input"));
			map.put("output", prop.getProperty("output"));
			map.put("numberVariant", prop.getProperty("numberVariant"));
			map.put("randomSelector", prop.getProperty("randomSelector"));
			map.put("onlyMetaData", prop.getProperty("onlyMetaData"));
			map.put("user.name", prop.getProperty("user.name"));

		} catch (FileNotFoundException ex) {
			throw new FileNotFoundException("Does the \"preferences.properties\" file exist in adapters.eclipse/src/resources ?");
		} finally {
			if (input != null) {
				input.close();
			}
		}
		return map;
	}
	
}
