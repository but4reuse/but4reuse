package org.but4reuse.adapters.cppcdt.elements;

import java.net.URI;

import org.but4reuse.utils.files.FileUtils;
import org.eclipse.cdt.core.dom.ast.IASTNode;

/**
 * This class contains the C++ element implementation for a #elif directive
 * Example : #elif MACRO_X f(x)
 * 
 * @author sandu.postaru
 */

public class ElifDirective extends CppElement {

	public ElifDirective(IASTNode node, CppElement parent, String text, String rawText) {
		super(node, parent, text, rawText, CppElementType.ELIF_DIR);
	}

	public void construct(URI uri) {

		if (parent != null && (parent instanceof SourceFile || parent instanceof HeaderFile)) {

			// Construct the parent if it doesn't exist
			parent.construct(uri);
			file = parent.file;

			try {
				String content = node.getRawSignature() + "\n";
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
