package org.but4reuse.adapters.cppcdt.elements;

import java.net.URI;

import org.but4reuse.utils.files.FileUtils;
import org.eclipse.cdt.core.dom.ast.IASTNode;

/**
 * This class contains the C++ element implementation for a header file (.h
 * extension)
 * 
 * @author sandu.postaru
 */

public class HeaderFile extends CppElement {

	public HeaderFile(IASTNode node, CppElement parent, String text, String rawText) {
		super(node, parent, text, rawText, CppElementType.HEADER_FILE);
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
