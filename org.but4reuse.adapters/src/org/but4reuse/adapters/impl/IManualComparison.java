package org.but4reuse.adapters.impl;

/**
 * IManualComparison. Use 0 for yes, 1 for no, 2 for deactivate manual
 * 
 * @author jabier.martinez
 * 
 */
public interface IManualComparison extends Runnable {
	public int getResult();
};
