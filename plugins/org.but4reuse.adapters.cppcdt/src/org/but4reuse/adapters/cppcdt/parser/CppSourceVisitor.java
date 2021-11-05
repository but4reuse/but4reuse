package org.but4reuse.adapters.cppcdt.parser;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.cppcdt.callhierarchy.xml.FunctionSignatureParser;
import org.but4reuse.adapters.cppcdt.dependencies.DependencyManager;
import org.but4reuse.adapters.cppcdt.elements.CppElement;
import org.but4reuse.adapters.cppcdt.elements.FunctionImpl;
import org.but4reuse.adapters.cppcdt.elements.GlobalVar;
import org.but4reuse.adapters.cppcdt.elements.SourceFile;
import org.but4reuse.adapters.cppcdt.elements.StatementImpl;
import org.but4reuse.adapters.cppcdt.preprocessing.PreprocessorManager;
import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTCompositeTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTParameterDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTFunctionDeclarator;

/**
 * This class is a visitor for the C++ source files. It creates the initial
 * elements from method names. It resolves containment and reference
 * dependencies.
 * 
 * @author sandu.postaru
 */

public class CppSourceVisitor extends ASTVisitor {

	/** List of elements created by tree traversing. */
	private List<IElement> elementsList;

	/** Element name associated to it's respective CppElement representation. */
	private Map<String, CppElement> elementsMap;

	/**
	 * Statements map used for resolving parenthood link for statement elements.
	 */
	private Map<IASTNode, CppElement> statementsMap;

	/** String Buffer used for parameter concatenation. */
	private StringBuffer parameters;

	/** Source element used for function definition dependency. */
	private CppElement sourceFileElement;

	/** Function element used for detecting global variables */
	private CppElement sourceFunctionElement;

	private File rootFile;

	public CppSourceVisitor(boolean visitAll, List<IElement> elementsList, Map<String, CppElement> elementsMap,
			File root) {
		super(visitAll);
		rootFile = root;
		this.elementsList = elementsList;
		this.elementsMap = elementsMap;
		this.statementsMap = new HashMap<IASTNode, CppElement>();
		parameters = new StringBuffer();
	}

	/**
	 * Resets the internal data used for class and element linkage. This method
	 * should be called before a new file parsing.
	 */
	public void reset() {
		sourceFileElement = null;
		sourceFunctionElement = null;
	}

	/**
	 * Provides the full list of created elements.
	 * 
	 * @return a list of created elements.
	 */
	public List<IElement> getElementsList() {
		return elementsList;
	}

	@Override
	public int visit(IASTTranslationUnit translationUnit) {

		// source file node
		File file = new File(translationUnit.getContainingFilename());
		String relativePath = rootFile.toURI().relativize(file.toURI()).getPath();
		CppElement sourceElement = new SourceFile(translationUnit, null, relativePath, relativePath);
		sourceFileElement = sourceElement;

		elementsList.add(sourceElement);

		PreprocessorManager.resolveSourcePreprocessorElements(sourceElement, elementsList, elementsMap);

		return PROCESS_CONTINUE;
	}

	@Override
	public int visit(IASTDeclaration declaration) {

		if (declaration instanceof IASTFunctionDefinition) {

			IASTFunctionDefinition functionDefinition = (IASTFunctionDefinition) declaration;
			ICPPASTFunctionDeclarator functionDeclarator = (ICPPASTFunctionDeclarator) functionDefinition
					.getDeclarator();

			IASTNode[] children = functionDeclarator.getChildren();

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

			String functionElementKey = functionDeclarator.getName().toString().trim() + parameters;
			String functionElementName = functionElementKey + CppElement.IMPL_EXTENSION;
			CppElement functionElement = new FunctionImpl(declaration, sourceFileElement, functionElementName,
					functionElementKey);

			sourceFunctionElement = functionElement;

			elementsList.add(functionElement);

			// the first statement of the function will depend on this
			// element
			statementsMap.put(declaration, functionElement);

			CppElement functionHeaderElement = elementsMap.get(functionElementKey);

			functionElement.addDependency(DependencyManager.CONTAINMENT_DEPENDENCY_ID, sourceFileElement);

			if (functionHeaderElement != null) {
				functionElement.addDependency(DependencyManager.HEADER_DEPENDENCY_ID, functionHeaderElement);
			} else {
				System.err
						.println("CppSourceVisitor: No header element found for function name: " + functionElementName);
			}

			String newKey = functionElementKey + CppElement.IMPL_EXTENSION;
			elementsMap.put(newKey, functionElement);
		}
		// possible global variable
		else if (declaration instanceof IASTSimpleDeclaration && sourceFunctionElement == null) {
			IASTSimpleDeclaration simpleDeclaration = (IASTSimpleDeclaration) declaration;
			IASTDeclSpecifier specifier = simpleDeclaration.getDeclSpecifier();

			if (!(specifier instanceof IASTCompositeTypeSpecifier)
					&& specifier.getStorageClass() == IASTDeclSpecifier.sc_unspecified) {

				CppElement globalVariableElement = new GlobalVar(declaration, sourceFileElement,
						declaration.getRawSignature(), declaration.getRawSignature());
				elementsList.add(globalVariableElement);

				globalVariableElement.addDependency(DependencyManager.CONTAINMENT_DEPENDENCY_ID, sourceFileElement);
			}
		}

		return PROCESS_CONTINUE;
	}

	public int visit(IASTStatement statement) {

		IASTNode parent = statement.getParent();
		String statementName = statement.getRawSignature() + CppElement.IMPL_EXTENSION;
		CppElement parentElement = null;

		if (parent != null) {
			parentElement = statementsMap.get(parent);

		} else {
			System.err.println("Null parent for " + statementName);
		}

		CppElement statementElement = new StatementImpl(statement, parentElement, statementName,
				statement.getRawSignature());

		elementsList.add(statementElement);
		statementsMap.put(statement, statementElement);

		if (parentElement != null) {
			statementElement.addDependency(DependencyManager.STATEMENT_DEPENDENCY_ID, parentElement);
		} else {
			System.err.println("Null parent element for statement " + statementName);
		}

		return PROCESS_CONTINUE;
	}
}
