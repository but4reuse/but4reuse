package org.but4reuse.wordclouds.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
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

public class ShowArtefactWordCloudTFIDF implements IObjectActionDelegate {

	ISelection selection;

	@Override
	public void run(IAction action) {

		List<List<String>> artefactsWords = null;

		// Show one word cloud for each selected artefact
		if (selection instanceof IStructuredSelection) {
			for (Object art : ((IStructuredSelection) selection).toArray()) {
				if (art instanceof Artefact) {
					Artefact selectedArtefact = ((Artefact) art);

					// check predefined
					List<IAdapter> defaultAdapters = null;
					EObject artefactModel = EcoreUtil.getRootContainer(selectedArtefact);
					if (artefactModel instanceof ArtefactModel) {
						defaultAdapters = AdaptersHelper
								.getAdaptersByIds(((ArtefactModel) artefactModel).getAdapters());
					}

					// Adapter selection by user
					List<IAdapter> adap = AdaptersSelectionDialog.show("Show Word Cloud", selectedArtefact,
							defaultAdapters);

					if (!adap.isEmpty()) {

						if (artefactsWords == null) {
							artefactsWords = new ArrayList<List<String>>();
							for (Artefact a : ((ArtefactModel) artefactModel).getOwnedArtefacts()) {
								List<String> artefactWords = new ArrayList<String>();
								for (IAdapter adapter : adap) {
									List<IElement> elements = AdaptersHelper.getElements(a, adapter);
									for (IElement element : elements) {
										AbstractElement ab = (AbstractElement) element;
										artefactWords.addAll(ab.getWords());
									}
								}
								artefactsWords.add(artefactWords);
							}
						}

						final Shell win = new Shell(Display.getCurrent().getActiveShell(),
								SWT.TITLE | SWT.CLOSE | SWT.RESIZE);
						int widthWin = 700, heightWin = 700;
						win.setSize(widthWin, heightWin);
						win.setText("TF-IDF Word Cloud for artefact "
								+ AdaptedModelHelper.getArtefactName(selectedArtefact));

						Composite comp = new Composite(win, SWT.NORMAL);
						comp.setBounds(0, 0, win.getBounds().width, win.getBounds().height);

						win.open();
						win.update();

						Cloud c = Cloudifier.cloudifyTFIDF(artefactsWords,
								((ArtefactModel) artefactModel).getOwnedArtefacts().indexOf(selectedArtefact),
								new NullProgressMonitor());

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
