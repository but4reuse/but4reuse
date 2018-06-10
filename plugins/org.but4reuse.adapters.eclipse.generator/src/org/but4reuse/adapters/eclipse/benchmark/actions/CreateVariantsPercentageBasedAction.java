package org.but4reuse.adapters.eclipse.benchmark.actions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.but4reuse.adapters.eclipse.benchmark.generator.VariantsPercentageBasedGenerator;
import org.but4reuse.adapters.eclipse.benchmark.generator.dialogs.PercentageBasedDialog;
import org.but4reuse.adapters.eclipse.benchmark.generator.utils.VariantsUtils;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.artefactmodel.ArtefactModelFactory;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
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
public class CreateVariantsPercentageBasedAction implements IObjectActionDelegate {

	private ISelection selection;

	public CreateVariantsPercentageBasedAction() {
		super();
	}

	private PercentageBasedDialog paramDialog;

	public void run(IAction action) {

		if (paramDialog == null) {
			paramDialog = new PercentageBasedDialog(Display.getCurrent().getActiveShell());
		}

		if (paramDialog.open() != Window.OK) {
			// Open the dialog and stop execution while a button is not pressed
			paramDialog = null;
			return;
		}

		// Settings checking
		int valRand = 0;
		int nbVariants = 0;
		Long seedRand = null;
		boolean isAllOK = true;

		if (!new File(paramDialog.getInputPath()).exists()) {
			paramDialog.setInputState(false);
			isAllOK = false;
		} else {
			paramDialog.setInputState(true);
		}

		try {
			valRand = Integer.parseInt(paramDialog.getRandomSelector());
			if ((valRand < 0 || valRand > 100)) {
				isAllOK = false;
				paramDialog.setRandomSelectorState(false);
			} else {
				paramDialog.setRandomSelectorState(true);
			}
		} catch (NumberFormatException e) {
			isAllOK = false;
			paramDialog.setRandomSelectorState(false);
		}

		try {
			nbVariants = Integer.parseInt(paramDialog.getVariantsNumber());
			if (nbVariants <= 0) {
				isAllOK = false;
				paramDialog.setVariantsNumberState(false);
			} else {
				paramDialog.setVariantsNumberState(true);
			}
		} catch (NumberFormatException e) {
			isAllOK = false;
			paramDialog.setVariantsNumberState(false);
		}

		try {
			if (paramDialog.getRandomSeed() != null && !paramDialog.getRandomSeed().isEmpty()) {
				seedRand = Long.parseLong(paramDialog.getRandomSeed());
			}
		} catch (NumberFormatException e) {
			isAllOK = false;
			paramDialog.setRandomSeedState(false);
		}

		// retry
		if (!isAllOK) {
			this.run(action);
			return;
		}

		// Start the generator process
		final int nbVariantsForThread = nbVariants;
		final int valRandForThread = valRand;
		final Long seedRandForThread = seedRand;
		final boolean keepOnlyMetadata = paramDialog.isKeepOnlyMetadata();
		final boolean noOutputOnlyStatistics = paramDialog.isNoOutputOnlyStatistics();

		final VariantsPercentageBasedGenerator varGen = new VariantsPercentageBasedGenerator(paramDialog.getInputPath(),
				paramDialog.getOutputPath(), nbVariantsForThread, valRandForThread, seedRandForThread, keepOnlyMetadata,
				noOutputOnlyStatistics);

		final ScrollableMessageDialog dialog = new ScrollableMessageDialog(Display.getCurrent().getActiveShell(),
				"Percentage-based generator", "", "");

		// Long time to execute
		// Launch Progress dialog
		ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
		try {
			progressDialog.run(true, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					// variants + 1 for the preparation
					monitor.beginTask("Generating variants", nbVariantsForThread + 1);
					dialog.scrollableText = varGen.generate(monitor);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Update the artefact model
		if (!paramDialog.isNoOutputOnlyStatistics()) {
			// get the selection
			ArtefactModel artefactModel = null;
			if (selection instanceof IStructuredSelection) {
				artefactModel = (ArtefactModel) ((IStructuredSelection) selection).getFirstElement();
				artefactModel.getOwnedArtefacts().clear();
				// create artefact and set some attributes
				for (int i = 1; i <= nbVariantsForThread; i++) {
					Artefact a = ArtefactModelFactory.eINSTANCE.createArtefact();
					String variantName = VariantsUtils.VARIANT + "_" + i;
					String output_variant = paramDialog.getOutputPath() + File.separator + variantName;
					a.setName(variantName);
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

		dialog.open();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

}
