package org.but4reuse.feature.constraints;

import java.util.ArrayList;
import java.util.List;

/**
 * Group constraint part
 * 
 * @author jabier.martinez
 */
public class GroupConstraintPart implements IConstraintPart {

	private List<IConstraintPart> constraintParts = new ArrayList<IConstraintPart>();

	@Override
	public String getText() {
		StringBuffer string = new StringBuffer(" ( ");
		for (IConstraintPart part : constraintParts) {
			string.append(part.getText());
		}
		string.append(" ) ");
		return string.toString();
	}

	public void addConstraintPart(IConstraintPart partToAdd) {
		constraintParts.add(partToAdd);
	}

	public List<IConstraintPart> getConstraintParts() {
		return constraintParts;
	}

	public void setConstraintParts(List<IConstraintPart> constraintParts) {
		this.constraintParts = constraintParts;
	}

}
