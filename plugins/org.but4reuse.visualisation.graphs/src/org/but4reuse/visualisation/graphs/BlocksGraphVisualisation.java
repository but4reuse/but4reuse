package org.but4reuse.visualisation.graphs;

import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.feature.constraints.BasicExcludesConstraint;
import org.but4reuse.feature.constraints.BasicRequiresConstraint;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.impl.ConstraintsHelper;
import org.eclipse.core.runtime.IProgressMonitor;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

/**
 * Blocks Graph visualisation
 * 
 * @author jabier.martinez
 */
public class BlocksGraphVisualisation extends ElementsGraphVisualisation {

	@Override
	public Graph createGraph(AdaptedModel adaptedModel, IProgressMonitor monitor) {
		return createBlocksGraph(adaptedModel, monitor);
	}

	@Override
	public String getNameAppendix() {
		return "_blocks.graphml";
	}

	/**
	 * Create Blocks Graph
	 * 
	 * @param adaptedModel
	 * @param monitor
	 * @return the graph
	 */
	public static Graph createBlocksGraph(AdaptedModel adaptedModel, IProgressMonitor monitor) {
		monitor.subTask("Creating the Blocks graph visualisation");
		Graph graph = new TinkerGraph();
		// Create vertices for each block in the list with the id of its
		// position
		List<Block> blocks = adaptedModel.getOwnedBlocks();
		for (int id = 0; id < blocks.size(); id++) {
			Vertex v = graph.addVertex(id);
			v.setProperty("Label", blocks.get(id).getName());
			v.setProperty("NumberOfBlockElements", blocks.get(id).getOwnedBlockElements().size());
		}

		// Create the edges with the constraints. For the mutually excludes add
		// edges in both directions
		List<IConstraint> constraints = ConstraintsHelper.getCalculatedConstraints(adaptedModel);
		for (IConstraint constraint : constraints) {
			if (constraint instanceof BasicRequiresConstraint) {
				BasicRequiresConstraint c = (BasicRequiresConstraint) constraint;
				int id1 = blocks.indexOf(c.getBlock1());
				int id2 = blocks.indexOf(c.getBlock2());
				Vertex one = graph.getVertex(id1);
				Vertex two = graph.getVertex(id2);
				Edge edge = graph.addEdge(id1 + "-" + id2, one, two, id1 + "-" + id2);
				edge.setProperty("Label", "requires");
				edge.setProperty("Explanations", ConstraintsHelper.getTextWithExplanations(constraint));
				edge.setProperty("NumberOfReasons", constraint.getNumberOfReasons());
			} else if (constraint instanceof BasicExcludesConstraint) {
				BasicExcludesConstraint c = (BasicExcludesConstraint) constraint;
				int id1 = blocks.indexOf(c.getBlock1());
				int id2 = blocks.indexOf(c.getBlock2());
				Vertex one = graph.getVertex(id1);
				Vertex two = graph.getVertex(id2);
				Edge edge = graph.addEdge(id1 + "-" + id2, one, two, id1 + "-" + id2);
				edge.setProperty("Label", "excludes");
				edge.setProperty("Explanations", ConstraintsHelper.getTextWithExplanations(constraint));
				edge.setProperty("NumberOfReasons", constraint.getNumberOfReasons());
				// Add also the opposite in the case of mutually excludes
				Edge edge2 = graph.addEdge(id2 + "-" + id1, two, one, id2 + "-" + id1);
				edge2.setProperty("Label", "excludes");
				edge2.setProperty("Explanations", ConstraintsHelper.getTextWithExplanations(constraint));
				edge2.setProperty("NumberOfReasons", constraint.getNumberOfReasons());
			}
		}
		return graph;
	}

	@Override
	public void show() {
		// Do nothing. Everything is done in the prepare method
	}

}
