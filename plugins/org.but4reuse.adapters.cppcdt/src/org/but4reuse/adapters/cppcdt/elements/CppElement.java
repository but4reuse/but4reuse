package org.but4reuse.adapters.cppcdt.elements;

import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.eclipse.cdt.core.dom.ast.IASTNode;

/**
 * This class contains the C++ element implementation.
 * 
 * @author sandu.postaru
 */

public abstract class CppElement extends AbstractElement {

	public static enum CppElementType {
		CLASS_H, FUNCTION_H, FUNCTION_IMPL, STATEMENT_IMPL, HEADER_FILE, SOURCE_FILE, INCLUDE_DIR, MACRO_DIR, IFDEF_DIR, IFNDEF_DIR, IF_DIR, ELIF_DIR, ELSE_DIR, ENDIF_DIR, GLOBAL_VAR
	};

	public static final String H_EXTENSION = "~~H";
	public static final String IMPL_EXTENSION = "~~IMPL";

	/** AST node representing the current element. */
	protected IASTNode node;

	/** The parent CppElement for the current element. */
	protected CppElement parent;

	/** Textual representation. */
	protected String text;

	/**
	 * Raw textual representation with no additional information such as
	 * Implementation or Header
	 */
	protected String rawText;

	/** Element type */
	protected CppElementType type;

	protected List<String> words;

	protected boolean constructed;

	/** the file in which this element is contained during it's construction */
	protected File file;

	public CppElement(IASTNode node, CppElement parent, String text, String rawText, CppElementType type) {
		super();
		this.node = node;
		this.parent = parent;
		this.text = text;
		this.rawText = rawText;
		this.type = type;

		// by default we return an empty list
		// for performance reasons we avoid creating a new empty list for each
		// element,
		// instead each subclass needs to create if needed this list and defin
		// its own behavior
		words = Collections.emptyList();
		constructed = false;
	}

	@Override
	public double similarity(IElement anotherElement) {

		// different text
		if (!anotherElement.getText().equals(getText())) {
			return 0.;
		}

		// text is equal, check parents
		CppElement currentElementParent = this.getParent();
		CppElement anotherElementParent = ((CppElement) anotherElement).getParent();

		// both parents are null and every text is equal
		if (currentElementParent == null && anotherElementParent == null) {
			return 1.0;
		}
		// one parent is null
		else if (currentElementParent == null || anotherElementParent == null) {
			return 0.;
		}
		// check parents
		return currentElementParent.similarity(anotherElementParent);
	}

	/**
	 *
	 * Given an output URI, this method should construct an artefact containing
	 * the element
	 * 
	 * @param uri
	 *            output URI
	 */
	public void construct(URI uri) {

		// default behavior -> don't construct
	}

	@Override
	public List<String> getWords() {
		return words;
	}

	@Override
	public String getText() {
		return "[" + type + "] " + text;
	}

	public String getRawText() {
		return rawText;
	}

	public IASTNode getNode() {
		return node;
	}

	public CppElement getParent() {
		return parent;
	}

	public CppElementType getType() {
		return type;
	}

	public void resetConstructFlag() {
		constructed = false;
	}

	@Override
	/**
	 * To improve performance. We put the same hash when they have the same
	 * text.
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getText() == null) ? 0 : getText().hashCode());
		return result;
	}
}
