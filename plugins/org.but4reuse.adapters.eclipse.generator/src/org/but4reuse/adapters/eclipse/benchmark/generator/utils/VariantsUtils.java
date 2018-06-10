package org.but4reuse.adapters.eclipse.benchmark.generator.utils;

import java.io.File;
import java.util.Arrays;

public class VariantsUtils {

	public static final String VARIANT = "Variant";
	public static final String FEATURES = "features";
	public static final String PLUGINS = "plugins";
	public static final String META_INF = "META-INF";
	public static final String PROPERTIES = ".properties";

	public static final String JAR = ".jar"; // $NON-NLS-N$

	public static final String PARAMETERS_TITLE = "Generate Eclipse variants";
	public static final String ADAPTER_NAME = "eclipse4generator";

	public static final String INPUT_TEXT = "Input path of eclipse package";
	public static final String OUTPUT_TEXT = "Output path for variants";
	public static final String GENERATOR_TEXT = "Generator";
	public static final String VARIANTS_NUMBER_TEXT = "Number of variants";
	public static final String TIME_NUMBER_TEXT = "Time (seconds) for calculation (0 for Random)";
	public static final String RANDOM_NUMBER_TEXT = "Value of random selector (between 0 and 100%)";
	public static final String RANDOM_SEED_TEXT = "Random seed (Empty or enter a number)";
	public static final String VALID = "Validate input";

	public static boolean isEclipseDir(File eclipse) {
		if (eclipse.list() != null
				&& Arrays.asList(eclipse.list()).containsAll(Arrays.asList(new String[] { "plugins", "features" }))) {
			return true;
		}
		return false;
	}

}
