package org.but4reuse.wordclouds.ui.actions;

import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.ui.AdaptersSelectionDialog;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.wordclouds.util.WordCloudUtil;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mcavallo.opencloud.Cloud;

/**
 * @author Arthur This class creates a pop-up menu for artefacts which will
 *         create a new window where a word cloud will be drawn
 */

public class ShowArtefactWordCloud implements IObjectActionDelegate {

	ISelection selection;
	Artefact artefact = null;
	List<IAdapter> adap;
	Cloud c = new Cloud();
	int widthWin = 600, heightWin = 600;

	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		c.setMaxWeight(50);
		c.setMinWeight(5);
		artefact = null;
		if (selection instanceof IStructuredSelection) {
			for (Object art : ((IStructuredSelection) selection).toArray()) {
				if (art instanceof Artefact) {
					artefact = ((Artefact) art);

					// check predefined
					List<IAdapter> defaultAdapters = null;
					EObject artefactModel = EcoreUtil.getRootContainer(artefact);
					if (artefactModel instanceof ArtefactModel) {
						defaultAdapters = AdaptersHelper
								.getAdaptersByIds(((ArtefactModel) artefactModel).getAdapters());
					}

					// Adapter selection by user
					adap = AdaptersSelectionDialog.show("Show Word Cloud", artefact, defaultAdapters);

					if (!adap.isEmpty()) {
						// Launch Progress dialog

						c.clear();
						for (IAdapter adapter : adap) {
							List<IElement> elements = AdaptersHelper.getElements(artefact, adapter);
							for (IElement element : elements) {
								AbstractElement ab = (AbstractElement) element;
								for (String s : ab.getWords())
									c.addTag(s);
							}
						}

						final Shell win = new Shell(Display.getCurrent().getActiveShell(), SWT.TITLE | SWT.CLOSE);
						win.setSize(widthWin, heightWin);
						win.setText("Word Cloud for artefact " + artefact.getName());

						Canvas canvas = new Canvas(win, SWT.BORDER);
						canvas.setBounds(5, 5, widthWin - 15, heightWin - 40);

						win.open();
						win.update();

						WordCloudUtil.drawWordCloud(canvas, c);

					}
				}
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub

	}

}
