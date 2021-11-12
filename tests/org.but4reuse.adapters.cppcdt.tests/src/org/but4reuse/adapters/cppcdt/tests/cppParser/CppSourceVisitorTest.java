package org.but4reuse.adapters.cppcdt.tests.cppParser;

import java.util.List;

import org.but4reuse.adapters.cppcdt.dependencies.DependencyManager;
import org.but4reuse.adapters.cppcdt.elements.CppElement;
import org.but4reuse.adapters.cppcdt.elements.CppElement.CppElementType;
import org.junit.Assert;
import org.junit.Test;

/**
 * This is a test suite for a project containing only source files. We are
 * testing the existence of the following elements : source file elements,
 * function implementations, statements, includes. We are testing for the
 * existence of the following dependencies : contained (function to file,
 * include to file), statement (statement to previous statement)
 * 
 * 
 * @author sandu.postaru
 *
 */

public class CppSourceVisitorTest extends CppTestHelper {	
	
	private final String fileName = "sourceExample";
	
	public CppSourceVisitorTest() {		
		initialise(fileName);	
	}

	// 41 existing elements
	// SOURCE_F -> 4
	// INCLUDES -> 9
	// FUNCTIONS -> 8
	// STATEMENTS -> 20
	@Test
	@Override
	public void numberOfElementsTest() {
		Assert.assertTrue(elements.size() == 41);
	}

	// Test the existence of source file elements
	@Test
	public void sourceFileElementsTest() {
		testElementExistance(CppElementType.SOURCE_FILE,
				new String[] { "animal.cpp", "cat.cpp", "dog.cpp", "main.cpp" });
	}

	@Test
	// Test the existence of function elements and containment dependencies
	public void functionImplElementsTest() {

		testElementExistance(CppElementType.FUNCTION_IMPL,
				new String[] { "main", "Dog::sound", "Dog::sleep", "Dog::Dog~char const", "Cat::Cat~char const",
						"Cat::sleep", "Cat::sound", "Animal::Animal~char const" });

		List<CppElement> functionElements = getElementsOfType(CppElementType.FUNCTION_IMPL);

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

		// test existence of containment dependencies (function to source file)

		testDependencyExistance(animalConstructorFunction, DependencyManager.CONTAINMENT_DEPENDENCY_ID,
				new String[] { "animal.cpp" });

		testDependencyExistance(catSleepFunction, DependencyManager.CONTAINMENT_DEPENDENCY_ID,
				new String[] { "cat.cpp" });

		testDependencyExistance(dogSoundFunction, DependencyManager.CONTAINMENT_DEPENDENCY_ID,
				new String[] { "dog.cpp" });

		// test the NON existence for some dependencies
		testDependencyExistance(animalConstructorFunction, DependencyManager.INHERITANCE_DEPENDENCY_ID,
				new String[] {});
		testDependencyExistance(catSleepFunction, DependencyManager.FUNCTION_CALL_DEPENDENCY_ID, new String[] {});
		testDependencyExistance(dogSoundFunction, DependencyManager.STATEMENT_DEPENDENCY_ID, new String[] {});
	}

	@Test
	// Test the existence of include elements
	// Containment dependencies have been tested in the CppHeaderVisitor
	public void includeElements() {

		// element existence
		testElementExistance(CppElementType.INCLUDE_DIR,
				new String[] { "#include \"animal.h\"", "#include \"animal.h\"", "#include <iostream>",
						"#include <iostream>", "#include <iostream>", "#include \"dog.h\"", "#include \"dog.h\"",
						"#include \"cat.h\"", "#include \"cat.h\"" });
	}

	@Test
	// Test the existence of statement elements and containment dependencies
	public void statementElements() {

		List<CppElement> statements = getElementsOfType(CppElementType.STATEMENT_IMPL);
		Assert.assertEquals(20, statements.size());

		for (CppElement s : statements) {
			System.out.println(s.getRawText());
		}

		// we don't test the whole 20 statements, we chose 2 statements that
		// depend one on another (statement contained in a body statement)
		// and on other elements (body statement contained in function element)

		CppElement animalConstructorBody = null;
		CppElement animalConstructorStatement = null;

		for (CppElement statement : statements) {
			String name = statement.getRawText();

			if (name.equals("{\n" + "	m_name = name;\n" + "}")) {
				animalConstructorBody = statement;
			} else if (name.equals("m_name = name;")) {
				animalConstructorStatement = statement;
			}
		}

		// element existence
		Assert.assertNotEquals(null, animalConstructorBody);
		Assert.assertNotEquals(null, animalConstructorStatement);

		// statement dependencies
		testDependencyExistance(animalConstructorStatement, DependencyManager.STATEMENT_DEPENDENCY_ID,
				new String[] { "{\n" + "	m_name = name;\n" + "}" });

		testDependencyExistance(animalConstructorBody, DependencyManager.STATEMENT_DEPENDENCY_ID,
				new String[] { "Animal::Animal~char const" });
		
		// non existence of other dependencies
		
		testDependencyExistance(animalConstructorBody, DependencyManager.INHERITANCE_DEPENDENCY_ID, new String[] {});
		testDependencyExistance(animalConstructorStatement, DependencyManager.REFERENCE_DEPENDENCY_ID, new String[] {});
		
	}

}
