package org.but4reuse.feature.constraints.impl;

import java.util.List;

import org.but4reuse.feature.constraints.IConstraint;

/**
 * Constraints helper
 * @author jabier.martinez
 */
public class ConstraintsHelper {
	
	public static String getText(List<IConstraint> constraints) {
		String text = "";
		for (IConstraint constraint : constraints) {
			text += getText(constraint) + "\n";
		}
		if (text.length() != 0) {
			// remove last end of line
			text = text.substring(0, text.length() - 1);
		}
		return text;
	}
	
	public static String getTextWithExplanations(List<IConstraint> constraints) {
		String text = "";
		for (IConstraint constraint : constraints) {
			text += getTextWithExplanations(constraint) + "\n\n";
		}
		if (text.length() != 0) {
			// remove last end of line
			text = text.substring(0, text.length() - 1);
		}
		return text;
	}

	public static String getText(IConstraint constraint) {
		return constraint.getBlock1().getName() + " " + constraint.getType() + " " + constraint.getBlock2().getName();
	}
	
	public static String getTextWithExplanations(IConstraint constraint) {
		return getText(constraint) + " (" + constraint.getNumberOfReasons() + " reasons) " + constraint.getExplanations();
	}
}
