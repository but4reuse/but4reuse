package org.but4reuse.adapters.cppcdt.dependencies;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.cppcdt.elements.CppElement;
import org.but4reuse.adapters.cppcdt.elements.CppElement.CppElementType;
import org.but4reuse.adapters.cppcdt.utils.Pair;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;

public class DependencyManager {

	public static final String HEADER_DEPENDENCY_ID = "header";
	public static final String STATEMENT_DEPENDENCY_ID = "statement";
	public static final String INHERITANCE_DEPENDENCY_ID = "inherits";
	public static final String CONTAINMENT_DEPENDENCY_ID = "contained";
	public static final String REFERENCE_DEPENDENCY_ID = "reference";
	public static final String FUNCTION_CALL_DEPENDENCY_ID = "call";

	private DependencyManager() {
	};

	/**
	 * Resolves inheritance dependencies. This method NEEDS to be called after
	 * all header files have been visited (meaning that all the class elements
	 * now exist in memory)
	 * 
	 * @param unresolvedInheritanceDependecies
	 *            dependencies that need be resolved
	 * @param elementsMap
	 *            the elements map (name -> CppElement)
	 */

	public static void resolveInheritanceDependencies(List<Pair<String, String>> unresolvedInheritanceDependecies,
			Map<String, CppElement> elementsMap) {

		for (Pair<String, String> dependency : unresolvedInheritanceDependecies) {
			String baseClass = dependency.first;
			String superClass = dependency.second;

			CppElement baseClassElement = elementsMap.get(baseClass);
			CppElement superClassElement = elementsMap.get(superClass);

			if (baseClassElement != null && superClassElement != null) {
				baseClassElement.addDependency(DependencyManager.INHERITANCE_DEPENDENCY_ID, superClassElement);
			} else {
				System.err
						.println("DependencyManager: Null reference in [resolveInheritanceDependencies] baseClass key: "
								+ baseClass + " superClass key: " + superClass);
			}
		}
	}

	/**
	 * Resolves include reference dependencies. This method NEEDS to be called
	 * after all header files have been visited (meaning that all the header
	 * elements now exist in memory)
	 * 
	 * @param unresolvedIncludeDependencies
	 *            dependencies that need be resolved
	 * @param elementsMap
	 *            the elements map (name -> CppElement)
	 */

	public static void resolveIncludeDependencies(List<CppElement> unresolvedIncludeDependencies,
			Map<String, CppElement> elementsMap) {

		for (CppElement includeElement : unresolvedIncludeDependencies) {
			IASTPreprocessorIncludeStatement includeNode = (IASTPreprocessorIncludeStatement) includeElement.getNode();
			File file = new File(includeNode.getName().toString());

			CppElement headerElement = elementsMap.get(file.getName());

			if (headerElement != null) {
				includeElement.addDependency(DependencyManager.REFERENCE_DEPENDENCY_ID, headerElement);
			} else {
				System.err.println(
						"DependencyManager: Null reference in [resolveInclusionDependencies] Destination header : "
								+ file.getName());

			}
		}
	}

	public static void printElementsWithDependencies(List<IElement> elements) {

		for (IElement element : elements) {
			CppElement e = (CppElement) element;

			if (e.getType() == CppElementType.INCLUDE_DIR) {

				Map<String, List<IDependencyObject>> dependencies = e.getDependencies();
				Map<String, List<IDependencyObject>> dependants = e.getDependants();

				System.out.println(e + " - > " + e.getParent());

				System.out.println("dependencies -> : ");
				for (Map.Entry<String, List<IDependencyObject>> entry : dependencies.entrySet()) {
					System.out.println("Key : " + entry.getKey());

					for (IDependencyObject d : entry.getValue()) {
						System.out.println("Object : " + d);
					}
				}

				System.out.println("dependendants -> : ");
				for (Map.Entry<String, List<IDependencyObject>> entry : dependants.entrySet()) {
					System.out.println("Key : " + entry.getKey());

					for (IDependencyObject d : entry.getValue()) {
						System.out.println("Object : " + d);
					}
				}
			}
		}
	}
}
