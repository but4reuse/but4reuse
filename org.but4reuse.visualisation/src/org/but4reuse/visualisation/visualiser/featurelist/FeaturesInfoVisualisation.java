package org.but4reuse.visualisation.visualiser.featurelist;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
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
		// asyncExec to avoid SWT invalid thread access
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				String text = "";
				for (Feature feature : featureList.getOwnedFeatures()) {
					text += feature.getName() + " = ";
					List<Block> blocks = getCorrespondingBlocks(feature);
					for(Block b : blocks){
						text += b.getName() + ", ";
					}
					// remove last comma
					if(!blocks.isEmpty()){
						text = text.substring(0, text.length()-2);
					}
					text += "\n";
				}

				Display display = Display.getDefault();
				Shell shell = new Shell(display);
				ScrollableMessageDialog m = new ScrollableMessageDialog(shell, "Features info", "", text);
				m.open();
			}
			
			public List<Block> getCorrespondingBlocks(Feature feature) {
				List<Block> correspondingBlocks = new ArrayList<Block>();
				for (Block block : adaptedModel.getOwnedBlocks()) {
					List<Feature> features = block.getCorrespondingFeatures();
					if (features != null && features.contains(feature) && !correspondingBlocks.contains(block)) {
						correspondingBlocks.add(block);
					}
				}
				return correspondingBlocks;
			}
		});
	}

}
