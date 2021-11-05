package org.but4reuse.adapters.cppcdt.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.cppcdt.callhierarchy.xml.FunctionSignatureParser;
import org.but4reuse.adapters.cppcdt.dependencies.DependencyManager;
import org.but4reuse.adapters.cppcdt.elements.ClassHeader;
import org.but4reuse.adapters.cppcdt.elements.CppElement;
import org.but4reuse.adapters.cppcdt.elements.FunctionHeader;
import org.but4reuse.adapters.cppcdt.elements.GlobalVar;
import org.but4reuse.adapters.cppcdt.elements.HeaderFile;
import org.but4reuse.adapters.cppcdt.preprocessing.PreprocessorManager;
import org.but4reuse.adapters.cppcdt.utils.Pair;
import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTCompositeTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTParameterDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTCompositeTypeSpecifier.ICPPASTBaseSpecifier;

/**
 * This class is a visitor for the C++ header files. It creates the initial
 * elements from class names and method names. It resolves containment,
 * inheritance and reference dependencies.
 * 
 * @author sandu.postaru
 */

public class CppHeaderVisitor extends ASTVisitor {

	/** list of elements created by tree traversing. */
	private List<IElement> elementsList;

	/** header element used for class definition dependency. */
	private CppElement headerFileElement;

	/** parent class name used for a function definition dependency. */
	private String parentClassName;

	/** parent class element used for a function definition dependency. */
	private CppElement parentClassElement;

	/**
	 * The problem with inheritance dependencies is that we might parse a class
	 * B that inherits from class A before the class A has been parsed and its
	 * respective element has been created. So we memorize all the unresolved
	 * dependencies and resolve them when the whole AST has been created. The
	 * same goes for header inclusion and references.
	 */

	/** Class A inherits from Class B <Class A, Class B> . */
	private List<Pair<String, String>> unresolvedInheritanceDependecies;

	/** Include elements that need to be referenced to the original .h file. */
	private List<CppElement> unresolvedIncludeDependencies;

	/**
	 * Element name associated to it's respective CppElement representation.
	 */
	private Map<String, CppElement> elementsMap;

	/** String Buffer used for parameter concatenation. */
	private StringBuffer parameters;

	private File rootFile;

	public CppHeaderVisitor(boolean visitAllNodes, File root) {
		super(true);
		rootFile = root;
		elementsList = new ArrayList<IElement>();
		unresolvedInheritanceDependecies = new LinkedList<Pair<String, String>>();
		unresolvedIncludeDependencies = new LinkedList<CppElement>();
		elementsMap = new HashMap<String, CppElement>();
		parameters = new StringBuffer();

		reset();
	}

	/** Returns a list of the function and class elements. */
	public List<IElement> getElementsList() {
		return elementsList;
	}

	/**
	 * Returns a map (key: name, value: element) of the function and class
	 * elements.
	 */
	public Map<String, CppElement> getElementsMap() {
		return elementsMap;
	}

	/**
	 * Resets the internal data used for class and element linkage. This method
	 * should be called before a new file parsing.
	 */
	public void reset() {
		headerFileElement = null;
		parentClassName = null;
		parentClassElement = null;
	}

	@Override
	public int visit(IASTTranslationUnit translationUnit) {

		// header file node
		File file = new File(translationUnit.getContainingFilename());
		String relativePath = rootFile.toURI().relativize(file.toURI()).getPath();
		CppElement headerElement = new HeaderFile(translationUnit, null, relativePath, relativePath);
		headerFileElement = headerElement;

		elementsList.add(headerElement);
		elementsMap.put(file.getName(), headerElement);

		PreprocessorManager.resolveHeaderPreprocessorElements(headerElement, elementsList,
				unresolvedIncludeDependencies);

		return PROCESS_CONTINUE;
	}

	@Override
	public int visit(IASTName name) {

		// class node
		if ((name.getParent() instanceof IASTCompositeTypeSpecifier)) {

			String classElementName = name.getRawSignature() + CppElement.H_EXTENSION;
			CppElement classElement = new ClassHeader(name, headerFileElement, classElementName,
					name.getRawSignature());
			elementsList.add(classElement);

			// class name and element used for contained functions
			parentClassName = name.getRawSignature().trim();
			parentClassElement = classElement;

			// class name and element used for class inheritance dependencies
			elementsMap.put(parentClassName, parentClassElement);

			// containment dependency
			classElement.addDependency(DependencyManager.CONTAINMENT_DEPENDENCY_ID, headerFileElement);
		}

		// inheritance node
		else if (name.getParent() instanceof ICPPASTBaseSpecifier) {

			// inheritance link that needs to be resolved
			unresolvedInheritanceDependecies.add(new Pair<String, String>(parentClassName, name.getRawSignature()));
		}

		// function declaration node
		else if (name.getParent() instanceof IASTFunctionDeclarator) {

			IASTFunctionDeclarator declarator = (IASTFunctionDeclarator) (name.getParent());
			IASTNode[] children = declarator.getChildren();

			// reset the parameter list
			parameters.setLength(0);

			// compute the parameter type list
			for (IASTNode child : children) {
				if (child instanceof IASTParameterDeclaration) {
					IASTParameterDeclaration parameter = (IASTParameterDeclaration) child;
					IASTDeclSpecifier declSpecifier = parameter.getDeclSpecifier();
					String type = declSpecifier.getRawSignature();
					parameters.append(FunctionSignatureParser.TYPE_SEPARATOR + type);
				}
			}

			// the functionElementKey is the function signature
			String functionElementKey = (parentClassName + "::" + name.getRawSignature()).trim() + parameters;
			String functionElementName = functionElementKey + CppElement.H_EXTENSION;

			CppElement functionElement = new FunctionHeader(name, parentClassElement, functionElementName,
					functionElementKey);
			elementsList.add(functionElement);

			elementsMap.put(functionElementKey, functionElement);

			// function containment dependency
			functionElement.addDependency(DependencyManager.CONTAINMENT_DEPENDENCY_ID, parentClassElement);

		}

		return PROCESS_CONTINUE;
	}

	@Override
	public int visit(IASTDeclaration declaration) {

		// simple declaration with no parent class (does not suffice for
		// detecting
		// global variables since this might be a class definition, typedef etc)
		if (declaration instanceof IASTSimpleDeclaration && parentClassElement == null) {
			IASTSimpleDeclaration simpleDeclaration = (IASTSimpleDeclaration) declaration;
			IASTDeclSpecifier specifier = simpleDeclaration.getDeclSpecifier();

			if (!(specifier instanceof IASTCompositeTypeSpecifier)
					&& specifier.getStorageClass() == IASTDeclSpecifier.sc_unspecified) {
				CppElement globalVariableElement = new GlobalVar(declaration, headerFileElement,
						declaration.getRawSignature(), declaration.getRawSignature());
				elementsList.add(globalVariableElement);

				globalVariableElement.addDependency(DependencyManager.CONTAINMENT_DEPENDENCY_ID, headerFileElement);
			}
		}

		return PROCESS_CONTINUE;
	}

	public void resolveInheritanceDependencies() {
		DependencyManager.resolveInheritanceDependencies(unresolvedInheritanceDependecies, elementsMap);
	}

	public void resolveInclusionDependencies() {
		DependencyManager.resolveIncludeDependencies(unresolvedIncludeDependencies, elementsMap);
	}
}
