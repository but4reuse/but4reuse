package org.but4reuse.adapters.cppcdt.elements;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.cppcdt.callhierarchy.xml.FunctionSignatureParser;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.strings.StringUtils;
import org.eclipse.cdt.core.dom.ast.IASTNode;

/**
 * This class contains the C++ element implementation for a function
 * implementation node. Example : std::string LineBorder::SVGPrintString() const
 * { return "so tired ... " }
 * 
 * 
 * @author sandu.postaru
 */

public class FunctionImpl extends CppElement {

	public FunctionImpl(IASTNode node, CppElement parent, String text, String rawText) {
		super(node, parent, text, rawText, CppElementType.FUNCTION_IMPL);

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

		if (parent != null && parent instanceof SourceFile) {

			// Construct the parent if it doesn't exist
			parent.construct(uri);
			file = parent.file;

			try {
				String content = "\n" + node.getRawSignature() + "\n";
				FileUtils.appendToFile(file, content);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Parent of " + getClass() + " not a SourceFile but a "
					+ ((parent != null) ? parent.getClass() : parent));
		}
	}
}
