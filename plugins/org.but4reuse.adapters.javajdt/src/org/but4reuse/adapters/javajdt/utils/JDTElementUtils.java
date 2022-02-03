package org.but4reuse.adapters.javajdt.utils;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.javajdt.JDTParser;
import org.but4reuse.adapters.javajdt.elements.CompilationUnitElement;
import org.but4reuse.adapters.javajdt.elements.MethodElement;
import org.but4reuse.adapters.javajdt.elements.TypeElement;

public class JDTElementUtils {

	public static List<TypeElement> getTypes(CompilationUnitElement compilationUnitElement) {
		List<TypeElement> types = new ArrayList<TypeElement>();
		List<IDependencyObject> dependants = compilationUnitElement.getDependants()
				.get(JDTParser.DEPENDENCY_COMPILATION_UNIT);
		for (IDependencyObject dependant : dependants) {
			if (dependant instanceof TypeElement) {
				types.add((TypeElement) dependant);
			}
		}
		return types;
	}

	public static List<MethodElement> getMethods(TypeElement typeElement) {
		List<MethodElement> methods = new ArrayList<MethodElement>();
		List<IDependencyObject> dependants = typeElement.getDependants().get(JDTParser.DEPENDENCY_TYPE);
		for (IDependencyObject dependant : dependants) {
			if (dependant instanceof MethodElement) {
				methods.add((MethodElement) dependant);
			}
		}
		return methods;
	}

}
