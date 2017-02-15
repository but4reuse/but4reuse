package org.but4reuse.wordclouds.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.ui.AdaptersSelectionDialog;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.wordclouds.util.Cloudifier;
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
 * This class creates a pop-up menu for artefacts which will create a new window
 * where a word cloud will be drawn
 * 
 * @author Arthur
 * @author jabier.martinez
 */

public class ShowArtefactModelWordCloud implements IObjectActionDelegate {

	ISelection selection;

	@Override
	public void run(IAction action) {

		if (selection instanceof IStructuredSelection) {
			for (Object art : ((IStructuredSelection) selection).toArray()) {
				if (art instanceof ArtefactModel) {
					ArtefactModel artefactM = ((ArtefactModel) art);

					// check predefined
					List<IAdapter> defaultAdapters = null;

					defaultAdapters = AdaptersHelper.getAdaptersByIds(((ArtefactModel) artefactM).getAdapters());

					// Adapter selection by user
					List<IAdapter> adap = AdaptersSelectionDialog.show("Select adapter to show Word Cloud", artefactM,
							defaultAdapters);

					if (!adap.isEmpty()) {
						List<String> words = new ArrayList<String>();
						for (IAdapter adapter : adap) {
							for (Artefact artefact : artefactM.getOwnedArtefacts()) {
								List<IElement> elements = AdaptersHelper.getElements(artefact, adapter);
								for (IElement element : elements) {
									AbstractElement ab = (AbstractElement) element;
									for (String s : ab.getWords()) {
										words.add(s);
									}
								}
							}
						}

						Cloud cloud = Cloudifier.cloudify(words, new NullProgressMonitor());

						final Shell win = new Shell(Display.getCurrent().getActiveShell(),
								SWT.TITLE | SWT.CLOSE | SWT.RESIZE);
						int widthWin = 700, heightWin = 700;
						win.setSize(widthWin, heightWin);
						win.setText("Word Cloud for Artefact Model " + artefactM.getName());

						Composite comp = new Composite(win, SWT.NORMAL);
						comp.setBounds(0, 0, win.getBounds().width, win.getBounds().height);

						WordCloudUtil.drawWordCloud(comp, cloud);

						win.open();
						win.update();
					}
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
