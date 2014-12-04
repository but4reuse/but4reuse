package org.but4reuse.feature.constraints.ui.visualisation;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.feature.constraints.IConstraintsDiscovery;
import org.but4reuse.feature.constraints.impl.BinaryRelationConstraintsDiscovery;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.visualisation.IVisualisation;
import org.but4reuse.visualisation.helpers.VisualisationsHelper;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * Constraints Textual view
 * 
 * @author jabier.martinez
 */
public class ConstraintsTextualView extends ViewPart {

	AdaptedModel adaptedModel = null;
	FeatureList featureList = null;
	String constraints = "";
	
	@Override
	public void createPartControl(Composite parent) {
		final Text scrollable = new Text(parent, SWT.BORDER | SWT.V_SCROLL | SWT.READ_ONLY);
		Action action = new Action() {
			@Override
			public void run() {


				// We get the information from the cached visualisation
				List<IVisualisation> visualisations = VisualisationsHelper.getAllVisualisations();
				for (IVisualisation visualisation : visualisations) {
					if (visualisation instanceof ConstraintsTextualVisualisation) {
						ConstraintsTextualVisualisation v = (ConstraintsTextualVisualisation) visualisation;
						adaptedModel = v.adaptedModel;
						featureList = v.featureList;
					}
				}

				if (adaptedModel == null) {
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "",
							"No feature identification or localization was performed yet");
					return;
				}
				// Launch Progress dialog
				ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());

				try {
					progressDialog.run(true, true, new IRunnableWithProgress() {
						@Override
						public void run(IProgressMonitor monitor) throws InvocationTargetException,
								InterruptedException {
							IConstraintsDiscovery constraintsDiscoveryAlgorithm = new BinaryRelationConstraintsDiscovery();
							constraints = constraintsDiscoveryAlgorithm.discover(featureList, adaptedModel,
									null, monitor);
							if (constraints.length() == 0) {
								constraints = ("No constraints found");
							}
							
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				scrollable.setText(constraints);
			}
		};
		action.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ELCL_SYNCED));
		IActionBars bars = getViewSite().getActionBars();
		bars.getToolBarManager().add(action);
		scrollable
				.setText("This operation is performed on demand.\nClick the Synch button after feature identification or localization");
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

}
