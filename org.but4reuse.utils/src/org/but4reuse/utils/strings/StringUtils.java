package org.but4reuse.utils.strings;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtils {

	/**
	 * Tokenize string by separating space and special characters like : etc.
	 * 
	 * @param string
	 * @return a non null list of tokens
	 */
	public static List<String> tokenizeString(String string) {
		List<String> list = new ArrayList<String>();
		if (string == null) {
			return list;
		}
		StringTokenizer tk = new StringTokenizer(string, " :!?*+²&~\"#'{}()[]|`_\\^°,.;/§");
		while (tk.hasMoreTokens()) {
			list.add(tk.nextToken());
		}
		return list;
	}

	/**
	 * Split camel case word
	 * 
	 * @param word
	 * @return a non null list of words
	 */
	public static List<String> splitCamelCase(String word) {
		List<String> list = new ArrayList<String>();
		if (word == null) {
			return list;
		}
		for (String s : word.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
			list.add(s);
		}
		return list;
	}
	
	/**
	 * Tokenize and then split CamelCase
	 * @param word
	 * @return a non null list of words
	 */
	public static List<String> tokenizeAndCamelCase(String word){
		List<String> list = new ArrayList<String>();
		for(String token : tokenizeString(word)){
			list.addAll(splitCamelCase(token));
		}
		return list;
	}
}
