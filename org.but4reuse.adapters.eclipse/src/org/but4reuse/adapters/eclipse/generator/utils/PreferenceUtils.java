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
	public static final String PREF_USERNAME = "user.name";

	public static String getPrefFilePath() {
		// Give org.but4reuse.adapters.eclipse
		String path = PreferenceUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		if (path.endsWith("bin/")) {
			// On some OS, it gives "bin/" in more, but we don't want
			path = path.substring(0, path.length() - 4);
		}
		path += "src" + File.separator + "resources" + File.separator + PREF_FILE;
		return path;
	}

	public static void savePreferences(String input, String output, String random, String numberVar)
			throws IOException {

		Map<String, String> mapToSave = getPreferences();
		
		if(input!=null && !input.isEmpty()) mapToSave.put(PREF_INPUT, input);
		if(output!=null && !output.isEmpty()) mapToSave.put(PREF_OUTPUT, output);
		if(random!=null && !random.isEmpty()) mapToSave.put(PREF_RANDOM, random);
		if(numberVar!=null && !numberVar.isEmpty()) mapToSave.put(PREF_VARIANTS, numberVar);
			
		// Display OUR preferences (maybe an other preferences.properties was committed)
		mapToSave.put(PREF_USERNAME, System.getProperty("user.name"));

		Properties prop = new Properties();
		OutputStream outputFOS = new FileOutputStream(getPrefFilePath());
		for (String key : mapToSave.keySet()) {
			prop.setProperty(key, mapToSave.get(key));
		}
		prop.store(outputFOS, null);

		if (outputFOS != null) {
			outputFOS.close();
		}

	}

	public static Map<String, String> getPreferences() throws IOException {

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
			map.put(PREF_USERNAME, prop.getProperty(PREF_USERNAME));

		} catch (FileNotFoundException ex) {
			throw new FileNotFoundException(
					"Does the \"preferences.properties\" file exist in adapters.eclipse/src/resources ?");
		} finally {
			if (input != null) {
				input.close();
			}
		}
		return map;
	}

	public static void savePreferences(String input, String output, String numberVar)
			throws IOException {

		Map<String, String> mapToSave = getPreferences();
		
		if(input!=null && !input.isEmpty()) mapToSave.put(PREF_INPUT, input);
		if(output!=null && !output.isEmpty()) mapToSave.put(PREF_OUTPUT, output);
		if(numberVar!=null && !numberVar.isEmpty()) mapToSave.put(PREF_VARIANTS, numberVar);
			
		// Display OUR preferences (maybe an other preferences.properties was committed)
		mapToSave.put(PREF_USERNAME, System.getProperty("user.name"));

		Properties prop = new Properties();
		OutputStream outputFOS = new FileOutputStream(getPrefFilePath());
		for (String key : mapToSave.keySet()) {
			prop.setProperty(key, mapToSave.get(key));
		}
		prop.store(outputFOS, null);

		if (outputFOS != null) {
			outputFOS.close();
		}

	}

}
