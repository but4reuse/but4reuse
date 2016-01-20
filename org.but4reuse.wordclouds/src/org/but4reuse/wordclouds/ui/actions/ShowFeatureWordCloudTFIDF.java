package org.but4reuse.wordclouds.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.wordclouds.util.Cloudifier;
import org.but4reuse.wordclouds.util.FeatureWordCloudUtil;
import org.but4reuse.wordclouds.util.WordCloudUtil;
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

public class ShowFeatureWordCloudTFIDF implements IObjectActionDelegate {

	ISelection selection;
	Feature feature = null;
	List<IAdapter> adap;
	Cloud c = new Cloud();
	int widthWin = 600, heightWin = 600;

	@Override
	public void run(IAction action) {
		feature = null;

		List<List<String>> list = null;
		FeatureList fList = null;

		if (selection instanceof IStructuredSelection) {
			for (Object feat : ((IStructuredSelection) selection).toArray()) {
				if (feat instanceof Feature) {
					feature = ((Feature) feat);
					
					if (list == null) {
						list = new ArrayList<List<String>>();
						fList = (FeatureList) feature.eContainer();

						for (Feature f : fList.getOwnedFeatures()) {
							list.add(FeatureWordCloudUtil.getFeatureWords(f));
						}
					}

					c.clear();

					final Shell win = new Shell(Display.getCurrent().getActiveShell(), SWT.TITLE | SWT.CLOSE | SWT.RESIZE);
					win.setSize(widthWin, heightWin);
					win.setText("Word Cloud for feature " + feature.getName());

					Composite comp = new Composite(win, SWT.NORMAL);
					comp.setBounds(0, 0, win.getBounds().width, win.getBounds().height);

					win.open();
					win.update();
					c = Cloudifier.cloudifyTFIDF(list, fList.getOwnedFeatures().indexOf(feature));
					WordCloudUtil.drawWordCloud(comp, c);

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
