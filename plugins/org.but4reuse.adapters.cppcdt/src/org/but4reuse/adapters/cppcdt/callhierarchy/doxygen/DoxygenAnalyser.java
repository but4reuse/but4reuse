package org.but4reuse.adapters.cppcdt.callhierarchy.doxygen;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.cppcdt.activator.Activator;
import org.but4reuse.adapters.cppcdt.callhierarchy.dot.DOTParser;
import org.but4reuse.adapters.cppcdt.callhierarchy.dot.DotElement;
import org.but4reuse.adapters.cppcdt.callhierarchy.xml.FunctionSignatureParser;
import org.but4reuse.adapters.cppcdt.dependencies.DependencyManager;
import org.but4reuse.adapters.cppcdt.elements.CppElement;
import org.but4reuse.adapters.cppcdt.preferences.CppAdapterPreferencePage;
import org.but4reuse.adapters.cppcdt.utils.Pair;
import org.but4reuse.utils.files.FileUtils;

/**
 * Using the Doxygen library to generate the call graphs.
 * DOXYGEN VERSION : Linux 1.8.13
 * 
 * @author sandu.postaru
 *
 */

/**
 * First of all the call graphs contain only the function name with no real
 * signature. Without the function signature it is impossible to find the actual
 * CPP element. In order to find that signature we can use a internal .dot id
 * that maps to the signature. This mapping is contained in the .xml files.
 *
 * So we begin by computing a internal mapping of the .dot id and the function
 * signature. Then we analyze the callgraph nodes and with the id we have the
 * function signature and the element. Then we analyze the callgraph edges and
 * we compute the actual function calls between the elements.
 * 
 * So yeah ... since parsing is involved this implementation is dependent on the
 * doxygen changes...
 * 
 * This is some ugly code so here's a joke to give you courage ! - Two bytes
 * meet. The first byte asks, â€œAre you ill?â€� - The second byte replies,
 * â€œNo, just feeling a bit off.
 * 
 */

public class DoxygenAnalyser {

	/**
	 * Folder containing the source code that needs be analyzed.
	 */
	private File cppSourceFolder;

	/**
	 * Doxygen configuration abstraction.
	 */
	private DoxygenConfig config;

	/**
	 * Location for the doxygen binary.
	 */
	private String doxygenBinaryLocation;

	/**
	 * Element name associated to it's respective CppElement representation.
	 */
	private Map<String, CppElement> elementsMap;

	public DoxygenAnalyser(File cppSourceFolder, Map<String, CppElement> elementsMap) {

		this.cppSourceFolder = cppSourceFolder;
		this.elementsMap = elementsMap;

		doxygenBinaryLocation = Activator.getDefault().getPreferenceStore()
				.getString(CppAdapterPreferencePage.DOXYGEN_PATH);
	}

	/**
	 * Analyze all the graphs and establish the call dependencies.
	 */

	public void analyse() {

		config = new DoxygenConfig(cppSourceFolder);
		config.write();

		List<String> command = new ArrayList<String>();
		command.add(doxygenBinaryLocation);
		command.add(config.getConfigFile().getAbsolutePath());

		try {

			ProcessBuilder builder = new ProcessBuilder(command);
			builder.redirectErrorStream(true);

			Process process = builder.start();

			// some monkey business to make it work on Windows
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((reader.readLine()) != null) {
			}

			process.waitFor();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		extractCallHierarchy();

		dispose();

	}

	private boolean isCallGraphFile(File file) {
		return file.getName().endsWith("cgraph.dot");
	}

	private void extractCallHierarchy() {

		List<File> files = FileUtils.getAllFiles(config.getOutputFolderHtml());

		// Map the function signatures to the .dot id
		List<File> xmlFiles = FileUtils.getAllFiles(config.getOutputFolderXml());
		FunctionSignatureParser xmlParser = new FunctionSignatureParser();

		for (File xmlFile : xmlFiles) {
			if (FileUtils.isExtension(xmlFile, "xml")) {
				xmlParser.parse(xmlFile);
			}
		}

		// This map contains [key : .dot id, value : real function signature]
		Map<String, String> functionSignatureMap = xmlParser.getSignatureMap();

		// Once the function signatures has been computed, we can analyse the
		// call graphs
		for (File file : files) {
			if (isCallGraphFile(file)) {

				Pair<List<DotElement>, List<DotElement>> dotElements = DOTParser.parse(file);

				List<DotElement> functionDefs = dotElements.first;
				List<DotElement> functionCalls = dotElements.second;

				// This map contains [key : node id (used for linking two call
				// functions), value : CppElement)
				Map<String, CppElement> nodeMap = new HashMap<String, CppElement>();

				// Find for each node in the file, the corresponding Cpp Element
				for (DotElement functionDef : functionDefs) {
					String nodeId = functionDef.content.first;
					String nodeName = functionDef.content.second;
					String nodeRef = functionDef.id;

					if (nodeRef != null) {

						String signature = functionSignatureMap.get(nodeRef);

						if (signature != null) {

							// check for a function header element
							CppElement functionElement = elementsMap.get(signature);

							if (functionElement != null) {
								nodeMap.put(nodeId, functionElement);
							} else {
								// maybe it's the main function with no header
								// definition
								functionElement = elementsMap.get(signature + CppElement.IMPL_EXTENSION);

								if (functionElement != null) {
									nodeMap.put(nodeId, functionElement);
								} else {
									System.err.println("DoxygenAnalyser: Null element for " + signature);
								}
							}
						} else {
							System.err.println("DoxygenAnalyser: Unexisting signature for " + nodeName + " " + nodeRef);
						}
					} else {
						System.err.println("DoxygenAnalyser: Null reference for : " + nodeName);
					}
				}

				// For each edge in the file, add a function call dependency if
				// one doesn't exist already
				for (DotElement functionCall : functionCalls) {
					String callerId = functionCall.content.first;
					String calleeId = functionCall.content.second;

					CppElement callerElement = nodeMap.get(callerId);
					CppElement calleeElement = nodeMap.get(calleeId);

					if (callerElement != null && calleeElement != null) {

						if (!functionDependencyExists(callerElement, calleeElement)) {
							callerElement.addDependency(DependencyManager.FUNCTION_CALL_DEPENDENCY_ID, calleeElement);
						}

					} else {
						System.err.println("DoxygenAnalyser: Null caller or calee for " + callerId + " " + calleeId);
					}

				}
			}
		}
	}

	public void dispose() {
		config.dispose();
	}

	private boolean functionDependencyExists(CppElement caller, CppElement callee) {
		Map<String, List<IDependencyObject>> dependencies = caller.getDependencies();

		List<IDependencyObject> callDependencies = dependencies.get(DependencyManager.FUNCTION_CALL_DEPENDENCY_ID);

		if (callDependencies == null)
			return false;

		for (IDependencyObject existingCallee : callDependencies) {
			if (existingCallee.equals(callee))
				return true;
		}

		return false;

	}
}
