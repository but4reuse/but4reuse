package org.but4reuse.adapters.cppcdt.elements;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.strings.StringUtils;
import org.eclipse.cdt.core.dom.ast.IASTNode;

/**
 * This class contains the C++ element implementation of a class header
 * definition. Example : class Animal { public: Animal(); };
 * 
 * @author sandu.postaru
 */

public class ClassHeader extends CppElement {

	public ClassHeader(IASTNode node, CppElement parent, String text, String rawText) {
		super(node, parent, text, rawText, CppElementType.CLASS_H);

		words = extractWords();
	}

	private List<String> extractWords() {

		// add the words for the word cloud
		List<String> wordList = new ArrayList<String>();
		wordList.addAll(StringUtils.tokenizeString(rawText));

		return wordList;
	}

	/**
	 * Construct the function element in the contained parent file.
	 */
	public void construct(URI uri) {

		if (!constructed) {

			if (parent instanceof HeaderFile) {

				// Construct the parent if it doesn't exist
				parent.construct(uri);
				file = parent.file;

				try {

					String content = "class " + rawText + "\n";
					FileUtils.appendToFile(file, content);

				} catch (Exception e) {
					e.printStackTrace();
				}
				constructed = true;
			} else {
				System.err.println("Parent of " + getClass() + " not a HeaderFile but a "
						+ ((parent != null) ? parent.getClass() : parent));
			}
		}
	}
}
