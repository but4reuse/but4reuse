package org.but4reuse.adapters.ui.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.adapters.ui.AdaptersSelectionDialog;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * ShowElementDependenciesAction
 * @author Selma Sadouk
 * @author Julia Wisniewski
 */
public class ShowElementDependenciesAction implements IObjectActionDelegate {

	Artefact artefact = null;
	List<IAdapter> adap;
	List<String> text = new ArrayList<String>();

	@Override
	public void run(IAction action) {
		artefact = null;
		if (selection instanceof IStructuredSelection) {
			for (Object art : ((IStructuredSelection) selection).toArray()) {
				if (art instanceof Artefact) {
					artefact = ((Artefact) art);

					// Adapter selection by user
					adap = AdaptersSelectionDialog.show("Show Element Dependencies", artefact);

					if (!adap.isEmpty()) {


						// Launch Progress dialog
						ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getCurrent()
								.getActiveShell());

						try {
							progressDialog.run(true, true, new IRunnableWithProgress() {
								@Override
								public void run(IProgressMonitor monitor) throws InvocationTargetException,
								InterruptedException {

									int totalWork = 1;
									monitor.beginTask("Calculating dependencies of " + artefact.getArtefactURI(), totalWork);

									text.clear();
									for (IAdapter adapter : adap) {
										List<IElement> elements = AdaptersHelper.getElements(artefact, adapter);
										for (IElement element : elements) {
											//text.add(element.getText());
											//element.getDependencies(); ??
										}
									}
									monitor.worked(1);
									monitor.done();
								}
							});
/*
							String sText = "";
							for (String t : text) {
								t = t.replaceAll("\n", " ").replaceAll("\r", "");
								sText = sText + t + "\n";
							}
*/
							String name = artefact.getName();
							if (name == null || name.length() == 0) {
								name = artefact.getArtefactURI();
							}

							/*
							ScrollableMessageDialog dialog = new ScrollableMessageDialog(Display.getCurrent()
									.getActiveShell(), name, text.size() + " Elements", sText);
							dialog.open();
							 */

							PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("tryEclipseZest.view");

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
