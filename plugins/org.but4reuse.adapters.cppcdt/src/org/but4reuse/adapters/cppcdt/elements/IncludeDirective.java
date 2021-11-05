package org.but4reuse.adapters.cppcdt.elements;

import java.net.URI;

import org.but4reuse.utils.files.FileUtils;
import org.eclipse.cdt.core.dom.ast.IASTNode;

/**
 * This class contains the C++ element implementation for a include directive
 * Example : #include <string> or #include "super_duper_file.h"
 * 
 * @author sandu.postaru
 */

public class IncludeDirective extends CppElement {

	public IncludeDirective(IASTNode node, CppElement parent, String text, String rawText) {
		super(node, parent, text, rawText, CppElementType.INCLUDE_DIR);
	}

	// TODO limitation, bug, "includes" which are inside preprocessor directives
	public void construct(URI uri) {
		if (!constructed) {
			if (parent != null && (parent instanceof SourceFile || parent instanceof HeaderFile)) {
				// Construct the parent if it doesn't exist
				parent.construct(uri);
				file = parent.file;

				try {
					String content = node.getRawSignature() + "\n";
					if (!content.startsWith("#")) {
						content = '#' + content;
					}
					FileUtils.appendToFile(file, content);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			constructed = true;
		}
	}

}
