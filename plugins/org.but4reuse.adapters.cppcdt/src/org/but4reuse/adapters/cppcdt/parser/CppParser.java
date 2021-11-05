package org.but4reuse.adapters.cppcdt.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.cppcdt.activator.Activator;
import org.but4reuse.adapters.cppcdt.callhierarchy.doxygen.DoxygenAnalyser;
import org.but4reuse.adapters.cppcdt.dependencies.DependencyManager;
import org.but4reuse.adapters.cppcdt.elements.CppElement;
import org.but4reuse.adapters.cppcdt.preferences.CppAdapterPreferencePage;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.model.ILanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.core.runtime.CoreException;

/**
 * This class uses the CDT plugin in order to parse C++ code . For a specific
 * project we begin by parsing all the header files and constructing class and
 * function elements and then resolving inheritance and containment
 * dependencies.
 * 
 * If the user selected the use of the function call hierarchy via the
 * preference page then we analyze it by using DOXYGEN.
 * 
 * TODO Complete once the parser is finished
 *
 * @author sandu.postaru
 */

public class CppParser {

	public final static String HEADER = "h";
	public final static String SOURCE_CPP = "cpp";
	public final static String SOURCE_C = "c";

	// enforce the static behavior by disallowing elements construction

	private CppParser() {
	};

	public static List<IElement> parse(File root) throws CoreException {

		// parser initialization boilerplate

		Map<String, String> definedSymbols = new HashMap<String, String>();
		String[] includePaths = new String[0];
		IScannerInfo info = new ScannerInfo(definedSymbols, includePaths);
		IParserLogService log = new DefaultLogService();

		IncludeFileContentProvider emptyIncludes = IncludeFileContentProvider.getEmptyFilesProvider();

		int options = 0;

		List<File> files = FileUtils.getAllFiles(root);
		List<File> headerFiles = new ArrayList<File>();
		List<File> sourceFiles = new ArrayList<File>();

		// filter between header files and source files
		for (File file : files) {

			if (FileUtils.isExtension(file, HEADER)) {
				headerFiles.add(file);
			} else if (FileUtils.isExtension(file, SOURCE_CPP) || FileUtils.isExtension(file, SOURCE_C)) {
				sourceFiles.add(file);
			}
		}

		// --------- start visiting the header files
		CppHeaderVisitor headerVisitor = new CppHeaderVisitor(true, root);

		for (File header : headerFiles) {

			FileContent fileContent = FileContent.createForExternalFileLocation(header.getAbsolutePath());

			IASTTranslationUnit translationUnit = GPPLanguage.getDefault().getASTTranslationUnit(fileContent, info,
					emptyIncludes, null, options, log);

			translationUnit.accept(headerVisitor);

			headerVisitor.reset();

		}

		// resolve inheritance dependencies
		headerVisitor.resolveInheritanceDependencies();

		// resolve inclusion dependencies (local header files )
		headerVisitor.resolveInclusionDependencies();

		List<IElement> elementsList = headerVisitor.getElementsList();
		Map<String, CppElement> headerElementsMap = headerVisitor.getElementsMap();

		// --------- start visiting the source files
		options = ILanguage.OPTION_IS_SOURCE_UNIT;

		CppSourceVisitor sourceVisitor = new CppSourceVisitor(true, elementsList, headerElementsMap, root);

		for (File source : sourceFiles) {

			FileContent fileContent = FileContent.createForExternalFileLocation(source.getAbsolutePath());

			IASTTranslationUnit translationUnit = GPPLanguage.getDefault().getASTTranslationUnit(fileContent, info,
					emptyIncludes, null, options, log);

			translationUnit.accept(sourceVisitor);

			sourceVisitor.reset();
		}

		elementsList = sourceVisitor.getElementsList();

		DependencyManager.printElementsWithDependencies(elementsList);

		// function call hierarchy
		boolean useFunctionCallHierarchy = Activator.getDefault().getPreferenceStore()
				.getBoolean(CppAdapterPreferencePage.USE_FUNCTION_CALL_HIERARCHY);

		if (useFunctionCallHierarchy) {
			DoxygenAnalyser analyser = new DoxygenAnalyser(root, headerElementsMap);
			analyser.analyse();
		}

		// analyser.dispose();
		return elementsList;
	}

}
