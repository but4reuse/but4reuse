package org.but4reuse.wordclouds.ui.actions;

import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.ui.AdaptersSelectionDialog;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.wordclouds.activator.Activator;
import org.but4reuse.wordclouds.preferences.WordCloudPreferences;
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

public class ShowArtefactModelWordCloud implements IObjectActionDelegate {

	ISelection selection;
	ArtefactModel artefactM = null;
	List<IAdapter> adap;
	Cloud c = new Cloud();
	int widthWin = 700, heightWin = 700;

	@Override
	public void run(IAction action) {
		c.setMaxTagsToDisplay(Activator.getDefault().getPreferenceStore().getInt(WordCloudPreferences.WORDCLOUD_NB_W));
		c.setMaxWeight(50);
		c.setMinWeight(5);
		artefactM = null;
		if (selection instanceof IStructuredSelection) {
			for (Object art : ((IStructuredSelection) selection).toArray()) {
				if (art instanceof ArtefactModel) {
					artefactM = ((ArtefactModel) art);

					// check predefined
					List<IAdapter> defaultAdapters = null;

					defaultAdapters = AdaptersHelper.getAdaptersByIds(((ArtefactModel) artefactM).getAdapters());

					// Adapter selection by user
					adap = AdaptersSelectionDialog.show("Show Word Cloud", artefactM, defaultAdapters);

					if (!adap.isEmpty()) {
						// Launch Progress dialog

						c.clear();
						for (IAdapter adapter : adap) {
							for (Artefact artefact : artefactM.getOwnedArtefacts()) {
								List<IElement> elements = AdaptersHelper.getElements(artefact, adapter);
								for (IElement element : elements) {
									AbstractElement ab = (AbstractElement) element;
									for (String s : ab.getWords())
										c.addTag(s);
								}

							}
						}

						final Shell win = new Shell(Display.getCurrent().getActiveShell(), SWT.TITLE | SWT.CLOSE);
						win.setSize(widthWin, heightWin);
						win.setText("Word Cloud for Artefact Model " + artefactM.getName());

						Composite comp = new Composite(win, SWT.NORMAL);
						comp.setBounds(0, 0, win.getBounds().width, win.getBounds().height);

						win.open();
						win.update();

						WordCloudUtil.drawWordCloud(comp, c);

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
