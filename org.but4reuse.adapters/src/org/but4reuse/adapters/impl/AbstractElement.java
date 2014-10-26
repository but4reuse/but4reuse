package org.but4reuse.adapters.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.preferences.PreferencesHelper;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

public abstract class AbstractElement implements IElement {

	private static final String MAIN_DEPENDENCY_ID = "depends on";
	/**
	 * Abstract IElement
	 * 
	 * @author jabier.martinez
	 */
	private Map<String, List<Object>> dependencies = new HashMap<String, List<Object>>();
	private Map<String, Integer> minDependencies = new HashMap<String, Integer>();
	private Map<String, Integer> maxDependencies = new HashMap<String, Integer>();

	@Override
	/**
	 * HashCode default implementation returns always 1 so it will be handled as a list.
	 * This method is intended to be overridden if a hash method could be provided to improve performance.
	 */
	public int hashCode() {
		return 1;
	}

	@Override
	public String toString() {
		return getText();
	}

	@Override
	public Map<String, List<Object>> getDependencies() {
		return dependencies;
	}

	@Override
	public int getMaxDependencies(String dependencyID) {
		return maxDependencies.get(dependencyID);
	}

	@Override
	public int getMinDependencies(String dependencyID) {
		return minDependencies.get(dependencyID);
	}

	public void setMaximumDependencies(String dependencyID, int number) {
		maxDependencies.put(dependencyID, number);
	}

	public void setMinimumDependencies(String dependencyID, int number) {
		minDependencies.put(dependencyID, number);
	}

	public void addDependency(String dependencyID, Object dependency) {
		List<Object> o = dependencies.get(dependencyID);
		if (o == null) {
			o = new ArrayList<Object>();
		}
		if (!o.contains(dependency)) {
			o.add(dependency);
		}
		dependencies.put(dependencyID, o);
	}

	public void addDependency(String dependency) {
		addDependency(MAIN_DEPENDENCY_ID, dependency);
	}

	public void addDependencies(List<String> dependencies) {
		for (String dependency : dependencies) {
			addDependency(MAIN_DEPENDENCY_ID, dependency);
		}
	}

	

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IElement) {
			// get threshold
			double automaticThreshold = PreferencesHelper.getAutomaticEqualThreshold();
			double similarity = similarity((IElement) obj);
			if (similarity >= automaticThreshold) {
				// The similarity was greater than the equal threshold
				return true;
			}
			// check if we should ask the user
			if (!PreferencesHelper.isManualEqualActivated()) {
				// no? ok, so it is not equal
				return false;
			}

			// check if we should really ask
			double manualThreshold = PreferencesHelper.getManualEqualThreshold();
			if (similarity < manualThreshold) {
				return false;
			}

			// ok, let's ask the user
			boolean userDecision = manualEqual(obj);
			return userDecision;
		}
		return super.equals(obj);
	}

	/**
	 * Manual equal. Override to provide tailored comparison user interfaces for your elements
	 * 
	 * @return whether the element is equal to another
	 */
	public boolean manualEqual(Object obj) {
		ElementTextManualComparison manualComparison = new ElementTextManualComparison(this.getText(),((IElement)obj).getText());
		Display.getDefault().syncExec(manualComparison);
		int buttonIndex = manualComparison.getResult();
		if (buttonIndex == 0) {
			return true;
		} else if (buttonIndex ==1){
			return false;
		} else {
			PreferencesHelper.setManualEqual(false);
			return false;
		}
	}

	public interface IManualComparison extends Runnable {
		public int getResult();
	};

	public class ElementTextManualComparison implements IManualComparison {
		String elementText1;
		String elementText2;
		int result;

		public ElementTextManualComparison(String elementText1, String elementText2) {
			this.elementText1 = elementText1;
			this.elementText2 = elementText2;
		}

		@Override
		public void run() {
			// TODO implement "Always" and "Never" buttons
			MessageDialog dialog = new MessageDialog(null, "Manual decision for equal", null, elementText1 + "\n\n is equal to \n\n" + elementText2,
					MessageDialog.QUESTION, new String[] { "Yes", "No" , "Deactivate manual equal" }, 0);
			result = dialog.open();
		}

		@Override
		public int getResult() {
			return result;
		}

	}

}
