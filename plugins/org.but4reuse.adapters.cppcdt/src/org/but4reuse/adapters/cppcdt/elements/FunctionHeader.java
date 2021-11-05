package org.but4reuse.adapters.cppcdt.elements;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.cppcdt.callhierarchy.xml.FunctionSignatureParser;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.strings.StringUtils;
import org.eclipse.cdt.core.dom.ast.IASTNode;

/**
 * This class contains the C++ element implementation for a function header
 * definition. Example : LineBorder(double, double, double, double);
 * 
 * @author sandu.postaru
 */

public class FunctionHeader extends CppElement {

	public FunctionHeader(IASTNode node, CppElement parent, String text, String rawText) {
		super(node, parent, text, rawText, CppElementType.FUNCTION_H);

		words = extractWords();
	}

	private List<String> extractWords() {

		// add the words for the word cloud
		List<String> wordsList = new ArrayList<String>();

		// the raw text contains the function signature (name + parameters
		// types)
		// we are only interested in the name
		String[] tokens = rawText.split(FunctionSignatureParser.TYPE_SEPARATOR.toString());

		String classAndFunctionName = tokens[0];
		tokens = classAndFunctionName.split("::");

		// function that doesn't belong to a class (main for example);
		if (tokens.length == 1) {
			wordsList.addAll(StringUtils.tokenizeString(tokens[0]));
		} else {
			wordsList.addAll(StringUtils.tokenizeString(tokens[1]));
		}

		return wordsList;
	}

	/**
	 * Construct the function element in the contained parent file.
	 */
	public void construct(URI uri) {

		if (!constructed) {
			if (parent instanceof ClassHeader) {

				// Construct the parent if it doesn't exist
				parent.construct(uri);
				file = parent.file;

				// to get the function signature, locate the function
				// implementation dependency

				try {

					String content = "";

					if (node.getParent() != null && node.getParent().getParent() != null) {
						content = "\t" + node.getParent().getParent().getRawSignature() + "\n";
					}

					FileUtils.appendToFile(file, content);

				} catch (Exception e) {
					e.printStackTrace();
				}
				constructed = true;

			} else {
				System.err.println("FunctionHeader: Parent of " + getClass() + " not a HeaderFile but a "
						+ ((parent != null) ? parent.getClass() : parent));
			}
		}
	}
}
