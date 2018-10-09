package org.but4reuse.adapters.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.preferences.PreferencesHelper;
import org.but4reuse.utils.strings.StringUtils;
import org.eclipse.swt.widgets.Display;

/**
 * Abstract Element
 * 
 * @author jabier.martinez
 */
public abstract class AbstractElement implements IElement, IDependencyObject {

	public static final String MAIN_DEPENDENCY_ID = "depends on";
	/**
	 * Abstract IElement
	 * 
	 * @author jabier.martinez
	 */
	private Map<String, List<IDependencyObject>> dependencies = new LinkedHashMap<String, List<IDependencyObject>>();
	private Map<String, List<IDependencyObject>> dependants = new LinkedHashMap<String, List<IDependencyObject>>();
	private Map<String, Integer> minDependencies = new HashMap<String, Integer>();
	private Map<String, Integer> maxDependencies = new HashMap<String, Integer>();

	@Override
	/**
	 * HashCode default implementation returns always 1 so it will be handled as
	 * a list. This method is intended to be overridden if a hash method could
	 * be provided to improve performance.
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
	public Map<String, List<IDependencyObject>> getDependants() {
		return dependants;
	}

	@Override
	public int getMaxDependencies(String dependencyID) {
		if (maxDependencies.get(dependencyID) == null) {
			return Integer.MAX_VALUE;
		}
		return maxDependencies.get(dependencyID);
	}

	@Override
	public int getMinDependencies(String dependencyID) {
		if (minDependencies.get(dependencyID) == null) {
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

	/**
	 * Add a dependency. We do not check if it was already added because it is
	 * expensive, try to avoid duplicates while adding dependencies
	 * 
	 * @param dependencyID
	 * @param dependency
	 */
	public void addDependency(String dependencyID, IDependencyObject dependency) {
		// do not add anything if null
		if(dependency == null){
			return;
		}
		// Add dependencies
		List<IDependencyObject> dos = dependencies.get(dependencyID);
		if (dos == null) {
			dos = new ArrayList<IDependencyObject>();
		}
		dos.add(dependency);
		dependencies.put(dependencyID, dos);
		// Add also dependants
		if (dependency instanceof AbstractElement) {
			AbstractElement ae = (AbstractElement) dependency;
			List<IDependencyObject> dependants = ae.getDependants().get(dependencyID);
			if (dependants == null) {
				dependants = new ArrayList<IDependencyObject>();
			}
			dependants.add(this);
			ae.getDependants().put(dependencyID, dependants);
		}
	}

	/**
	 * Add a dependency without specifying a dependency type id.
	 * 
	 * @param dependency
	 */
	public void addDependency(IDependencyObject dependency) {
		addDependency(MAIN_DEPENDENCY_ID, dependency);
	}

	/**
	 * Add a list of dependencies without specifying a dependency type id.
	 * 
	 * @param dependencies
	 */
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
			if (!PreferencesHelper.isManualEqualActivated()
					|| PreferencesHelper.isDeactivateManualEqualOnlyForThisTime()) {
				// no? ok, so it is not equal
				return false;
			}

			// check if we should really ask
			double manualThreshold = PreferencesHelper.getManualEqualThreshold();
			if (similarity < manualThreshold) {
				return false;
			}

			// check if it is on the cache
			Boolean cache = ManualEqualCache.check(this, (IElement) obj);
			if (cache != null) {
				return cache;
			}

			// ok, let's ask the user
			boolean userDecision = manualEqual(similarity, (IElement) obj);
			if (userDecision) {
				ManualEqualCache.add(this, (IElement) obj, true);
			} else {
				ManualEqualCache.add(this, (IElement) obj, false);
			}
			return userDecision;
		}
		return super.equals(obj);
	}

	/**
	 * Manual equal.
	 * 
	 * @return whether the element is equal to another
	 */
	public boolean manualEqual(double calculatedSimilarity, IElement element) {
		IManualComparison manualComparison = getManualComparison(calculatedSimilarity, element);
		Display.getDefault().syncExec(manualComparison);
		int buttonIndex = manualComparison.getResult();
		if (buttonIndex == 0) {
			return true;
		} else if (buttonIndex == 1) {
			return false;
		} else {
			PreferencesHelper.setDeactivateManualEqualOnlyForThisTime(true);
			return false;
		}
	}

	/**
	 * Get manual comparison. Override to provide tailored comparison user
	 * interfaces for your elements
	 * 
	 * @param calculatedSimilarity
	 * @param anotherElement
	 * @return
	 */
	public IManualComparison getManualComparison(double calculatedSimilarity, IElement anotherElement) {
		return new ElementTextManualComparison(calculatedSimilarity, this.getText(), anotherElement.getText());
	}

	@Override
	public String getDependencyObjectText() {
		return getText();
	}

	/**
	 * Default implementation of get words
	 * 
	 * @return tokenize
	 */
	public List<String> getWords() {
		return StringUtils.tokenizeString(getText());
	}

}
