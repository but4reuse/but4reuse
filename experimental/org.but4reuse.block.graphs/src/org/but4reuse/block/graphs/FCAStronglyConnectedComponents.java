package org.but4reuse.block.graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModelFactory;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adapters.IElement;
import org.but4reuse.block.identification.IBlockIdentification;
import org.but4reuse.block.identification.impl.IntersectionsBlockIdentification;
import org.but4reuse.graphs.utils.GraphUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.GabowStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.graph.DefaultEdge;

/**
 * FCA + strongly connected components
 * 
 * @author jabier.martinez
 * 
 */
public class FCAStronglyConnectedComponents implements IBlockIdentification {

	@Override
	public List<Block> identifyBlocks(List<AdaptedArtefact> artefacts, IProgressMonitor monitor) {
		// Get the fca blocks
		IntersectionsBlockIdentification blockIdentification = new IntersectionsBlockIdentification();
		List<Block> fcaBlocks = blockIdentification.identifyBlocks(artefacts, monitor);
		// Structural splitting
		List<Block> allBlocks = new ArrayList<Block>();
		int i = 0;
		for (Block b : fcaBlocks) {
			i++;
			DirectedGraph<IElement, DefaultEdge> graph = GraphUtils.createDirectedGraph(b);
			StrongConnectivityAlgorithm<IElement, DefaultEdge> algo = new GabowStrongConnectivityInspector<IElement, DefaultEdge>(
					graph);
			List<Set<IElement>> connectedSets = algo.stronglyConnectedSets();
			for (Set<IElement> connectedSet : connectedSets) {
				Block newB = AdaptedModelFactory.eINSTANCE.createBlock();
				for (IElement e : connectedSet) {
					BlockElement be = AdaptedModelFactory.eINSTANCE.createBlockElement();
					ElementWrapper ew = AdaptedModelFactory.eINSTANCE.createElementWrapper();
					ew.setElement(e);
					be.getElementWrappers().add(ew);
					newB.getOwnedBlockElements().add(be);
				}
				allBlocks.add(newB);
			}
			monitor.subTask("Block identification. Structural splitting with strongly connected components " + i + "/"
					+ fcaBlocks.size());
		}
		return allBlocks;
	}

}
