package org.but4reuse.adapters.eclipse.generator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PreferenceUtils {
	
	public static final String PREF_FILE = "prefMap.ser"; 

	public static File getPreferencesFile(Object context) throws IOException{
		ClassLoader classLoader = context.getClass().getClassLoader();
		URL res = classLoader.getResource(PREF_FILE);
		return new File(res.getFile());
	}
	
	public static void savePreferencesMap(Map<String,Object> mapToSave, Object context) throws IOException {
		if(mapToSave!=null && !mapToSave.isEmpty()){
			File pref = getPreferencesFile(context);
			FileOutputStream fos = new FileOutputStream(pref);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(mapToSave);
			oos.close();
			fos.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getPreferencesMap(Object context) throws IOException, ClassNotFoundException {
		HashMap<String, Object> map = null;
		FileInputStream fis = new FileInputStream(getPreferencesFile(context));
		ObjectInputStream ois = new ObjectInputStream(fis);
		map = (HashMap<String, Object>) ois.readObject();
		ois.close();
		fis.close();
		return map;
	}
	
}
