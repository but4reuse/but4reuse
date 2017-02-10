package org.but4reuse.utils.nlp;

import java.net.URL;

import org.but4reuse.utils.files.FileUtils;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;

/**
 * WordNet utils
 * 
 * @author jabier.martinez
 *
 */
public class WordNetUtils {

	public static IDictionary dict = null;

	public static IDictionary getDictionary() {
		if (dict != null) {
			return dict;
		}
		try {
			URL url = FileUtils.getFileURLFromPluginFileProtocol("org.but4reuse.utils.nlp", "lib/dict");

			// construct the dictionary object and open it
			dict = new Dictionary(url);
			dict.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dict;
	}
}
