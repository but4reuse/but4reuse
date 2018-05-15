package org.but4reuse.adapters.eclipse.benchmark.generator.actions;

import java.io.File;
import java.io.IOException;

import org.but4reuse.adapters.eclipse.benchmark.generator.VariantsPercentageBasedGenerator;
import org.but4reuse.adapters.eclipse.benchmark.generator.dialogs.ParametersDialog;
import org.but4reuse.adapters.eclipse.benchmark.generator.dialogs.SummaryDialog;
import org.but4reuse.adapters.eclipse.benchmark.generator.interfaces.IListener;
import org.but4reuse.adapters.eclipse.benchmark.generator.utils.VariantsUtils;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.artefactmodel.ArtefactModelFactory;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Julien Margarido
 * @author Felix Lima Gorito
 */
public class CreateVariantsAction implements IListener, IObjectActionDelegate {

	private ISelection selection;
	private CreateVariantsAction context;

	public CreateVariantsAction() {
		super();
		this.context = this;
	}

	private SummaryDialog summaryDialog;
	private ParametersDialog paramDialog;

	public void run(IAction action) {

		if (paramDialog == null) {
			paramDialog = new ParametersDialog(Display.getCurrent().getActiveShell());
		}

		if (paramDialog.open() != Window.OK) {
			// Open the dialog and stop execution while a button is not pressed
			paramDialog = null;
			return;
		}

		// Settings checking
		int valRand = 0;
		int nbVariants = 0;
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
			e.printStackTrace();
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
			e.printStackTrace();
		}

		if (!isAllOK) {
			this.run(action);
			return;
		}

		// Start the generator process
		// final for the thread and because nbVariants and valRand can't be
		// final
		final int nbVariantsForThread = nbVariants;
		final int valRandForThread = valRand;
		final boolean keepOnlyMetadata = paramDialog.isKeepOnlyMetadata();
		final boolean noOutputOnlyStatistics = paramDialog.isNoOutputOnlyStatistics();
		new Thread(new Runnable() {

			@Override
			public void run() {
				VariantsPercentageBasedGenerator varGen = new VariantsPercentageBasedGenerator(paramDialog.getInputPath(),
						paramDialog.getOutputPath(), nbVariantsForThread, valRandForThread, keepOnlyMetadata,
						noOutputOnlyStatistics);
				varGen.addListener(context);
				// Long time to execute
				varGen.generate();
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						waitWhileParameterIsNull(summaryDialog);
						if (!summaryDialog.isDisposed())
							summaryDialog.setCloseable(true);
					}

				});
			}
		}, "Variant Generator Thread").start();

		final Shell shell = Display.getCurrent().getActiveShell();

		// Open the Summary Dialog
		try {
			summaryDialog = new SummaryDialog(shell, "Summary", null, "", false);
			summaryDialog.open();
			synchronized (this) {
				this.notifyAll();
			} // Security for not make things on a null dialog in receive method
		} catch (Exception e) {
			MessageDialog.openError(shell, "Error in summary dialog", e.toString());
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
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	@Override
	public void receive(final String msg) {
		if (msg != null) {
			Display.getDefault().syncExec(new Runnable() {
				public void run() {

					waitWhileParameterIsNull(summaryDialog);

					if (!summaryDialog.isDisposed()) {
						String scrollText = summaryDialog.getScrollableText();
						if (scrollText == null)
							scrollText = "";
						scrollText += msg.replaceAll("\n", "\r\n") + "\r\n";

						summaryDialog.setScrollableText(scrollText);
					}
				}
			});
		}
	}

	/**
	 * This method interrupt the current thread while the parameter is null
	 */
	private <T> void waitWhileParameterIsNull(T param) {
		// If dialog is null, we wait.
		if (param == null) {
			synchronized (this) {
				try {
					while (param == null) {
						// Double checking method
						this.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
