package org.but4reuse.visualisation.graphs;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.IElement;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.but4reuse.visualisation.IVisualisation;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;

public class GraphVisualisation implements IVisualisation {

	private AdaptedModel adaptedModel;

	@Override
	public void prepare(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor) {
		this.adaptedModel = adaptedModel;
	}

	@Override
	public void show() {

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
				List<Artefact> artefacts = getArtefactList(blockElement);
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
				v.setProperty("elementType", element.getClass().getName());
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
				Vertex one = graph.getVertex(id1);
				for (String depKey : depKeyTargetbes.keySet()) {
					List<BlockElement> targetbes = depKeyTargetbes.get(depKey);
					for (BlockElement targetbe : targetbes) {
						Integer id2 = idMap.get(targetbe);
						Vertex two = graph.getVertex(id2);
						Edge edge = graph.addEdge(id1 + "-" + id2, one, two, id1 + "-" + id2);
						edge.setProperty("dependencyType", depKey);
					}
				}
			}
		}

		// Save
		GraphMLWriter writer = new GraphMLWriter(graph);
		writer.setNormalize(true);
		// TODO improve checks!
		URI uri = adaptedModel.getOwnedAdaptedArtefacts().get(0).getArtefact().eResource().getURI();
		java.net.URI uri2 = null;
		try {
			uri2 = new java.net.URI(uri.toString());
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		IResource res = WorkbenchUtils.getIResourceFromURI(uri2);
		File artefactModelFile = WorkbenchUtils.getFileFromIResource(res);
		File file = new File(artefactModelFile.getAbsolutePath() + "_visualisation.graphml");
		try {
			writer.outputGraph(file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		WorkbenchUtils.refreshIResource(res.getParent());
	}

	// TODO move to adaptedmodelhelper or similar
	public List<Artefact> getArtefactList(BlockElement blockElement) {
		List<Artefact> artefacts = new ArrayList<Artefact>();
		for (ElementWrapper ew : blockElement.getElementWrappers()) {
			AdaptedArtefact aa = (AdaptedArtefact) ew.eContainer();
			if (!artefacts.contains(aa.getArtefact())) {
				artefacts.add(aa.getArtefact());
			}
		}
		return artefacts;
	}

}
