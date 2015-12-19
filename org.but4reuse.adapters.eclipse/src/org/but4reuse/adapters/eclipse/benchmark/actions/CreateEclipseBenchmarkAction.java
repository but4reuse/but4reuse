package org.but4reuse.adapters.eclipse.benchmark.actions;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.eclipse.PluginElement;
import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;
import org.but4reuse.adapters.eclipse.benchmark.EclipseAdapter4Benchmark;
import org.but4reuse.adapters.eclipse.benchmark.FeatureHelper;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.featurelist.FeatureListFactory;
import org.but4reuse.utils.emf.EMFUtils;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.but4reuse.visualisation.graphs.utils.GraphUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

/**
 * Create Benchmark Action
 * 
 * @author jabier.martinez
 */
public class CreateEclipseBenchmarkAction implements IObjectActionDelegate {

	private ISelection selection;

	@Override
	public void run(IAction action) {
		if (selection instanceof IStructuredSelection) {
			Object art = ((IStructuredSelection) selection).getFirstElement();
			if (art instanceof ArtefactModel) {
				try {
					// Get the artefact model
					ArtefactModel artefactModel = ((ArtefactModel) art);
					artefactModel.setAdapters("eclipse4benchmark");
					artefactModel.eResource().save(null);
					List<ActualFeature> features = new ArrayList<ActualFeature>();
					for (Artefact a : AdaptersHelper.getActiveArtefacts(artefactModel)) {
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
					}

					// Sort list
					Collections.sort(features);

					String name = artefactModel.getName();
					if (name == null || name.isEmpty()) {
						name = "eclipse";
					}

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
					IResource res = EMFUtils.getIResource(artefactModel.eResource());
					IContainer container = res.getParent();
					EMFUtils.saveEObject(new URI(container.getLocationURI() + "/benchmark/eclipse_" + name
							+ ".featurelist"), fl);

					// Save features graph
					Graph graph = createActualFeaturesGraph(features);
					File fileGraph = new File(new URI(container.getLocationURI() + "/benchmark/actualFeatures.graphml"));
					GraphUtils.saveGraph(graph, fileGraph);

					// Save the plugins of each feature
					for (ActualFeature af : features) {
						File fileF = new File(new URI(container.getLocationURI() + "/benchmark/actualFeatures/"
								+ af.getId() + ".txt"));
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

					// create plugin dependencies graph
					EclipseAdapter4Benchmark adapter = new EclipseAdapter4Benchmark();
					Integer id = 0;
					// Map<PluginElement, Vertex> peMap = new
					// HashMap<PluginElement, Vertex>();
					Map<String, Vertex> idMap = new HashMap<String, Vertex>();
					HashSet<String> addedEdges = new HashSet<String>();
					Graph pluginsGraph = new TinkerGraph();
					for (Artefact a : artefactModel.getOwnedArtefacts()) {
						List<IElement> elements = adapter.adapt(new URI(a.getArtefactURI()), new NullProgressMonitor());
						// nodes
						for (IElement e : elements) {
							if (e instanceof PluginElement) {
								PluginElement pe = ((PluginElement) e);
								Vertex v = idMap.get(pe.getSymbName());
								if (v == null) {
									v = pluginsGraph.addVertex(id);
									v.setProperty("Label", pe.getSymbName());
									if (pe.getName() != null) {
										v.setProperty("Name", pe.getName());
									}
									idMap.put(pe.getSymbName(), v);
									id++;
								}
							}
						}
						// edges
						for (IElement e : elements) {
							if (e instanceof PluginElement) {
								PluginElement pe = ((PluginElement) e);
								Vertex source = idMap.get(pe.getSymbName());
								for (String dep : pe.getRequire_Bundles()) {
									if (!addedEdges.contains(pe.getSymbName() + "-" + dep)) {
										// if
										// (pe.getSymbName().contains("org.eclipse.team.cvs"))
										// {
										addedEdges.add(pe.getSymbName() + "-" + dep);
										Vertex target = idMap.get(dep);
										if (target == null) {
											System.out.println(dep + " not found and it is dependency of "
													+ pe.getSymbName());
										} else {
											source.addEdge("requires", target);
										}
										// }
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
					File fileGraph2 = new File(new URI(container.getLocationURI()
							+ "/benchmark/pluginDependencies.graphml"));
					GraphUtils.saveGraph(pluginsGraph, fileGraph2);

					// Preparing the message to display
					HashSet<String> uniqueIds = new HashSet<String>();
					int nonUniquePlugins = 0;
					StringBuffer text = new StringBuffer();
					for (ActualFeature af : features) {
						text.append("Feature: " + af.getName() + " [" + af.getId() + "]\n");
						for (String plu : af.getPlugins()) {
							text.append(plu + "\n");
							if (!uniqueIds.contains(plu)) {
								uniqueIds.add(plu);
							}
							nonUniquePlugins++;
						}
						text.append("\n");
					}

					// Refresh
					WorkbenchUtils.refreshIResource(container);

					// Show message
					ScrollableMessageDialog dialog = new ScrollableMessageDialog(Display.getCurrent().getActiveShell(),
							"Creation of Eclipse Benchmark", "Check the actualFeatures folder that was created. Here it is a summary:",
							artefactModel.getOwnedArtefacts().size() + " packages.\n"
									+ features.size() + " features found.\n" + nonUniquePlugins
									+ " plugins are mapped to features.\n" + uniqueIds.size()
									+ " unique plugins associated to features.\n" + id
									+ " unique plugins in all the Eclipse packages\n\n" +
									text.toString());
					dialog.open();

				} catch (Exception e) {
					MessageDialog
							.openError(
									Display.getCurrent().getActiveShell(),
									"Error",
									"Maybe check in the properties of each artefact that the URI attribute is pointing to an eclipse folder. Example file:/C:/keplerPackages/eclipse-jee-kepler-SR1-win32-x86_64/eclipse/\n"
											+ e.toString());
					e.printStackTrace();
				}
			}
		}
	}

	public static Graph createActualFeaturesGraph(List<ActualFeature> features) {
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

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

}
