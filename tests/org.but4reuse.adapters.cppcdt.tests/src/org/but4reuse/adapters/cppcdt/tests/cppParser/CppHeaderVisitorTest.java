package org.but4reuse.adapters.cppcdt.tests.cppParser;

import java.util.List;

import org.but4reuse.adapters.cppcdt.dependencies.DependencyManager;
import org.but4reuse.adapters.cppcdt.elements.CppElement;
import org.but4reuse.adapters.cppcdt.elements.CppElement.CppElementType;
import org.junit.Assert;
import org.junit.Test;

/**
 * This is a test suite for a project containing only header files. We are
 * testing the existence of the following elements : header file elements, class
 * definitions, function definitions, ifndef, includes and macros. We are
 * testing for the existence of the following dependencies : contained (class to
 * file, function to class, macro to file) inherits (class to class) reference
 * (include directive to file)
 * 
 * 
 * @author sandu.postaru
 *
 */

public class CppHeaderVisitorTest extends CppTestHelper {

	private final String fileName = "headerExample";
	
	public CppHeaderVisitorTest() {		
		initialise(fileName);	
	}
	

	// 24 existing elements
	// HEADER_F -> 3
	// CLASS_H -> 3
	// FUNCTION_H -> 9
	// IFNDEF -> 3
	// INCLUDE -> 3
	// MACRO -> 3
	@Test
	@Override
	public void numberOfElementsTest() {
		Assert.assertTrue(elements.size() == 24);
	}

	// Existence of all and only header elements
	@Test
	public void headerFileElementsTest() {

		testElementExistance(CppElementType.HEADER_FILE, new String[] { "animal.h", "cat.h", "dog.h" });
	}

	@Test
	// Existence of all and only class elements
	// Containment dependencies and inheritance dependencies
	public void classElementsTest() {

		// Test existence of class elements
		testElementExistance(CppElementType.CLASS_H, new String[] { "Animal", "Cat", "Dog" });

		CppElement animalClassElement = null;
		CppElement catClassElement = null;
		CppElement dogClassElement = null;

		List<CppElement> classElements = getElementsOfType(CppElementType.CLASS_H);

		// Find some elements to test dependencies for (here test for all of
		// them)
		for (CppElement element : classElements) {
			String name = element.getRawText();

			if (name.equals("Animal")) {
				animalClassElement = element;
			} else if (name.equals("Cat")) {
				catClassElement = element;
			} else if (name.equals("Dog")) {
				dogClassElement = element;
			} else {
				Assert.fail("Unexpected class element for " + name);
			}
		}

		// test containment dependencies (class in file)

		testDependencyExistance(animalClassElement, DependencyManager.CONTAINMENT_DEPENDENCY_ID,
				new String[] { "animal.h" });

		testDependencyExistance(dogClassElement, DependencyManager.CONTAINMENT_DEPENDENCY_ID, new String[] { "dog.h" });

		testDependencyExistance(catClassElement, DependencyManager.CONTAINMENT_DEPENDENCY_ID, new String[] { "cat.h" });

		// test inheritance dependencies (class to class)

		testDependencyExistance(animalClassElement, DependencyManager.INHERITANCE_DEPENDENCY_ID, new String[] {});

		testDependencyExistance(dogClassElement, DependencyManager.INHERITANCE_DEPENDENCY_ID,
				new String[] { "Animal" });

		testDependencyExistance(catClassElement, DependencyManager.INHERITANCE_DEPENDENCY_ID,
				new String[] { "Animal" });

		// test the NON existence of different type of dependencies
		testDependencyExistance(animalClassElement, DependencyManager.FUNCTION_CALL_DEPENDENCY_ID, new String[] {});
		testDependencyExistance(dogClassElement, DependencyManager.REFERENCE_DEPENDENCY_ID, new String[] {});
		testDependencyExistance(catClassElement, DependencyManager.STATEMENT_DEPENDENCY_ID, new String[] {});

	}

	@Test
	// Existence of function elements (all 9)
	// Containment dependencies for 3 of the 9 functions

	public void functionElementsTest() {

		// test existence of function elements
		testElementExistance(CppElementType.FUNCTION_H,
				new String[] { "Animal::Animal~char const", "Animal::sleep", "Animal::sound", "Cat::Cat~char const",
						"Cat::sleep", "Cat::sound", "Dog::Dog~char const", "Dog::sleep", "Dog::sound" });

		List<CppElement> functionElements = getElementsOfType(CppElementType.FUNCTION_H);

		// Find some elements to test dependencies for (here 3 of them)"

		CppElement animalConstructorFunction = null;
		CppElement catSleepFunction = null;
		CppElement dogSoundFunction = null;

		for (CppElement function : functionElements) {
			String name = function.getRawText();

			if (name.equals("Animal::Animal~char const")) {
				animalConstructorFunction = function;
			} else if (name.equals("Cat::sleep")) {
				catSleepFunction = function;
			} else if (name.equals("Dog::sound")) {
				dogSoundFunction = function;
			}
		}

		// test existence of containment dependencies (function to class)

		testDependencyExistance(animalConstructorFunction, DependencyManager.CONTAINMENT_DEPENDENCY_ID,
				new String[] { "Animal" });

		testDependencyExistance(catSleepFunction, DependencyManager.CONTAINMENT_DEPENDENCY_ID, new String[] { "Cat" });

		testDependencyExistance(dogSoundFunction, DependencyManager.CONTAINMENT_DEPENDENCY_ID, new String[] { "Dog" });

		// test the NON existence for some dependencies
		testDependencyExistance(animalConstructorFunction, DependencyManager.INHERITANCE_DEPENDENCY_ID,
				new String[] {});
		testDependencyExistance(catSleepFunction, DependencyManager.FUNCTION_CALL_DEPENDENCY_ID, new String[] {});
		testDependencyExistance(dogSoundFunction, DependencyManager.STATEMENT_DEPENDENCY_ID, new String[] {});
	}

	// Existence of infndef elements and containment dependencies
	@Test
	public void infndefElementsTest() {

		// test existence of ifndef elements
		testElementExistance(CppElementType.IFNDEF_DIR,
				new String[] { "#ifndef ANIMAL_H_", "#ifndef CAT_H_", "#ifndef DOG_H_" });

		// Find some elements to test dependencies for (here all of them)"

		CppElement animalH = null;
		CppElement catH = null;
		CppElement dogH = null;

		List<CppElement> ifndefs = getElementsOfType(CppElementType.IFNDEF_DIR);

		for (CppElement ifndef : ifndefs) {
			String name = ifndef.getRawText();

			if (name.equals("#ifndef ANIMAL_H_")) {
				animalH = ifndef;
			} else if (name.equals("#ifndef CAT_H_")) {
				catH = ifndef;
			} else if (name.equals("#ifndef DOG_H_")) {
				dogH = ifndef;
			}
		}

		// test the existence containment dependencies

		testDependencyExistance(animalH, DependencyManager.CONTAINMENT_DEPENDENCY_ID, new String[] { "animal.h" });

		testDependencyExistance(dogH, DependencyManager.CONTAINMENT_DEPENDENCY_ID, new String[] { "dog.h" });

		testDependencyExistance(catH, DependencyManager.CONTAINMENT_DEPENDENCY_ID, new String[] { "cat.h" });

		// test the Non existence for some dependencies
		testDependencyExistance(animalH, DependencyManager.FUNCTION_CALL_DEPENDENCY_ID, new String[] {});
		testDependencyExistance(animalH, DependencyManager.INHERITANCE_DEPENDENCY_ID, new String[] {});
		testDependencyExistance(dogH, DependencyManager.REFERENCE_DEPENDENCY_ID, new String[] {});
	}

	// Existence of include elements and containment dependencies
	@Test
	public void includeElementsTest() {

		// element existence
		testElementExistance(CppElementType.INCLUDE_DIR,
				new String[] { "#include \"animal.h\"", "#include <iostream>", "#include \"animal.h\"" });

		List<CppElement> includes = getElementsOfType(CppElementType.INCLUDE_DIR);
		CppElement iostream = null;

		for (CppElement include : includes) {
			String name = include.getRawText();

			if (name.equals("#include <iostream>")) {
				iostream = include;
				break;
			}
		}

		// dependency existence
		testDependencyExistance(iostream, DependencyManager.CONTAINMENT_DEPENDENCY_ID, new String[] { "animal.h" });

		// dependency non existence
		testDependencyExistance(iostream, DependencyManager.REFERENCE_DEPENDENCY_ID, new String[] {});
		testDependencyExistance(iostream, DependencyManager.INHERITANCE_DEPENDENCY_ID, new String[] {});
	}

	@Test
	// Existence of macro elements and containmenet dependencies
	public void macroElementsTest() {

		// test existence of ifndef elements
		testElementExistance(CppElementType.MACRO_DIR,
				new String[] { "#define ANIMAL_H_", "#define CAT_H_", "#define DOG_H_" });

		// Find some elements to test dependencies for (here all of them)"

		CppElement animalM = null;
		CppElement catM = null;
		CppElement dogM = null;

		List<CppElement> macros = getElementsOfType(CppElementType.MACRO_DIR);

		for (CppElement macro : macros) {
			String name = macro.getRawText();

			if (name.equals("#define ANIMAL_H_")) {
				animalM = macro;
			} else if (name.equals("#define CAT_H_")) {
				catM = macro;
			} else if (name.equals("#define DOG_H_")) {
				dogM = macro;
			}
		}

		// test the existence containment dependencies

		testDependencyExistance(animalM, DependencyManager.CONTAINMENT_DEPENDENCY_ID, new String[] { "animal.h" });

		testDependencyExistance(dogM, DependencyManager.CONTAINMENT_DEPENDENCY_ID, new String[] { "dog.h" });

		testDependencyExistance(catM, DependencyManager.CONTAINMENT_DEPENDENCY_ID, new String[] { "cat.h" });

		// test the Non existence for some dependencies
		testDependencyExistance(animalM, DependencyManager.FUNCTION_CALL_DEPENDENCY_ID, new String[] {});
		testDependencyExistance(catM, DependencyManager.INHERITANCE_DEPENDENCY_ID, new String[] {});
		testDependencyExistance(dogM, DependencyManager.REFERENCE_DEPENDENCY_ID, new String[] {});
	}
}
