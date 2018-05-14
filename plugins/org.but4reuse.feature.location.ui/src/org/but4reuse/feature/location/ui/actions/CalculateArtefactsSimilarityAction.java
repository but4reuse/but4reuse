package org.but4reuse.feature.location.ui.actions;

import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.artefactmodel.ArtefactModel;
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
 * Calculate similarity
 * 
 * @author jabier.martinez
 */
public class CalculateArtefactsSimilarityAction implements IObjectActionDelegate {

	FeatureList featureList;
	List<IAdapter> adapters;
	ArtefactModel artefactModel;

	@Override
	public void run(IAction action) {
		if (selection instanceof IStructuredSelection) {
			Object featureListObject = ((IStructuredSelection) selection).getFirstElement();
			if (featureListObject instanceof FeatureList) {
				FeatureList fl = (FeatureList) featureListObject;
				double result = FeatureListHelper.getJaccardSimilarity(fl);
				ScrollableMessageDialog dialog = new ScrollableMessageDialog(Display.getCurrent().getActiveShell(), "",
						"", "Jaccard similarity: " + result);
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
