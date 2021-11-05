package org.but4reuse.adapters.cppcdt.callhierarchy.dot;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.but4reuse.adapters.cppcdt.utils.Pair;
import org.but4reuse.utils.files.FileUtils;

/**
 * This is very naive, and poorly optimized DOTParser, used to extract the call
 * hierarchy from a dot graph. DOXYGEN VERSION : Linux 1.8.13
 * 
 * @author sandu.postaru
 *
 */

public class DOTParser {

	/**
	 * Parses a dot file representing a call graph and a pair of two lists. The
	 * first list has all the nodes and the second one all the dependencies.
	 * This avoids filtering in the caller function.
	 * 
	 * @param file
	 *            the callgraph file
	 * @return a pair of two node lists
	 */
	public static Pair<List<DotElement>, List<DotElement>> parse(File file) {

		List<String> lines = FileUtils.getLinesOfFile(file);

		List<DotElement> dotFunctionDefs = new LinkedList<DotElement>();
		List<DotElement> dotFunctionCalls = new LinkedList<DotElement>();

		String fileName = file.getName();

		for (String line : lines) {

			line = line.trim();

			/* node definition */
			if (line.startsWith("Node")) {

				int spacePosition = line.indexOf(' ');
				String from = line.substring(0, spacePosition);
				String to = null;
				String label = null;
				String id = null;

				/* ignore the space */
				line = line.substring(spacePosition + 1);

				/* dependency */
				if (line.startsWith("->")) {

					spacePosition = line.indexOf(' ');
					line = line.substring(spacePosition + 1);
					spacePosition = line.indexOf(' ');

					to = line.substring(0, spacePosition + 1);

					line = line.substring(spacePosition + 1);

				} else {

					/* find name */
					int labelPosition = line.indexOf("label=\"");
					int commaPosition = line.indexOf("\",");

					label = line.substring(labelPosition + 7, commaPosition);

					int urlPosition = line.indexOf("URL=");

					if (urlPosition != -1) {
						line = line.substring(urlPosition + 5);
						urlPosition = line.indexOf("\"");
						id = line.substring(0, urlPosition);

					}
					// the first node definition doesn't contain a field id,
					// but instead contains the id in the file name
					else if (!label.equals("main")) {

						id = transformFileName(fileName);
					}
				}

				// function definition
				if (to == null && label != null) {

					if (id != null) {
						DotElement dotElement = new DotElement(from.trim(), sanitize(label),
								DotElement.Type.FUNCTION_DEF);
						dotFunctionDefs.add(dotElement);
						dotElement.id = id;
					}
					// the main program doesn't have an id so create a false one
					else if (label.equals("main")) {
						DotElement dotElement = new DotElement(from.trim(), label, DotElement.Type.FUNCTION_DEF);
						dotFunctionDefs.add(dotElement);
						dotElement.id = "main";

					}
				}
				// function call
				else {
					dotFunctionCalls.add(new DotElement(from.trim(), to.trim(), DotElement.Type.FUNCTION_CALL));
				}
			}
		}

		return new Pair<List<DotElement>, List<DotElement>>(dotFunctionDefs, dotFunctionCalls);
	}

	/**
	 * Removes odd characters from dot labels.
	 * 
	 * @param name
	 *            label name
	 * @return sanitized label
	 */
	private static String sanitize(String label) {
		return label.replaceAll("\\\\l", "");
	}

	/**
	 * Extracts an id from a file name. The first node of a graph has it's id in
	 * the file name.
	 * 
	 * @param name
	 *            the name of the file
	 * @return the extracted id
	 */

	private static String transformFileName(String name) {
		String[] tokens = name.split("_");

		return "$" + tokens[0] + ".html#" + tokens[1];
	}
}
