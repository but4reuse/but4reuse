package org.but4reuse.feature.constraints.ui.visualisation;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.impl.ConstraintsHelper;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.but4reuse.visualisation.IVisualisation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;

/**
 * Constraints textual visualisation
 * 
 * @author jabier.martinez
 * 
 */
public class ConstraintsTextualVisualisation implements IVisualisation {

	String message;

	@Override
	public void prepare(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor) {
		Object o = adaptedModel.getConstraints();
		List<IConstraint> constraints = new ArrayList<IConstraint>();
		if (o != null && (o instanceof List<?>)) {
			constraints = (List<IConstraint>) o;
		}
		message = ConstraintsHelper.getText(constraints);
		if (message.isEmpty()) {
			message = "No structural constraints were identified";
		} else {
			message += "\n\nExplanations\n" + ConstraintsHelper.getTextWithExplanations(constraints);
		}
	}

	@Override
	public void show() {
		// asyncExec to avoid SWT invalid thread access
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				ConstraintsTextualView view = (ConstraintsTextualView) WorkbenchUtils
						.forceShowView(ConstraintsTextualView.ID);
				
				view.scrollable.setText(message);
			}
		});
	}

}
