package org.but4reuse.blockcreation.helper;

import java.util.List;

import org.but4reuse.blockcreation.IBlockCreationAlgorithm;
import org.but4reuse.blockcreation.activator.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Intialize Block creation preferences by selecting only the default algorithm
 * (intersections-based)
 * 
 * @author jabier.martinez
 * 
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		List<IBlockCreationAlgorithm> algos = BlockCreationHelper.getAllBlockCreationAlgorithms();
		for (IBlockCreationAlgorithm algo : algos) {
			String algoName = BlockCreationHelper.getAlgorithmName(algo);
			boolean isTheDefault = algoName.startsWith("Interdependent elements");
			store.setDefault(BlockCreationHelper.getAlgorithmName(algo), isTheDefault);
		}
	}

}
