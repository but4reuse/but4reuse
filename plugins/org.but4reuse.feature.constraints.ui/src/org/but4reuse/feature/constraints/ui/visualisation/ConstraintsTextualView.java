package org.but4reuse.feature.constraints.ui.visualisation;

import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

/**
 * Constraints Textual view
 * 
 * @author jabier.martinez
 */
public class ConstraintsTextualView extends ViewPart {

	public final static String ID = "org.but4reuse.feature.constraints.ui.view";

	AdaptedModel adaptedModel = null;
	FeatureList featureList = null;
	List<IConstraint> constraints;
	Text scrollable = null;

	@Override
	public void createPartControl(Composite parent) {
		scrollable = new Text(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
		scrollable.setText("Results available after feature identification or localization");
	}

	@Override
	public void setFocus() {
	}

}
