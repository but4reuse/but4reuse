package org.but4reuse.feature.constraints.impl;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.feature.constraints.IConstraint;

/**
 * Constraints helper
 * 
 * @author jabier.martinez
 */
public class ConstraintsHelper {

	public static String getText(List<IConstraint> constraints) {
		String text = "";
		for (IConstraint constraint : constraints) {
			text += constraint.getText() + "\n";
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

	public static String getTextWithExplanations(IConstraint constraint) {
		return constraint.getText() + " (" + constraint.getNumberOfReasons() + " reasons) "
				+ constraint.getExplanations();
	}

	/**
	 * Provisional method to perform the casting
	 * 
	 * @param adaptedModel
	 * @return
	 */
	public static List<IConstraint> getCalculatedConstraints(AdaptedModel adaptedModel) {
		List<IConstraint> constraints = new ArrayList<IConstraint>();
		Object o = adaptedModel.getConstraints();
		if (o != null && o instanceof List<?>) {
			List<?> list = (List<?>) o;
			for (Object e : list) {
				if (e instanceof IConstraint) {
					constraints.add((IConstraint) e);
				}
			}
		}
		return constraints;
	}
}
