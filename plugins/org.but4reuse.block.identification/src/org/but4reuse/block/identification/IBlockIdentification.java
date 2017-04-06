package org.but4reuse.block.identification;

import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.Block;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Block identification
 * 
 * @author jabier.martinez
 */
public interface IBlockIdentification {

	/**
	 * An algorithm that analyze the artefacts to identify distinguishable
	 * blocks
	 * 
	 * @param artefacts
	 * @param monitor
	 * @return list of identified blocks
	 */
	public List<Block> identifyBlocks(List<AdaptedArtefact> artefacts, IProgressMonitor monitor);
}
