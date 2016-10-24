package org.but4reuse.feature.location.ui.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
				ArtefactModel am = FeatureListHelper.getArtefactModel(fl);

				List<List<Feature>> artefactAndFeatures = new ArrayList<List<Feature>>();
				for (Artefact a : am.getOwnedArtefacts()) {
					List<Feature> features = FeatureListHelper.getArtefactFeatures(fl, a);
					artefactAndFeatures.add(features);
				}

				double pairs = 0;
				double accumulatedJaccard = 0;
				for (int i = 0; i < artefactAndFeatures.size(); i++) {
					for (int j = i + 1; j < artefactAndFeatures.size(); j++) {
						pairs += 1;
						double jaccard = getSimilarity(artefactAndFeatures.get(i), artefactAndFeatures.get(j));
						accumulatedJaccard += jaccard;
					}
				}
				double result = accumulatedJaccard / pairs;

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

	public static double getSimilarity(Collection<?> s1, Collection<?> s2) {
		double sum = s1.size() + s2.size();
		Set<?> intersection = new HashSet(s1); // use the copy constructor
		intersection.retainAll(s2);
		return 1 - ((sum - 2 * intersection.size()) / (sum - intersection.size()));
	}

}
