package org.but4reuse.visualisation.graphs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.IElement;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.but4reuse.visualisation.IVisualisation;
import org.but4reuse.visualisation.graphs.utils.GraphUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IProgressMonitor;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

/**
 * Elements Graph visualisation
 * 
 * @author jabier.martinez
 */
public class ElementsGraphVisualisation implements IVisualisation {

	@Override
	public void prepare(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor) {
		// Create the graphs
		Graph graph = createGraph(adaptedModel, monitor);

		// Save them
		monitor.subTask("Saving the graph visualisations");

		// TODO improve checks!
		// Here we try to find the folder to save it
		IContainer output = AdaptedModelManager.getDefaultOutput();
		File outputFile = WorkbenchUtils.getFileFromIResource(output);
		String name = AdaptedModelHelper.getName(adaptedModel);
		if (name == null) {
			name = "default";
		}

		// create folder
		File graphsFolder = new File(outputFile, "graphVisualisations");
		graphsFolder.mkdir();

		// Save
		File file = new File(graphsFolder, name + getNameAppendix());
		GraphUtils.saveGraph(graph, file);

		// Refresh
		WorkbenchUtils.refreshIResource(output);
	}

	/**
	 * Create Elements Graph
	 * 
	 * @param adaptedModel
	 * @param monitor
	 * @return the graph
	 */
	public static Graph createElementsGraph(AdaptedModel adaptedModel, IProgressMonitor monitor) {
		monitor.subTask("Creating the Elements graph visualisation");
		// Create graph
		Map<BlockElement, Integer> idMap = new HashMap<BlockElement, Integer>();
		Map<IElement, List<ElementWrapper>> ieews = new HashMap<IElement, List<ElementWrapper>>();

		// Add block elements
		Graph graph = new TinkerGraph();
		Integer id = 0;
		for (Block block : adaptedModel.getOwnedBlocks()) {
			for (BlockElement blockElement : block.getOwnedBlockElements()) {
				Vertex v = graph.addVertex(id);
				v.setProperty("block", block.getName());
				List<Artefact> artefacts = AdaptedModelHelper.getArtefactsContainingBlockElement(blockElement);
				for (AdaptedArtefact aa : adaptedModel.getOwnedAdaptedArtefacts()) {
					if (artefacts.contains(aa.getArtefact())) {
						v.setProperty(aa.getArtefact().getName(), "yes");
					} else {
						v.setProperty(aa.getArtefact().getName(), "no");
					}
				}
				// Assumption, all block elements are of the same type
				ElementWrapper ew = blockElement.getElementWrappers().get(0);
				IElement element = (IElement) ew.getElement();
				v.setProperty("elementType", element.getClass().getSimpleName());
				List<ElementWrapper> list = ieews.get(element);
				if (list == null) {
					list = new ArrayList<ElementWrapper>();
				}
				if (!list.contains(ew)) {
					list.add(ew);
				}
				ieews.put(element, list);
				v.setProperty("Label", element.getText());

				idMap.put(blockElement, id);
				id++;
			}
		}

		// Add edges
		// For each Element
		for (IElement elementKey : ieews.keySet()) {
			List<BlockElement> sourcebes = new ArrayList<BlockElement>();
			// List<BlockElement> targetbes = new ArrayList<BlockElement>();
			Map<String, List<BlockElement>> depKeyTargetbes = new HashMap<String, List<BlockElement>>();

			// Create the list of source block elements containing Element
			List<ElementWrapper> sources = ieews.get(elementKey);
			for (ElementWrapper source : sources) {
				List<BlockElement> blockes = source.getBlockElements();
				for (BlockElement blocke : blockes) {
					if (!sourcebes.contains(blocke)) {
						sourcebes.add(blocke);
					}
				}
			}

			// Create the list of target block elements
			Map<String, List<IDependencyObject>> dependencies = elementKey.getDependencies();
			for (String dependencyKey : dependencies.keySet()) {
				List<IDependencyObject> idos = dependencies.get(dependencyKey);
				for (IDependencyObject ido : idos) {
					if (ido instanceof IElement) {
						IElement element = (IElement) ido;
						List<ElementWrapper> targets = ieews.get(element);
						if (targets == null) {
							System.err.println("GraphVisualisation error: target element was not found");
							continue;
						}
						for (ElementWrapper target : targets) {
							List<BlockElement> blockElements = target.getBlockElements();
							for (BlockElement blocke : blockElements) {
								List<BlockElement> targetbes = depKeyTargetbes.get(dependencyKey);
								if (targetbes == null) {
									targetbes = new ArrayList<BlockElement>();
								}
								if (!targetbes.contains(blocke)) {
									targetbes.add(blocke);
									depKeyTargetbes.put(dependencyKey, targetbes);
								}
							}
						}
					}
				}
			}

			// Create the edge
			for (BlockElement sourcebe : sourcebes) {
				Integer id1 = idMap.get(sourcebe);
				if (id1 == null) {
					System.err.println("GraphVisualisation error: edge source element was not found");
				} else {
					Vertex one = graph.getVertex(id1);
					for (String depKey : depKeyTargetbes.keySet()) {
						List<BlockElement> targetbes = depKeyTargetbes.get(depKey);
						for (BlockElement targetbe : targetbes) {
							Integer id2 = idMap.get(targetbe);
							if (id2 == null) {
								System.err.println("GraphVisualisation error: edge target element was not found");
							} else {
								Vertex two = graph.getVertex(id2);
								// the id of the edge is the concatenation of
								// id1 id2 and depKey
								// but in case that there is more than one with
								// this id, we concatenate something at the end
								String repeated = "";
								Edge e = graph.getEdge(id1 + "-" + id2 + "-" + depKey);
								if (e != null) {
									repeated = "-r";
								}
								Edge edge = graph.addEdge(id1 + "-" + id2 + "-" + depKey + repeated, one, two, id1
										+ "-" + id2);
								edge.setProperty("dependencyType", depKey);
								edge.setProperty("Label", depKey);
							}
						}
					}
				}
			}
		}
		return graph;
	}

	@Override
	public void show() {
		// Do nothing. Everything is done in the prepare method
	}

	public Graph createGraph(AdaptedModel adaptedModel, IProgressMonitor monitor) {
		return createElementsGraph(adaptedModel, monitor);
	}

	public String getNameAppendix() {
		return "_elements.graphml";
	}

}
