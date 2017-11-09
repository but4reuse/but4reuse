package org.but4reuse.adapters.ui.actions;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.IElement;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.utils.emf.EMFUtils;
import org.but4reuse.utils.ui.dialogs.URISelectionDialog;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * Serialize AdaptedModel action
 * 
 * @author jabier.martinez
 */
public class SerializeAction implements IViewActionDelegate {

	String containerURIString;

	@Override
	public void run(IAction action) {

		// Get construction uri from user
		String out = "/projectName";
		IContainer output = AdaptedModelManager.getDefaultOutput();
		if (output != null) {
			out = output.getFullPath().toString();
		}
		URISelectionDialog inputDialog = new URISelectionDialog(Display.getCurrent().getActiveShell(), "Container URI",
				"Insert Container URI", "platform:/resource" + out + "/");
		if (inputDialog.open() != Dialog.OK) {
			return;
		}
		containerURIString = inputDialog.getValue();
		if (!containerURIString.endsWith("/")) {
			containerURIString += '/';
		}

		// Launch Progress dialog
		ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());

		try {
			progressDialog.run(true, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

					// check if artefact model file exists, then put texts and
					// save
					int totalWork = 3;
					monitor.beginTask("Serialize Adapted Model", totalWork);

					AdaptedModel adaptedModel = AdaptedModelManager.getAdaptedModel();

					// first check if we need to serialise also the artefact
					// model
					ArtefactModel artefactModel = AdaptedModelHelper.getArtefactModel(adaptedModel);
					if (artefactModel.eResource() == null) {
						try {
							String artefactModelURI = containerURIString + "analysis.artefactmodel";
							EMFUtils.saveEObject(new URI(artefactModelURI), artefactModel);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					monitor.worked(1);

					for (AdaptedArtefact aa : adaptedModel.getOwnedAdaptedArtefacts()) {
						for (ElementWrapper ew : aa.getOwnedElementWrappers()) {
							ew.setText(((IElement) ew.getElement()).getText());
						}
					}
					monitor.worked(1);

					try {
						String adaptedModelURI = containerURIString + "analysis.adaptedmodel";
						EMFUtils.saveEObject(new URI(adaptedModelURI), adaptedModel);
					} catch (Exception e) {
						e.printStackTrace();
					}
					monitor.worked(1);
					monitor.done();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		// TODO try to be more specific about the project to refresh
		WorkbenchUtils.refreshAllWorkspace();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

	@Override
	public void init(IViewPart view) {
	}

}
