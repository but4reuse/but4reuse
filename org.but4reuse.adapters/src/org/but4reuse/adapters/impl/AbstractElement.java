package org.but4reuse.adapters.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.preferences.PreferencesHelper;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * Abstract Element
 * @author jabier.martinez
 */
public abstract class AbstractElement implements IElement, IDependencyObject {

	public static final String MAIN_DEPENDENCY_ID = "depends on";
	/**
	 * Abstract IElement
	 * 
	 * @author jabier.martinez
	 */
	private Map<String, List<IDependencyObject>> dependencies = new HashMap<String, List<IDependencyObject>>();
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
	public Map<String, List<IDependencyObject>> getDependencies() {
		return dependencies;
	}

	@Override
	public int getMaxDependencies(String dependencyID) {
		if(maxDependencies.get(dependencyID)==null){
			return Integer.MAX_VALUE;
		}
		return maxDependencies.get(dependencyID);
	}

	@Override
	public int getMinDependencies(String dependencyID) {
		if(minDependencies.get(dependencyID)==null){
			return Integer.MIN_VALUE;
		}
		return minDependencies.get(dependencyID);
	}

	public void setMaximumDependencies(String dependencyID, int number) {
		maxDependencies.put(dependencyID, number);
	}

	public void setMinimumDependencies(String dependencyID, int number) {
		minDependencies.put(dependencyID, number);
	}

	public void addDependency(String dependencyID, IDependencyObject dependency) {
		List<IDependencyObject> o = dependencies.get(dependencyID);
		if (o == null) {
			o = new ArrayList<IDependencyObject>();
		}
		if (!o.contains(dependency)) {
			o.add(dependency);
		}
		dependencies.put(dependencyID, o);
	}

	public void addDependency(IDependencyObject dependency) {
		addDependency(MAIN_DEPENDENCY_ID, dependency);
	}

	public void addDependencies(List<IDependencyObject> dependencies) {
		for (IDependencyObject dependency : dependencies) {
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
			if (!PreferencesHelper.isManualEqualActivated() || PreferencesHelper.isDeactivateManualEqualOnlyForThisTime()) {
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
			PreferencesHelper.setDeactivateManualEqualOnlyForThisTime(true);
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
			// Default is No
			MessageDialog dialog = new MessageDialog(null, "Manual decision for equal", null, elementText1 + "\n\n is equal to \n\n" + elementText2,
					MessageDialog.QUESTION, new String[] { "Yes", "No" , "Deactivate manual equal" }, 1);
			result = dialog.open();
		}

		@Override
		public int getResult() {
			return result;
		}

	}

}
