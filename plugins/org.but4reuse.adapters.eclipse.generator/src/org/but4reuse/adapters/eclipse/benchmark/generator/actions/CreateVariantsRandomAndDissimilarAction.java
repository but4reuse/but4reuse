package org.but4reuse.adapters.eclipse.benchmark.generator.actions;

import java.io.File;
import java.io.IOException;

import org.but4reuse.adapters.eclipse.benchmark.generator.VariantsRandomAndDissimilarGenerator;
import org.but4reuse.adapters.eclipse.benchmark.generator.dialogs.RandomAndDissimilarDialog;
import org.but4reuse.adapters.eclipse.benchmark.generator.utils.VariantsUtils;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.artefactmodel.ArtefactModelFactory;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Julien Margarido
 * @author Felix Lima Gorito
 */
public class CreateVariantsRandomAndDissimilarAction implements IObjectActionDelegate {

	private ISelection selection;

	public CreateVariantsRandomAndDissimilarAction() {
		super();
	}

	private RandomAndDissimilarDialog dialog;

	public void run(IAction action) {

		if (dialog == null) {
			// Not create a new dialog if it's a "re-open" (parameters not
			// good).
			dialog = new RandomAndDissimilarDialog(Display.getCurrent().getActiveShell());
		}

		if (dialog.open() != Window.OK) {
			// Open the dialog and stop execution while a button is not pressed
			dialog = null;
			return;
		}

		// Settings checking
		int time = 0;
		int nbVariants = 0;
		boolean isAllOK = true;

		if (!new File(dialog.getInputPath()).exists()) {
			dialog.setInputState(false);
			isAllOK = false;
		} else {
			dialog.setInputState(true);
		}

		try {
			time = Integer.parseInt(dialog.getTime());
			if ((time < 0)) {
				isAllOK = false;
				dialog.setTimeState(false);
			} else {
				dialog.setTimeState(true);
			}
		} catch (NumberFormatException e) {
			isAllOK = false;
			dialog.setTimeState(false);
			e.printStackTrace();
		}

		try {
			nbVariants = Integer.parseInt(dialog.getVariantsNumber());
			if (nbVariants <= 0) {
				isAllOK = false;
				dialog.setVariantsNumberState(false);
			} else {
				dialog.setVariantsNumberState(true);
			}
		} catch (NumberFormatException e) {
			isAllOK = false;
			dialog.setVariantsNumberState(false);
			e.printStackTrace();
		}

		if (!isAllOK) {
			this.run(action);
			return;
		}

		// Start the generator process
		// final for the thread and because nbVariants and time can't be
		// final
		final int nbVariantsForThread = nbVariants;
		final int timeForThread = time;
		final boolean keepOnlyMetadata = dialog.isKeepOnlyMetadata();
		final boolean noOutputOnlyStatistics = dialog.isNoOutputOnlyStatistics();

		VariantsRandomAndDissimilarGenerator varGen = new VariantsRandomAndDissimilarGenerator(dialog.getInputPath(),
				dialog.getOutputPath(), dialog.getGeneratorPath(), nbVariantsForThread, timeForThread, keepOnlyMetadata,
				noOutputOnlyStatistics);
		// Long time to execute
		String message = varGen.generate(new NullProgressMonitor());
		System.out.println(message);

		// Update the artefact model
		if (!dialog.isNoOutputOnlyStatistics()) {
			// get the selection
			ArtefactModel artefactModel = null;
			if (selection instanceof IStructuredSelection) {
				artefactModel = (ArtefactModel) ((IStructuredSelection) selection).getFirstElement();
				artefactModel.getOwnedArtefacts().clear();
				// create artefact and set some attributes
				for (int i = 1; i <= nbVariantsForThread; i++) {
					Artefact a = ArtefactModelFactory.eINSTANCE.createArtefact();
					String varName = VariantsUtils.VARIANT + "_" + i;
					String output_variant = dialog.getOutputPath() + File.separator + varName;
					a.setName(varName);
					a.setArtefactURI(new File(output_variant).toURI().toString());
					// add to the artefact model
					artefactModel.getOwnedArtefacts().add(a);
				}
			}

			try {
				artefactModel.eResource().save(null);
			} catch (IOException e) {
				e.printStackTrace();
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
