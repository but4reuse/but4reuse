package org.but4reuse.feature.constraints.ui.visualisation;

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

	StringBuilder message;
	AdaptedModel adaptedModel;
	FeatureList featureList;

	@Override
	public void prepare(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor) {
		message = new StringBuilder();
		this.adaptedModel = adaptedModel;
		this.featureList = featureList;
	}

	@Override
	public void show() {
		// asyncExec to avoid SWT invalid thread access
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				List<IConstraint> blockConstraints = ConstraintsHelper.getCalculatedConstraints(adaptedModel);
				// Feature constraints if there is a feature list
				if (featureList != null) {
					message.append("Discovered Feature Constraints\n");
					List<IConstraint> featureConstraints = ConstraintsHelper.getFeatureConstraints(featureList,
							adaptedModel);
					if (featureConstraints.isEmpty()) {
						message.append("No structural feature constraints were identified\n\n");
					} else {
						message.append(ConstraintsHelper.getText(featureConstraints));
						message.append("\n\nExplanations\n"
								+ ConstraintsHelper.getTextWithExplanations(featureConstraints) + "\n");
					}
				}
				// Block constraints
				message.append("Discovered Block Constraints\n");
				if (blockConstraints.isEmpty()) {
					message.append("No structural block constraints were identified\n");
				} else {
					message.append(ConstraintsHelper.getText(blockConstraints));
					message.append("\n\nExplanations\n" + ConstraintsHelper.getTextWithExplanations(blockConstraints));
				}

				ConstraintsTextualView view = (ConstraintsTextualView) WorkbenchUtils
						.forceShowView(ConstraintsTextualView.ID);

				view.scrollable.setText(message.toString());
			}
		});
	}

}
