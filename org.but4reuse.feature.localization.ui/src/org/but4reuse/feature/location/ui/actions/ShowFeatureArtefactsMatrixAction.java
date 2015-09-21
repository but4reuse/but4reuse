package org.but4reuse.feature.location.ui.actions;

import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.featurelist.helpers.FeatureListHelper;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Show feature artefacts matrix
 * 
 * @author jabier.martinez
 */
public class ShowFeatureArtefactsMatrixAction implements IObjectActionDelegate {

	FeatureList featureList;
	List<IAdapter> adapters;
	ArtefactModel artefactModel;

	@Override
	public void run(IAction action) {
		if (selection instanceof IStructuredSelection) {
			Object featureListObject = ((IStructuredSelection) selection).getFirstElement();
			if (featureListObject instanceof FeatureList) {
				FeatureList fl = (FeatureList) featureListObject;
				StringBuffer sb = new StringBuffer();
				ArtefactModel am = FeatureListHelper.getArtefactModel(fl);
				sb.append(";");
				for (Artefact a : am.getOwnedArtefacts()) {
					sb.append(a.getName() + ";");
				}
				sb.setLength(sb.length() - 1);
				sb.append("\n");
				for (Feature f : fl.getOwnedFeatures()) {
					sb.append(f.getId() + ";");
					for (Artefact a : am.getOwnedArtefacts()) {
						if (f.getImplementedInArtefacts().contains(a)) {
							sb.append("1;");
						} else {
							sb.append("0;");
						}
					}
					sb.setLength(sb.length() - 1);
					sb.append("\n");
				}
				ScrollableMessageDialog dialog = new ScrollableMessageDialog(Display.getCurrent().getActiveShell(), "",
						"", sb.toString());
				dialog.open();
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
