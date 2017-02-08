package org.but4reuse.visualisation.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

/**
 * Metrics Textual view
 * 
 * @author jabier.martinez
 */
public class MetricsVisualisationView extends ViewPart {

	public final static String ID = "org.but4reuse.visualisation.metrics";

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
