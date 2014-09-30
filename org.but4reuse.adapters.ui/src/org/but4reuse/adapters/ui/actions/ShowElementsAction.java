package org.but4reuse.adapters.ui.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.adapters.ui.AdaptersSelectionDialog;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.but4reuse.variantsmodel.Variant;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ShowElementsAction implements IObjectActionDelegate {

	Variant variant = null;
	List<IAdapter> adap;
	List<String> text = new ArrayList<String>();

	@Override
	public void run(IAction action) {
		variant = null;
		if (selection instanceof IStructuredSelection) {
			for (Object art : ((IStructuredSelection) selection).toArray()) {
				// Object art = ((IStructuredSelection)
				// selection).getFirstElement();
				if (art instanceof Variant) {
					variant = ((Variant) art);

					// Adapter selection by user
					adap = AdaptersSelectionDialog.show("Show elements", variant);

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
									monitor.beginTask("Calculating elements of " + variant.getVariantURI(), totalWork);

									text.clear();
									for (IAdapter adapter : adap) {
										List<IElement> cps = AdaptersHelper.getElements(variant, adapter);
										for (IElement cp : cps) {
											text.add(cp.getText());
										}
									}
									monitor.worked(1);
									monitor.done();
								}
							});

							String sText = "";
							for (String t : text) {
								t = t.replaceAll("\n", " ").replaceAll("\r", "");
								sText = sText + t + "\n";
							}
							String name = variant.getName();
							if (name == null || name.length() == 0) {
								name = variant.getVariantURI();
							}
							ScrollableMessageDialog dialog = new ScrollableMessageDialog(Display.getCurrent()
									.getActiveShell(), name, text.size() + " Elements", sText);
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

}
