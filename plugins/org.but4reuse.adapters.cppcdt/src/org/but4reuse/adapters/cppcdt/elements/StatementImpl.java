package org.but4reuse.adapters.cppcdt.elements;

import org.eclipse.cdt.core.dom.ast.IASTNode;

/**
 * This class contains the C++ element implementation for statement node.
 * Example : int x = 40;
 * 
 * @author sandu.postaru
 */

public class StatementImpl extends CppElement {

	public StatementImpl(IASTNode node, CppElement parent, String text, String rawText) {
		super(node, parent, text, rawText, CppElementType.STATEMENT_IMPL);
	}

}
