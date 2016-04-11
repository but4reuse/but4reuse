package org.but4reuse.adapters.eclipse.generator;

import java.io.File;
import java.io.IOException;

import org.but4reuse.adapters.eclipse.generator.utils.PreferenceUtils;

public class TestExeVariants {

	public static void main(String[] args) throws IOException {
		String output =  PreferenceUtils.getPreferencesMap().get(PreferenceUtils.PREF_OUTPUT);
		File outputDir = new File(output);
		
		String[] allVars = outputDir.list();
		Process process = new ProcessBuilder(allVars[0]+"eclipse.exe").start();
	}
	
}
