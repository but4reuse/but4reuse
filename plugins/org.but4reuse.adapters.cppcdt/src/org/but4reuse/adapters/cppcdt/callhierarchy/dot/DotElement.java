package org.but4reuse.adapters.cppcdt.callhierarchy.dot;

import org.but4reuse.adapters.cppcdt.utils.Pair;

/**
 * This class represents a Dot Element, used in constructing the function call
 * hierarchy. The element is either a Node representing a function name, or a
 * dependency between two nodes (functions, the first calling the second) .
 * 
 * @author sandu.postaru
 *
 */

public class DotElement {
	public static enum Type {
		FUNCTION_DEF, FUNCTION_CALL
	};

	public final Type type;
	public final Pair<String, String> content;
	public String id;

	public DotElement(String first, String second, Type type) {
		content = new Pair<String, String>(first, second);
		this.type = type;
	}

	@Override
	public String toString() {
		return "DotElement [type=" + type + ", content=" + content + ", id=" + id + "]";
	}
}
