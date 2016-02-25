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
	public static final String PREF_INPUT = "input";
	public static final String PREF_OUTPUT = "output";
	public static final String PREF_RANDOM = "randomSelector";
	public static final String PREF_VARIANTS = "numberVariant";
	public static final String PREF_METADATA = "onlyMetaData";
	public static final String PREF_USERNAME = "user.name";

	public static String getPrefFilePath(){
		String path = PreferenceUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath(); //Give org.but4reuse.adapters.eclipse
		if(path.endsWith("bin/")) path = path.substring(0, path.length()-4); // On some OS, it gives "bin/" in more, but we don't want
		path += "src"+File.separator+"resources"+File.separator+PREF_FILE;
		return path;
	}
	
	public static void savePreferencesMap(String input, String output, String random, 
			String numberVar, String metadata) throws IOException {

		Map<String, String> mapToSave = new HashMap<>();
		mapToSave.put(PREF_INPUT, input);
		mapToSave.put(PREF_OUTPUT, output);
		mapToSave.put(PREF_RANDOM, random);
		mapToSave.put(PREF_VARIANTS, numberVar);
		mapToSave.put(PREF_METADATA, metadata );
		mapToSave.put(PREF_USERNAME, System.getProperty("user.name")); // Display OUR preferences (maybe an other prefMap.ser was committed)
		
		Properties prop = new Properties();
		OutputStream outputFOS = new FileOutputStream(getPrefFilePath());
		for(String key : mapToSave.keySet()){
			prop.setProperty(key, mapToSave.get(key));
		}
		prop.store(outputFOS, null);

		if (outputFOS != null) {
			outputFOS.close();
		}

	}
	
	public static Map<String, String> getPreferencesMap() throws IOException{

		Properties prop = new Properties();
		InputStream input = null;

		Map<String, String> map = new HashMap<>();
		try {

			input = new FileInputStream(getPrefFilePath());
			prop.load(input);

			map.put(PREF_INPUT, prop.getProperty(PREF_INPUT));
			map.put(PREF_OUTPUT, prop.getProperty(PREF_OUTPUT));
			map.put(PREF_VARIANTS, prop.getProperty(PREF_VARIANTS));
			map.put(PREF_RANDOM, prop.getProperty(PREF_RANDOM));
			map.put(PREF_METADATA, prop.getProperty(PREF_METADATA));
			map.put(PREF_USERNAME, prop.getProperty(PREF_USERNAME));

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
