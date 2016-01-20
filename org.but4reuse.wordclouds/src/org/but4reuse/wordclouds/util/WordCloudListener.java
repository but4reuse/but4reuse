package org.but4reuse.wordclouds.util;

import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.visualisation.helpers.VisualisationsHelper;
import org.but4reuse.wordclouds.visualisation.WordCloudView;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Label;

public class WordCloudListener implements MouseListener {

	private final int blockIndex;

	public WordCloudListener(int blockIndex) {
		this.blockIndex = blockIndex;
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
	}

	@Override
	public void mouseDown(MouseEvent e) {
		if (e.getSource() instanceof Label) {
			// Change block name
			String newName = ((Label) e.getSource()).getText();
			Block b = AdaptedModelManager.getAdaptedModel().getOwnedBlocks().get(blockIndex);
			b.setName(newName);

			VisualisationsHelper.notifyVisualisations(AdaptedModelManager.getFeatureList(),
					AdaptedModelManager.getAdaptedModel(), null, new NullProgressMonitor());
			WordCloudView.update(blockIndex, true);
		}
	}

	@Override
	public void mouseUp(MouseEvent e) {
	}
}
