package org.but4reuse.adapters.jacoco;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

/**
 * Covered line element
 * 
 * @author jabier.martinez
 */
public class CoveredLineElement extends AbstractElement {

	public String packageName;
	public String fileName;
	public int lineNumber;

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof CoveredLineElement) {
			CoveredLineElement another = (CoveredLineElement) anotherElement;
			if (another.lineNumber == lineNumber && another.fileName.equals(fileName)
					&& another.packageName.equals(packageName)) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		return packageName + "/" + fileName + " " + lineNumber;
	}
	
	@Override
	public List<String> getWords() {
		List<String> words = new ArrayList<String>();
		words.add(packageName);
		words.add(fileName);
		return words;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getText() == null) ? 0 : getText().hashCode());
		return result;
	}

}
