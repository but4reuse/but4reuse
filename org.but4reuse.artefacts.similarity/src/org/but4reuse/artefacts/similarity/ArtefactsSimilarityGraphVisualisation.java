package org.but4reuse.artefacts.similarity;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.IElement;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.but4reuse.visualisation.IVisualisation;
import org.but4reuse.visualisation.graphs.utils.GraphUtils;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;

/**
 * ArtefactsSimilarityDistanceVisualisation
 * 
 * TODO use other similarity metrics
 * 
 * @author jabier.martinez
 * 
 */
public class ArtefactsSimilarityGraphVisualisation implements IVisualisation {

	@Override
	public void prepare(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor) {
		monitor.subTask("Artefacts similarity graph visualisation");
	}

	@Override
	public void show() {
		Graph graph = new TinkerGraph();
		Graph graph2 = new TinkerGraph();
		AdaptedModel adaptedModel = AdaptedModelManager.getAdaptedModel();
		try {
			List<AdaptedArtefact> adaptedArtefacts = adaptedModel.getOwnedAdaptedArtefacts();
			List<IElement> globalEquals = null;

			for (AdaptedArtefact aa1 : adaptedArtefacts) {
				for (AdaptedArtefact aa2 : adaptedArtefacts) {
					// Plus 1 just to start the graph ids with 1 and not with 0
					int id1 = adaptedArtefacts.indexOf(aa1) + 1;
					int id2 = adaptedArtefacts.indexOf(aa2) + 1;
					Vertex v1 = graph.getVertex(id1);
					if (v1 == null) {
						v1 = graph.addVertex(id1);
						v1.setProperty("Label", aa1.getArtefact().getName());
						v1.setProperty("Elements", aa1.getOwnedElementWrappers().size());
					}
					Vertex v2 = graph.getVertex(id2);
					if (v2 == null) {
						v2 = graph.addVertex(id2);
						v2.setProperty("Label", aa2.getArtefact().getName());
						v2.setProperty("Elements", aa2.getOwnedElementWrappers().size());
					}
					// No repetition
					if (id2 > id1) {
						List<IElement> a1 = AdaptedModelHelper.getElementsOfAdaptedArtefact(aa1);
						List<IElement> a2 = AdaptedModelHelper.getElementsOfAdaptedArtefact(aa2);

						// changes needed to go from a1 to a2
						List<IElement> equals = new ArrayList<IElement>();
						List<IElement> deletions = new ArrayList<IElement>();
						List<IElement> additions = new ArrayList<IElement>();

						for (IElement e1 : a1) {
							if (a2.contains(e1)) {
								equals.add(e1);
							} else {
								deletions.add(e1);
							}
						}

						for (IElement e2 : a2) {
							if (!a1.contains(e2)) {
								additions.add(e2);
							}
						}

						// Calculate globalEquals
						if (globalEquals == null) {
							globalEquals = new ArrayList<IElement>();
							globalEquals.addAll(equals);
						} else {
							List<IElement> toBeDeleted = new ArrayList<IElement>();
							for (IElement e : globalEquals) {
								if (!equals.contains(e)) {
									toBeDeleted.add(e);
								}
							}
							globalEquals.removeAll(toBeDeleted);
						}

						// Similarity in this case is the cardinality of the
						// Union of the two artefacts minus the intersection,
						// divided by the number of elements of both artefacts
						int maxLength = a1.size() + a2.size();
						double similarity = 1 - ((deletions.size() + additions.size()) / (double) maxLength);
						if (similarity > 0) {
							Edge edge = graph.addEdge(id1 + "-" + id2, v1, v2, id1 + "-" + id2);
							edge.setProperty("Weight", similarity);
							edge.setProperty("ElementsSpecificSource", deletions.size());
							edge.setProperty("ElementsSpecificTarget", additions.size());
							edge.setProperty("ElementsEqual", equals.size());
						}
					}
				}
			}

			// Add the similarity ignoring the global common elements
			// TODO implement it in a better way
			// Clone the graph
			graph2 = GraphUtils.cloneGraph(graph);
			List<Edge> toBeDeleted = new ArrayList<Edge>();
			for (AdaptedArtefact aa1 : adaptedArtefacts) {
				for (AdaptedArtefact aa2 : adaptedArtefacts) {
					int id1 = adaptedArtefacts.indexOf(aa1) + 1;
					int id2 = adaptedArtefacts.indexOf(aa2) + 1;
					if (id2 > id1) {
						Edge edge = graph2.getEdge(id1 + "-" + id2);
						int maxLength = (Integer) graph2.getVertex(id1).getProperty("Elements")
								+ (Integer) graph2.getVertex(id2).getProperty("Elements") - (2 * globalEquals.size());
						double similarity = 1 - (((Integer) edge.getProperty("ElementsSpecificSource") + (Integer) edge
								.getProperty("ElementsSpecificTarget")) / (double) maxLength);
						if (similarity > 0) {
							edge.setProperty("Weight", similarity);
						} else {
							toBeDeleted.add(edge);
						}
					}
				}
			}
			// remove similarity 0 edges
			for (Edge edge : toBeDeleted) {
				graph2.removeEdge(edge);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO improve checks!
		// Here we try to find the folder to save it
		URI uri = adaptedModel.getOwnedAdaptedArtefacts().get(0).getArtefact().eResource().getURI();
		java.net.URI uri2 = null;
		try {
			uri2 = new java.net.URI(uri.toString());
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		IResource res = WorkbenchUtils.getIResourceFromURI(uri2);
		File artefactModelFile = WorkbenchUtils.getFileFromIResource(res);

		// create folder
		File graphsFolder = new File(artefactModelFile.getParentFile(), "graphVisualisations");
		graphsFolder.mkdir();

		File file = new File(graphsFolder, artefactModelFile.getName() + "_artefacts.graphml");
		try {
			GraphMLWriter writer = new GraphMLWriter(graph);
			writer.setNormalize(true);
			writer.outputGraph(file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		File file2 = new File(graphsFolder, artefactModelFile.getName() + "_artefactsSimilarityIgnoringCommon.graphml");
		try {
			GraphMLWriter writer = new GraphMLWriter(graph2);
			writer.setNormalize(true);
			writer.outputGraph(file2.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Refresh
		WorkbenchUtils.refreshIResource(res.getParent());
	}

}
