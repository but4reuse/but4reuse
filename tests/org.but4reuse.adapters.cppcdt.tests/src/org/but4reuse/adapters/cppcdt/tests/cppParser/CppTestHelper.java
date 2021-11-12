package org.but4reuse.adapters.cppcdt.tests.cppParser;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.cppcdt.elements.CppElement;
import org.but4reuse.adapters.cppcdt.elements.CppElement.CppElementType;
import org.but4reuse.adapters.cppcdt.parser.CppParser;
import org.eclipse.core.runtime.CoreException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Base class for all the CppParser test classes.
 * Contains some helper management and test methods.
 * 
 * @author sandu.postaru
 *
 */

public abstract class CppTestHelper {

	protected List<CppElement> elements;
	protected File root;

	public CppTestHelper() {
		elements = new LinkedList<CppElement>();
	}
	
	public void initialise(String fileName){
		List<IElement> iElements = Collections.emptyList();
		root = Paths.get("data", "cppParser", fileName).toFile();

		try {
			iElements = CppParser.parse(root);
		} catch (CoreException e) {

			e.printStackTrace();
		}

		for (IElement iElement : iElements) {
			Assert.assertTrue(iElement instanceof CppElement);
			elements.add((CppElement) iElement);
		}
	}
	
	@Test
	public abstract void numberOfElementsTest();

	/**
	 * Returns a list of all the elements that have the required type
	 * @param type the type of the searched elements
	 * @return a list of all the elements of that type.
	 */
	public List<CppElement> getElementsOfType(CppElementType type) {
		List<CppElement> result = new LinkedList<CppElement>();

		for (CppElement element : elements) {
			if (element.getType() == type) {
				result.add(element);
			}
		}

		return result;
	}

	/**
	 * Returns a list of all the dependencies of a certain type, for a certain element.
	 * @param element the investigated element
	 * @param type the type of dependency
	 * @return a list of all the dependencies
	 */
	public List<CppElement> getDependenciesOfType(CppElement element, String type) {
		Map<String, List<IDependencyObject>> dependencies = element.getDependencies();
		List<IDependencyObject> dependenciesElements = dependencies.get(type);

		List<CppElement> result = Collections.emptyList();

		if (dependenciesElements != null) {
			result = new LinkedList<CppElement>();

			for (IDependencyObject dependency : dependenciesElements) {
				if (dependency instanceof CppElement) {
					result.add((CppElement) dependency);
				}
			}
		}

		return result;
	}

	/**
	 * Tests the existence of all elements of type : type, described in
	 * testingForElementNames
	 * 
	 * @param type
	 *            the type of elements to be checked
	 * @param testingForElementNames
	 *            names of the elements to be checked
	 */

	public void testElementExistance(CppElementType type, String[] testingForElementNames) {

		List<CppElement> elements = getElementsOfType(type);
		List<String> testingElementNames = Arrays.asList(testingForElementNames);

		// test the size
		Assert.assertEquals(testingElementNames.size(), elements.size());

		// extract the names
		List<String> elementNames = new LinkedList<String>();
		for (CppElement element : elements) {
			elementNames.add(element.getRawText());
		}

		// test the existence
		for (String testingName : testingElementNames) {
			if (!elementNames.contains(testingName)) {
				Assert.fail("Element not present : " + testingName);
			}
		}
	}

	/**
	 * Tests the existence of all dependencies of type : dependencyType,
	 * described in testingDependencies for the current element
	 * 
	 * @param element
	 *            the element containing the dependencies
	 * @param dependencyType
	 *            the type of dependencies
	 * @param testingDependencies
	 *            all the dependencies names that will be tested
	 * @return true if all the dependencies are contained, false otherwise
	 */
	public void testDependencyExistance(CppElement element, String dependencyType, String[] testingForDependencies) {
		List<CppElement> dependencies = getDependenciesOfType(element, dependencyType);
		List<String> testingDependencies = Arrays.asList(testingForDependencies);

		// test the size
		Assert.assertEquals(testingDependencies.size(), dependencies.size());

		// extract the names
		List<String> dependencyNames = new LinkedList<String>();
		for (CppElement dependency : dependencies) {
			dependencyNames.add(dependency.getRawText());
		}

		// test the existance
		for (String testingDependencyName : testingDependencies) {
			if (!dependencyNames.contains(testingDependencyName)) {
				Assert.fail("Dependency not present: " + testingDependencyName);
			}
		}
	}

}
