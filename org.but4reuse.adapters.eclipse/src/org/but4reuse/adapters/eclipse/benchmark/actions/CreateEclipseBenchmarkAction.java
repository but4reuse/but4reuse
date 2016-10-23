package org.but4reuse.adapters.eclipse.benchmark.actions;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.HashSet;
import java.util.List;

import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;
import org.but4reuse.adapters.eclipse.benchmark.EclipseBenchmarkCreator;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.utils.emf.EMFUtils;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.but4reuse.visualisation.graphs.utils.GraphUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.tinkerpop.blueprints.Graph;

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
				// Get the artefact model
				final ArtefactModel artefactModel = ((ArtefactModel) art);
				IResource res = EMFUtils.getIResource(artefactModel.eResource());
				final IContainer container = res.getParent();

				// Show message
				final ScrollableMessageDialog dialog = new ScrollableMessageDialog(Display.getCurrent()
						.getActiveShell(), "Creation of Eclipse Benchmark",
						"Check the actualFeatures folder that was created. Here it is a summary:", "");

				// Launch Progress dialog
				ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
				try {
					progressDialog.run(true, true, new IRunnableWithProgress() {
						@Override
						public void run(IProgressMonitor monitor) throws InvocationTargetException,
								InterruptedException {
							try {

								// number of artefacts and 2 for the graphs
								monitor.beginTask("Creating the Eclipse Benchmark", artefactModel.getOwnedArtefacts().size() + 2);

								List<ActualFeature> features = EclipseBenchmarkCreator.createBenchmark(artefactModel,
										container, monitor);

								if (!monitor.isCanceled()) {
									monitor.subTask("[Optional, you can Cancel now] Creating graphs. Plugins graph");
									EclipseBenchmarkCreator.createPluginsGraph(artefactModel,
											container, monitor);
								}
								monitor.worked(1);
								
								if (!monitor.isCanceled()) {
									monitor.subTask("[Optional, you can Cancel now] Creating graphs. Features graph");
									// Save features graph
									Graph graph = EclipseBenchmarkCreator.createActualFeaturesGraph(features, monitor);
									File fileGraph = new File(new URI(container.getLocationURI()
											+ "/benchmark/actualFeatures.graphml"));
									GraphUtils.saveGraph(graph, fileGraph);
								}
								monitor.worked(1);

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
									text.append('\n');
								}

								String message = artefactModel.getOwnedArtefacts().size() + " packages.\n"
										+ features.size() + " features found.\n" + nonUniquePlugins
										+ " plugins are mapped to features.\n" + uniqueIds.size()
										+ " unique plugins associated to features.\n\n";

								dialog.scrollableText = message + text.toString();

								monitor.done();
							} catch (Exception e) {
								dialog.text = "Error";
								dialog.scrollableText = "Maybe check in the properties of each artefact that the URI attribute is pointing to an eclipse folder.\nExample file:/C:/keplerPackages/eclipse-jee-kepler-SR1-win32-x86_64/eclipse/\n\n";
								e.printStackTrace();
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Refresh
				WorkbenchUtils.refreshIResource(container);

				// Open summary
				dialog.open();
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

}
