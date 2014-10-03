package org.but4reuse.adaptedmodel.blockcreation;

import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.Block;

public interface IBlockCreationAlgorithm {
	public List<Block> createBlocks(List<AdaptedArtefact> artefacts);
}
