package org.but4reuse.adapters.ui.actions;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.adapters.ui.AdaptersSelectionDialog;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.artefactmodel.ArtefactModelFactory;
import org.but4reuse.utils.strings.StringUtils;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Show Elements Action
 * 
 * @author jabier.martinez
 */
public class ShowArtefactElementsAction implements IObjectActionDelegate, IViewActionDelegate {

	Artefact artefact = null;
	List<IAdapter> adap;
	List<String> text = new ArrayList<String>();

	@Override
	public void run(IAction action) {
		artefact = null;
		if (selection instanceof IStructuredSelection) {
			for (Object art : ((IStructuredSelection) selection).toArray()) {
				if (art instanceof IResource) {
					IResource resource = (IResource) art;
					artefact = ArtefactModelFactory.eINSTANCE.createArtefact();
					URI uri = WorkbenchUtils.getURIFromIResource(resource);
					artefact.setArtefactURI(uri.toString());
					artefact.setName(resource.getName());
				} else if (art instanceof Artefact) {
					artefact = ((Artefact) art);
				}
				if (artefact != null) {
					// check predefined
					List<IAdapter> defaultAdapters = null;
					EObject artefactModel = EcoreUtil.getRootContainer(artefact);
					if (artefactModel instanceof ArtefactModel) {
						defaultAdapters = AdaptersHelper
								.getAdaptersByIds(((ArtefactModel) artefactModel).getAdapters());
					}

					// Adapter selection by user
					adap = AdaptersSelectionDialog.show("Show elements", artefact, defaultAdapters);

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

									text.clear();
									for (IAdapter adapter : adap) {
										List<IElement> elements = AdaptersHelper.getElements(artefact, adapter);
										for (IElement element : elements) {
											text.add(element.getText());
										}
									}
									monitor.worked(1);
									monitor.done();
								}
							});

							// Create the text to be shown
							StringBuilder sText = new StringBuilder();
							for (String t : text) {
								sText.append(StringUtils.removeNewLines(t));
								sText.append("\n");
							}
							String name = artefact.getName();
							if (name == null || name.length() == 0) {
								name = artefact.getArtefactURI();
							}
							ScrollableMessageDialog dialog = new ScrollableMessageDialog(
									Display.getCurrent().getActiveShell(), name, text.size() + " Elements",
									sText.toString());
							dialog.open();
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

	@Override
	public void init(IViewPart view) {

	}

}
