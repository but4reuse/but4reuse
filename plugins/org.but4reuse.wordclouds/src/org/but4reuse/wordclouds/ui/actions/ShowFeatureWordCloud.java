package org.but4reuse.wordclouds.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.featurelist.Feature;
import org.but4reuse.wordclouds.util.Cloudifier;
import org.but4reuse.wordclouds.util.FeatureWordCloudUtil;
import org.but4reuse.wordclouds.util.WordCloudUtil;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mcavallo.opencloud.Cloud;

/**
 * @author Arthur This class creates a pop-up menu for artefacts which will
 *         create a new window where a word cloud will be drawn
 */

public class ShowFeatureWordCloud implements IObjectActionDelegate {

	ISelection selection;

	@Override
	public void run(IAction action) {

		if (selection instanceof IStructuredSelection) {
			for (Object feat : ((IStructuredSelection) selection).toArray()) {
				if (feat instanceof Feature) {
					Feature feature = ((Feature) feat);
					List<String> words = new ArrayList<String>();
					List<String> featureWords = FeatureWordCloudUtil.getFeatureWords(feature);
					words.addAll(featureWords);

					Cloud cloud = Cloudifier.cloudify(words, new NullProgressMonitor());

					final Shell win = new Shell(Display.getCurrent().getActiveShell(),
							SWT.TITLE | SWT.CLOSE | SWT.RESIZE);
					int widthWin = 600, heightWin = 600;
					win.setSize(widthWin, heightWin);
					win.setText("Word Cloud for feature " + feature.getName());

					Composite comp = new Composite(win, SWT.NORMAL);
					comp.setBounds(0, 0, win.getBounds().width, win.getBounds().height);

					win.open();
					win.update();

					WordCloudUtil.drawWordCloud(comp, cloud);
				}
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

}
