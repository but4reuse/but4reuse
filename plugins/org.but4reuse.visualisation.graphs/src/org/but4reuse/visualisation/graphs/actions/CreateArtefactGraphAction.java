package org.but4reuse.visualisation.graphs.actions;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.AdaptedModelFactory;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.adapters.ui.AdaptersSelectionDialog;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.ui.dialogs.URISelectionDialog;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.but4reuse.visualisation.graphs.ElementsGraphVisualisation;
import org.but4reuse.visualisation.graphs.utils.GraphUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import com.tinkerpop.blueprints.Graph;

/**
 * Create Elements Action
 * 
 * @author jabier.martinez
 */
public class CreateArtefactGraphAction implements IObjectActionDelegate {

	Artefact artefact = null;
	List<IAdapter> adap;
	URI graphURI;

	@Override
	public void run(IAction action) {

		// Get the graph uri from user
		String out = "/projectName";
		IContainer output = AdaptedModelManager.getDefaultOutput();
		if (output != null) {
			out = output.getFullPath().toString();
		}
		URISelectionDialog inputDialog = new URISelectionDialog(Display.getCurrent().getActiveShell(), "Graph URI",
				"Insert Graph URI (graphml or tgf extensions)", "platform:/resource" + out + "/elements.graphml");
		if (inputDialog.open() != Dialog.OK) {
			return;
		}
		String constructionURIString = inputDialog.getValue();
		graphURI = null;
		try {
			graphURI = new URI(constructionURIString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		artefact = null;
		if (selection instanceof IStructuredSelection) {
			for (Object art : ((IStructuredSelection) selection).toArray()) {
				if (art instanceof Artefact) {
					artefact = ((Artefact) art);

					// check predefined
					List<IAdapter> defaultAdapters = null;
					EObject artefactModel = EcoreUtil.getRootContainer(artefact);
					if (artefactModel instanceof ArtefactModel) {
						defaultAdapters = AdaptersHelper
								.getAdaptersByIds(((ArtefactModel) artefactModel).getAdapters());
					}

					// Adapter selection by user
					adap = AdaptersSelectionDialog.show("Create Graph Visualisation", artefact, defaultAdapters);

					if (!adap.isEmpty()) {
						// Launch Progress dialog
						ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(
								Display.getCurrent().getActiveShell());

						try {
							progressDialog.run(true, true, new IRunnableWithProgress() {
								@Override
								public void run(IProgressMonitor monitor)
										throws InvocationTargetException, InterruptedException {
									int totalWork = 1;
									monitor.beginTask("Calculating elements of " + artefact.getArtefactURI(),
											totalWork);
									AdaptedArtefact adaptedArtefact = AdaptedModelHelper.adapt(artefact, adap, monitor);
									AdaptedModel adaptedModel = AdaptedModelFactory.eINSTANCE.createAdaptedModel();
									adaptedModel.getOwnedAdaptedArtefacts().add(adaptedArtefact);

									Block fakeBlock = AdaptedModelFactory.eINSTANCE.createBlock();
									fakeBlock.setName("GraphBlock");
									for (ElementWrapper ew : adaptedArtefact.getOwnedElementWrappers()) {
										BlockElement blockElement = AdaptedModelFactory.eINSTANCE.createBlockElement();
										blockElement.getElementWrappers().add(ew);
										fakeBlock.getOwnedBlockElements().add(blockElement);
									}
									adaptedModel.getOwnedBlocks().add(fakeBlock);

									// Create the graph
									Graph graph = ElementsGraphVisualisation.createElementsGraph(adaptedModel, monitor);

									// Save
									File graphFile = FileUtils.getFile(graphURI);
									// Trivial graph format
									if (FileUtils.isExtension(graphFile, "tgf")) {
										GraphUtils.saveAsTGFGraph(graph, graphFile);
									} else {
										// graphml or any other extension
										// introduced by the user
										GraphUtils.saveGraph(graph, graphFile);
									}

									// Refresh
									IResource iResource = WorkbenchUtils.getIResourceFromURI(graphURI);
									if (iResource != null) {
										WorkbenchUtils.refreshIResource(iResource);
									}

									monitor.worked(1);
									monitor.done();
								}
							});
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	ISelection selection;

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

}
