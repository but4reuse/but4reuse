package org.but4reuse.adaptedmodel.blockcreation;

import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.Block;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IBlockCreationAlgorithm {
	public List<Block> createBlocks(List<AdaptedArtefact> artefacts, IProgressMonitor monitor);
}
