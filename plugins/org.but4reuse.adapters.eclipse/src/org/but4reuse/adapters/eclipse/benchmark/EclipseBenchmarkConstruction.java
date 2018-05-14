package org.but4reuse.adapters.eclipse.benchmark;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.eclipse.PluginElement;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.featurelist.FeatureListFactory;
import org.but4reuse.utils.emf.EMFUtils;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.visualisation.graphs.utils.GraphUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

/**
 * Eclipse Benchmark construction
 * 
 * @author jabier.martinez
 */
public class EclipseBenchmarkConstruction {

	/**
	 * Create benchmark ground-truth files
	 * @param actual features
	 * @param artefactModel
	 * @param container
	 * @param monitor
	 * @return feature list
	 * @throws Exception
	 */
	public static FeatureList createBenchmark(List<ActualFeature> features, ArtefactModel artefactModel, IContainer container,
			IProgressMonitor monitor) throws Exception {

		String name = artefactModel.getName();
		if (name == null || name.isEmpty()) {
			name = "eclipse";
		}

		monitor.subTask("Creating the feature list");
		// Create the feature list
		FeatureList fl = FeatureListFactory.eINSTANCE.createFeatureList();
		fl.setName("eclipse_" + name);
		fl.setArtefactModel(artefactModel);

		for (ActualFeature af : features) {
			Feature f = FeatureListFactory.eINSTANCE.createFeature();
			f.setId(af.getId());
			f.setName(af.getName());
			f.setDescription(af.getDescription());
			f.getImplementedInArtefacts().addAll(af.getArtefacts());
			fl.getOwnedFeatures().add(f);
		}

		// Save feature list
		EMFUtils.saveEObject(new URI(container.getLocationURI() + "/benchmark/eclipse_" + name + ".featurelist"), fl);

		monitor.subTask("Saving plugins of each feature");
		// Save the plugins of each feature
		for (ActualFeature af : features) {
			File fileF = new File(new URI(container.getLocationURI() + "/benchmark/actualFeatures/" + af.getId()
					+ ".txt"));
			if (!fileF.exists()) {
				FileUtils.createFile(fileF);
			}
			StringBuilder sb = new StringBuilder();
			for (String plu : af.getPlugins()) {
				sb.append(plu + "\n");
			}
			// remove last \n
			if (sb.length() > 0) {
				sb.setLength(sb.length() - 1);
			}
			FileUtils.writeFile(fileF, sb.toString());
		}
		return fl;
	}

	
	public static List<ActualFeature> getActualFeatures(ArtefactModel artefactModel, IProgressMonitor monitor)  throws Exception {
		List<ActualFeature> features = new ArrayList<ActualFeature>();
		artefactModel.setAdapters("eclipse4benchmark");
		artefactModel.eResource().save(null);
		for (Artefact a : AdaptersHelper.getActiveArtefacts(artefactModel)) {
			monitor.subTask("Getting actual features of " + a.getArtefactURI());
			// example of uri
			// file:/C:/keplerPackages/eclipse-cpp-kepler-SR1-win32-x86_64/eclipse/
			List<ActualFeature> currentFeatures = FeatureHelper.getFeaturesOfEclipse(a.getArtefactURI());
			for (ActualFeature af : currentFeatures) {
				int index = features.indexOf(af);
				if (index == -1) {
					af.getArtefacts().add(a);
					features.add(af);
				} else {
					ActualFeature p = features.get(index);
					p.getArtefacts().add(a);
				}
			}
			monitor.worked(1);
		}

		// Sort list
		Collections.sort(features);
		return features;
	}
	
	/**
	 * Create features graph
	 * @param features
	 * @param monitor
	 * @return graph
	 */
	public static Graph createActualFeaturesGraph(List<ActualFeature> features, IProgressMonitor monitor) {
		Graph graph = new TinkerGraph();
		// Add nodes
		Integer id = 0;
		Map<ActualFeature, Vertex> fMap = new HashMap<ActualFeature, Vertex>();
		Map<String, Vertex> idMap = new HashMap<String, Vertex>();
		for (ActualFeature f : features) {
			Vertex v = graph.addVertex(id);
			v.setProperty("Label", f.getId());
			v.setProperty("Name", f.getName());
			fMap.put(f, v);
			idMap.put(f.getId(), v);
			id++;
		}
		// Add edges
		for (ActualFeature f : features) {
			Vertex source = fMap.get(f);
			for (String requi : f.getRequiredFeatures()) {
				Vertex target = idMap.get(requi);
				if (source == null || target == null) {
					System.out.println(f.getId() + " requires " + requi + " but it was not found");
				} else {
					Edge e = source.addEdge("required", target);
					e.setProperty("Label", "required");
				}
			}
			for (String requi : f.getIncludedFeatures()) {
				Vertex target = idMap.get(requi);
				Edge e = source.addEdge("included", target);
				e.setProperty("Label", "included");
			}
		}
		return graph;
	}

	
	/**
	 * Create plugins graph
	 * @param artefactModel
	 * @param container
	 * @param monitor
	 * @return the number of unique plugins in all artefacts
	 * @throws URISyntaxException
	 */
	public static Integer createPluginsGraph(ArtefactModel artefactModel, IContainer container, IProgressMonitor monitor)
			throws Exception {
		// create plugin dependencies graph
		EclipseAdapter4Benchmark adapter = new EclipseAdapter4Benchmark();
		Integer pluginIdsCounter = 0;
		Map<String, Vertex> idMap = new HashMap<String, Vertex>();
		HashSet<String> addedEdges = new HashSet<String>();
		Graph pluginsGraph = new TinkerGraph();
		for (Artefact a : artefactModel.getOwnedArtefacts()) {
			List<IElement> elements = adapter.adapt(new URI(a.getArtefactURI()), new NullProgressMonitor());
			// nodes
			for (IElement e : elements) {
				if (monitor.isCanceled()) {
					return null;
				}
				if (e instanceof PluginElement) {
					PluginElement pe = ((PluginElement) e);
					Vertex v = idMap.get(pe.getSymbName());
					if (v == null) {
						v = pluginsGraph.addVertex(pluginIdsCounter);
						v.setProperty("Label", pe.getSymbName());
						if (pe.getName() != null) {
							v.setProperty("Name", pe.getName());
						}
						idMap.put(pe.getSymbName(), v);
						pluginIdsCounter++;
					}
				}
			}
			// edges
			for (IElement e : elements) {
				if (monitor.isCanceled()) {
					return null;
				}
				if (e instanceof PluginElement) {
					PluginElement pe = ((PluginElement) e);
					Vertex source = idMap.get(pe.getSymbName());
					for (String dep : pe.getRequire_Bundles()) {
						if (!addedEdges.contains(pe.getSymbName() + "-" + dep)) {
							addedEdges.add(pe.getSymbName() + "-" + dep);
							Vertex target = idMap.get(dep);
							if (target == null) {
								System.out.println(dep + " not found and it is dependency of " + pe.getSymbName());
							} else {
								source.addEdge("requires", target);
							}
						}
					}
					if (pe.isFragment() && pe.getFragmentHost() != null) {
						if (!addedEdges.contains(pe.getSymbName() + "-" + pe.getFragmentHost())) {
							addedEdges.add(pe.getSymbName() + "-" + pe.getFragmentHost());
							Vertex target = idMap.get(pe.getFragmentHost());
							if (target == null) {
								System.out.println(pe.getFragmentHost() + " not found and it is host of "
										+ pe.getSymbName());
							} else {
								source.addEdge("requires", target);
							}
						}
					}
				}
			}
		}
		
		File fileGraph = new File(new URI(
				container.getLocationURI() + "/benchmark/pluginDependencies.graphml"));
		GraphUtils.saveGraph(pluginsGraph, fileGraph);
		return pluginIdsCounter;
	}
}
