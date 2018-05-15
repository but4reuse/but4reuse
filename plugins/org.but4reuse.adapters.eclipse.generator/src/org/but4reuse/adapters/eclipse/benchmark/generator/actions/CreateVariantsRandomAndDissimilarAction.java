package org.but4reuse.adapters.eclipse.benchmark.generator.actions;

import java.io.File;
import java.io.IOException;

import org.but4reuse.adapters.eclipse.benchmark.generator.VariantsRandomAndDissimilarGenerator;
import org.but4reuse.adapters.eclipse.benchmark.generator.dialogs.RandomAndDissimilarDialog;
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
public class CreateVariantsRandomAndDissimilarAction implements IListener, IObjectActionDelegate {

	private ISelection selection;
	private CreateVariantsRandomAndDissimilarAction context;

	public CreateVariantsRandomAndDissimilarAction() {
		super();
		this.context = this;
	}

	private SummaryDialog summaryDialog;
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
		new Thread(new Runnable() {

			@Override
			public void run() {
				VariantsRandomAndDissimilarGenerator varGen = new VariantsRandomAndDissimilarGenerator(dialog.getInputPath(),
						dialog.getOutputPath(), dialog.getGeneratorPath(), nbVariantsForThread, timeForThread, keepOnlyMetadata,
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
