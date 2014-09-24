package org.but4reuse.adapters.textlines;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.textlines.utils.LevenshteinDistanceStrategy;

/**
 * Line element
 * @author jabier.martinez
 */
public class LineElement extends AbstractElement {

	public String line;
	
	// Constructor
	public LineElement(String line){
		this.line=line;
	}
	
	@Override
	public double similarity(IElement anotherElement) {
		if(anotherElement instanceof LineElement){
			// similarity metric for two strings
			return LevenshteinDistanceStrategy.score(line,((LineElement)anotherElement).line);
		}
		return 0;
	}

	@Override
	public String getText() {
		return line;
	}

}
