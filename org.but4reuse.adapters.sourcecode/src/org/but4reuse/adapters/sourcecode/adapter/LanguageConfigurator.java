package org.but4reuse.adapters.sourcecode.adapter;

import java.util.HashMap;

import de.ovgu.cide.fstgen.ast.FSTNode;

public class LanguageConfigurator {
	
	public static InterfaceLanguage LANGUAGE;
	
	public static HashMap<FSTNode, String> filesNames=
				new HashMap<FSTNode, String>();

	public static InterfaceLanguage getLanguage() {
		return LANGUAGE;
	}

	
	
	
}
