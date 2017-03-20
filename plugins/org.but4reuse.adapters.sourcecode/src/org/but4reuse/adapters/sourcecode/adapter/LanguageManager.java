package org.but4reuse.adapters.sourcecode.adapter;

import java.util.HashMap;

import de.ovgu.cide.fstgen.ast.FSTNode;

/**
 * The current language
 */
public class LanguageManager {

	private static ILanguage LANGUAGE;

	// TODO seems unused
	public static HashMap<FSTNode, String> filesNames = new HashMap<FSTNode, String>();

	public static ILanguage getLanguage() {
		return LANGUAGE;
	}

	public static void setLanguage(ILanguage iLanguage) {
		LANGUAGE = iLanguage;
	}

}
