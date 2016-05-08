package org.but4reuse.adapters.eclipse.generator.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class VariantsUtils {

	public static final String VARIANT = "Variant";
	public static final String FEATURES = "features";
	public static final String PLUGINS = "plugins";
	public static final String META_INF = "META-INF";
	public static final String PROPERTIES = ".properties";
	public static final String JAR = ".jar";

	public static final String PARAMETERS_TITLE = "Generate Eclipse variants";
	public static final String ADAPTER_NAME = "eclipse4generator";

	public static final String INPUT_TEXT = "Input path of eclipse package";
	public static final String OUTPUT_TEXT = "Output path for variants";
	public static final String VARIANTS_NUMBER_TEXT = "Variants number (1 min.)";
	public static final String TIME_NUMBER_TEXT = "Time in secods allowed for Pledge generations";
	public static final String RANDOM_NUMBER_TEXT = "Value of random selector (between 0 and 100%)";
	public static final String VALID = "Validate input";
	
	public static boolean isEclipseDir(File eclipse){
		if(eclipse.list()!=null && Arrays.asList(eclipse.list()).containsAll(
				Arrays.asList(new String[]{"plugins", "features", "eclipse.exe"}))) {
			return true;
		}
		return false;
	}
	
	public static void copy(final Object from, final Object to) {
        Map<String, Field> fromFields = analyze(from);
        Map<String, Field> toFields = analyze(to);
        fromFields.keySet().retainAll(toFields.keySet());
        for (Entry<String, Field> fromFieldEntry : fromFields.entrySet()) {
            final String name = fromFieldEntry.getKey();
            final Field sourceField = fromFieldEntry.getValue();
            final Field targetField = toFields.get(name);
            if (targetField.getType().isAssignableFrom(sourceField.getType())) {
                sourceField.setAccessible(true);
                if (Modifier.isFinal(targetField.getModifiers())) continue;
                targetField.setAccessible(true);
                try {
                    targetField.set(to, sourceField.get(from));
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Can't access field!");
                }
            }
        }
    }

    private static Map<String, Field> analyze(Object object) {
        if (object == null) throw new NullPointerException();

        Map<String, Field> map = new TreeMap<String, Field>();

        Class<?> current = object.getClass();
        while (current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    if (!map.containsKey(field.getName())) {
                        map.put(field.getName(), field);
                    }
                }
            }
            current = current.getSuperclass();
        }
        return map;
    }

}
