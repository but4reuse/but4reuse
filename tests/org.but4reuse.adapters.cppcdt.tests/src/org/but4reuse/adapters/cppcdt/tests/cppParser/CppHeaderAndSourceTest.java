package org.but4reuse.adapters.cppcdt.tests.cppParser;

import java.util.List;

import org.but4reuse.adapters.cppcdt.activator.Activator;
import org.but4reuse.adapters.cppcdt.dependencies.DependencyManager;
import org.but4reuse.adapters.cppcdt.elements.CppElement;
import org.but4reuse.adapters.cppcdt.elements.CppElement.CppElementType;
import org.but4reuse.adapters.cppcdt.preferences.CppAdapterPreferencePage;
import org.junit.Assert;
import org.junit.Test;

/**
 * This is a test suite for a complete project containing both header and source
 * files. The existence of all the elements and the inheritance and containment
 * dependencies for these elements are tested in CppHeaderVisitor and
 * CppSourceVisitor.
 * 
 * So here we will test the dependencies between header and source elements that
 * could not be tested before such as : function call dependencies, header
 * dependencies (function implementation source element to function definition
 * header element), reference dependencies (include directive to the header file
 * that is included).
 * 
 * @author sandu.postaru
 *
 */

public class CppHeaderAndSourceTest extends CppTestHelper {

	private final String fileName = "completeExample";
	
	// modify this field to the actual doxygen path
	public static final String doxygenPath = "/usr/bin/doxygen"; 
	
	public CppHeaderAndSourceTest() {		
		initialise(fileName);	
		
		// enable the analysis of function calls
		// since the tests are run in a new eclipse environment we need to
		// override the default settings
		// 30 minutes lost here YAY! ><
		Activator.getDefault().getPreferenceStore().setDefault(CppAdapterPreferencePage.USE_FUNCTION_CALL_HIERARCHY,
				true);
		Activator.getDefault().getPreferenceStore().setDefault(CppAdapterPreferencePage.DOXYGEN_PATH,
				doxygenPath);
	}
	

	// 65 existing elements
	// header elements -> 24
	// source elements -> 41
	@Test
	@Override
	public void numberOfElementsTest() {
		Assert.assertTrue(elements.size() == 65);
	}

	@Test	
	// The only function that calls other functions is the main function
	public void functionCallDependenciesTest() {

		List<CppElement> functions = getElementsOfType(CppElementType.FUNCTION_IMPL);

		CppElement mainFunction = null;
		CppElement catSleepFunction = null;
		CppElement dogSoundFunction = null;

		for (CppElement function : functions) {
			String name = function.getRawText();
			if (name.equals("main")) {
				mainFunction = function;
			} else if (name.equals("Cat::sleep")) {
				catSleepFunction = function;
			} else if (name.equals("Dog::sound")) {
				dogSoundFunction = function;
			}
		}

		Assert.assertNotEquals(null, mainFunction);
		Assert.assertNotEquals(null, catSleepFunction);
		Assert.assertNotEquals(null, dogSoundFunction);

		// existence of call dependencies
		testDependencyExistance(mainFunction, DependencyManager.FUNCTION_CALL_DEPENDENCY_ID,
				new String[] { "Animal::sleep", "Animal::sound" });

		testDependencyExistance(catSleepFunction, DependencyManager.FUNCTION_CALL_DEPENDENCY_ID, new String[] {});
		testDependencyExistance(dogSoundFunction, DependencyManager.FUNCTION_CALL_DEPENDENCY_ID, new String[] {});
	}

	// function implementation in the source file is linked to the corresponding
	// function definition in the header file, except for the main function
	@Test
	public void functionImplToFunctionDefDependenciesTest() {

		// get function source elements
		List<CppElement> functionImpls = getElementsOfType(CppElementType.FUNCTION_IMPL);

		for (CppElement functionImpl : functionImpls) {
			String functioName = functionImpl.getRawText();
			if (!functioName.equals("main")) {

				// one header dependency
				List<CppElement> headerDeps = getDependenciesOfType(functionImpl,
						DependencyManager.HEADER_DEPENDENCY_ID);
				Assert.assertEquals(1, headerDeps.size());

				// Test ALSO the function header type,
				// (Important since having the same name doesn't guarantee
				// correction)
				// this is why testDependencyExistance was not used here
				CppElement.CppElementType headerDepType = headerDeps.get(0).getType();
				Assert.assertEquals(CppElementType.FUNCTION_H, headerDepType);

				// same name as the function impl name
				String headerDepName = headerDeps.get(0).getRawText();
				Assert.assertEquals(functioName, headerDepName);
			}
		}
	}

	@Test
	public void headerReferenceDependenciesTest() {

		CppElement includeA = null;
		CppElement includeD = null;
		CppElement includeC = null;

		List<CppElement> includes = getElementsOfType(CppElementType.INCLUDE_DIR);

		for (CppElement include : includes) {
			String name = include.getRawText();

			if (name.equals("#include \"animal.h\"")) {
				includeA = include;
			} else if (name.equals("#include \"cat.h\"")) {
				includeC = include;
			} else if (name.equals("#include \"dog.h\"")) {
				includeD = include;
			}
		}

		Assert.assertNotEquals(null, includeA);
		Assert.assertNotEquals(null, includeD);
		Assert.assertNotEquals(null, includeC);

		testDependencyExistance(includeA, DependencyManager.REFERENCE_DEPENDENCY_ID, new String[] { "animal.h" });
		testDependencyExistance(includeD, DependencyManager.REFERENCE_DEPENDENCY_ID, new String[] { "dog.h" });
		testDependencyExistance(includeC, DependencyManager.REFERENCE_DEPENDENCY_ID, new String[] { "cat.h" });

		// test the Non existence for some dependencies
		testDependencyExistance(includeA, DependencyManager.INHERITANCE_DEPENDENCY_ID, new String[] {});
		testDependencyExistance(includeD, DependencyManager.FUNCTION_CALL_DEPENDENCY_ID, new String[] {});
		testDependencyExistance(includeC, DependencyManager.STATEMENT_DEPENDENCY_ID, new String[] {});
	}
}
