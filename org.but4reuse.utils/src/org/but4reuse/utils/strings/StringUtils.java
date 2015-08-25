package org.but4reuse.utils.strings;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class StringUtils {

	public static ArrayList<String> splitString(String s) {
		ArrayList<String> list = new ArrayList<String>();

		if (s == null)
			return list;

		StringTokenizer tk = new StringTokenizer(s, " :!?*+²&~\"#'{}()[]|`_\\^°,.;/§");
		while (tk.hasMoreTokens())
			list.add(tk.nextToken());

		return list;
	}

	public static ArrayList<String> splitWords(String w) {
		ArrayList<String> list = new ArrayList<String>();

		if (w == null)
			return list;

		for (String s : w.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"))
			list.add(s);

		return list;
	}
}
