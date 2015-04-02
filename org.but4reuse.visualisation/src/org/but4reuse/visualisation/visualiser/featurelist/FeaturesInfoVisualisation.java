package org.but4reuse.visualisation.visualiser.featurelist;

import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.feature.constraints.impl.ConstraintsHelper;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.but4reuse.visualisation.IVisualisation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * FeatureSpecificHeatMapVisualisation
 * 
 * @author jabier.martinez
 */
public class FeaturesInfoVisualisation implements IVisualisation {
	FeatureList featureList;
	AdaptedModel adaptedModel;

	@Override
	public void prepare(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor) {
		this.featureList = featureList;
		this.adaptedModel = adaptedModel;
	}

	@Override
	public void show() {
		if (featureList != null) {
			// asyncExec to avoid SWT invalid thread access
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					StringBuilder text = new StringBuilder();
					for (Feature feature : featureList.getOwnedFeatures()) {
						text.append(feature.getName() + " = ");
						List<Block> blocks = ConstraintsHelper.getCorrespondingBlocks(adaptedModel, feature);
						for (Block b : blocks) {
							text.append(b.getName() + ", ");
						}
						// remove last comma
						if (!blocks.isEmpty()) {
							text.setLength(text.length() - 2);
						}
						text.append("\n");
					}

					Display display = Display.getDefault();
					Shell shell = new Shell(display);
					ScrollableMessageDialog m = new ScrollableMessageDialog(shell, "Features info", "", text.toString());
					m.open();
				}
			});
		}
	}

}
