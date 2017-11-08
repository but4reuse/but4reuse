package org.but4reuse.feature.constraints;

import java.util.ArrayList;
import java.util.List;

public class Constraint extends GroupConstraintPart implements IConstraint {

	List<String> explanations = new ArrayList<String>();
	int numberOfReasons = 1;

	public List<String> getExplanations() {
		return explanations;
	}

	public void setExplanations(List<String> explanations) {
		this.explanations = explanations;
	}
	
	public void addExplanation(String explanation){
		explanations.add(explanation);
	}

	public int getNumberOfReasons() {
		return numberOfReasons;
	}

	public void setNumberOfReasons(int numberOfReasons) {
		this.numberOfReasons = numberOfReasons;
	}

	@Override
	// same as GroupConstraintPart but without parenthesis
	public String getText() {
		StringBuffer string = new StringBuffer();
		for (IConstraintPart part : getConstraintParts()) {
			string.append(part.getText());
		}
		return string.toString();
	}

}
