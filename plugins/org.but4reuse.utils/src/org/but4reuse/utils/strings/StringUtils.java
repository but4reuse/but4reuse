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

		string = string.replaceAll("\\s", " ");
		StringTokenizer tk = new StringTokenizer(string, " :!?*+²&~\"#'{}()[]|`_\\^°,.;/§");
		while (tk.hasMoreTokens()) {
			String token = tk.nextToken();

			if (!token.equals("-")) {
				list.add(token);
			}
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
		String[] split = word.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");

		for (int i = 0; i < split.length; i++) {
			// special case e.g. Non-Complete
			if (split[i].endsWith("-") && i + 1 < split.length) {
				list.add(split[i] + split[i + 1]);
			} else {
				list.add(split[i]);
			}
		}
		return list;
	}

	/**
	 * Tokenize and then split CamelCase
	 * 
	 * @param word
	 * @return a non null list of words
	 */
	public static List<String> tokenizeAndCamelCase(String word) {
		List<String> list = new ArrayList<String>();
		for (String token : tokenizeString(word)) {
			list.addAll(splitCamelCase(token));
		}
		return list;
	}
}
