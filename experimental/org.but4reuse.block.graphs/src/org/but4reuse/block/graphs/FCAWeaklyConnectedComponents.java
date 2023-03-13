package org.but4reuse.block.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModelFactory;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adapters.IElement;
import org.but4reuse.block.identification.IBlockIdentification;
import org.but4reuse.fca.block.identification.FCABlockIdentification;
import org.but4reuse.graphs.utils.GraphUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;

/**
 * FCA + weakly connected components
 * 
 * @author jabier.martinez
 * 
 */
public class FCAWeaklyConnectedComponents implements IBlockIdentification {

	@Override
	public List<Block> identifyBlocks(List<AdaptedArtefact> artefacts, IProgressMonitor monitor) {
		// Get the fca blocks
		FCABlockIdentification blockIdentification = new FCABlockIdentification();
		List<Block> fcaBlocks = blockIdentification.identifyBlocks(artefacts, monitor);

		// Structural splitting
		List<Block> allBlocks = new ArrayList<Block>();
		int i = 0;
		for (Block b : fcaBlocks) {
			i++;
			
			// create a map to reuse the same block elements
			Map<IElement,BlockElement> mapIEBE = new HashMap<IElement, BlockElement>();
			for (BlockElement be : b.getOwnedBlockElements()) {
				mapIEBE.put((IElement) be.getElementWrappers().get(0).getElement(), be);
			}
			
			// create graph
			HashSet<IElement> elements = AdaptedModelHelper.getElementsOfBlockHashSet(b);
			DirectedGraph<IElement, DefaultEdge> graph = GraphUtils.createDirectedGraph(elements);
			List<Set<IElement>> connectedSets = getConnectedSets(graph);
			for (Set<IElement> connectedSet : connectedSets) {
				Block newB = AdaptedModelFactory.eINSTANCE.createBlock();
				for (IElement e : connectedSet) {
					BlockElement be = mapIEBE.get(e);
					newB.getOwnedBlockElements().add(be);
				}
				allBlocks.add(newB);
			}
			monitor.subTask("Block identification. Structural splitting with " + getNameForMonitor() + " " + i + "/"
					+ fcaBlocks.size());
		}
		return allBlocks;
	}
	
	public List<Set<IElement>> getConnectedSets(DirectedGraph<IElement, DefaultEdge> graph){
		ConnectivityInspector<IElement, DefaultEdge> algo = new ConnectivityInspector<IElement, DefaultEdge>(graph);
		List<Set<IElement>> connectedSets = algo.connectedSets();
		return connectedSets;
	}
	
	public String getNameForMonitor() {
		return "weakly connected components";
	}

}
