package org.but4reuse.adapters.cppcdt.elements;

import java.net.URI;

import org.but4reuse.utils.files.FileUtils;
import org.eclipse.cdt.core.dom.ast.IASTNode;

/**
 * This class contains the C++ element implementation for a source file (.cpp
 * extension)
 * 
 * @author sandu.postaru
 */

public class SourceFile extends CppElement {

	public SourceFile(IASTNode node, CppElement parent, String text, String rawText) {
		super(node, parent, text, rawText, CppElementType.SOURCE_FILE);
	}

	/**
	 * Create an empty file with the required name.
	 */
	public void construct(URI uri) {

		if (!constructed) {

			// Use the given file or use a default name if a folder was given
			if (uri.toString().endsWith("/")) {
				try {
					uri = new URI(uri.toString() + rawText);

					// Create the file if it does not exist
					file = FileUtils.getFile(uri);
					FileUtils.createFile(file);

				} catch (Exception e) {
					e.printStackTrace();
				}

				constructed = true;
			}
		}
	}

}
