package org.but4reuse.block.identification.helper;

import java.util.List;

import org.but4reuse.block.identification.IBlockIdentification;
import org.but4reuse.block.identification.activator.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Intialize Block identification preferences by selecting only the default
 * algorithm (intersections-based)
 * 
 * @author jabier.martinez
 * 
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		List<IBlockIdentification> algos = BlockIdentificationHelper.getAllBlockIdentificationAlgorithms();
		for (IBlockIdentification algo : algos) {
			String algoName = BlockIdentificationHelper.getAlgorithmName(algo);
			boolean isTheDefault = algoName.startsWith("Interdependent elements");
			store.setDefault(BlockIdentificationHelper.getAlgorithmName(algo), isTheDefault);
		}
	}

}
