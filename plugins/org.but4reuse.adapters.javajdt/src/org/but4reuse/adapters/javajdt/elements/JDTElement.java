package org.but4reuse.adapters.javajdt.elements;

import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.utils.strings.StringUtils;
import org.eclipse.jdt.core.dom.ASTNode;

/**
 * JDT Element
 * 
 * @author jabier.martinez
 */
public abstract class JDTElement extends AbstractElement {

	public ASTNode node;
	public String id;
	public String name;

	@Override
	public double similarity(IElement anotherElement) {
		if(anotherElement.getClass().equals(this.getClass())){
			if(((JDTElement)anotherElement).id.equals(id)){
				return 1;
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		return id;
	}
	
	@Override
	public List<String> getWords() {
		return StringUtils.tokenizeString(name);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

}
