package org.but4reuse.adapters.eclipse.generator.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.but4reuse.adapters.eclipse.generator.VariantsGenerator;
import org.but4reuse.adapters.eclipse.generator.dialogs.ParametersDialog;
import org.but4reuse.adapters.eclipse.generator.dialogs.SummaryDialog;
import org.but4reuse.adapters.eclipse.generator.utils.IListener;
import org.but4reuse.adapters.eclipse.generator.utils.PreferenceUtils;
import org.but4reuse.adapters.eclipse.generator.utils.VariantsUtils;
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

public class CreateEclipseVariantsAction implements IListener, IObjectActionDelegate {

	private ISelection selection;
	private CreateEclipseVariantsAction context;

	public CreateEclipseVariantsAction() {
		super();
		this.context = this;
	}

	private SummaryDialog summaryDialog;
	private ParametersDialog paramDialog;
	private Map<String, String> prefMap;

	public void run(IAction action) {

		if (paramDialog == null) {
			// Not create a new dialog if it's a "re-open" (parameters not
			// good).
			paramDialog = new ParametersDialog(Display.getCurrent().getActiveShell());
			try { // Load preferences
				prefMap = PreferenceUtils.getPreferencesMap();
				if (prefMap.containsKey(PreferenceUtils.PREF_USERNAME)
						&& prefMap.get(PreferenceUtils.PREF_USERNAME).equals(
								System.getProperty(PreferenceUtils.PREF_USERNAME))) {
					// Look below, in registration
					paramDialog.addPreferenceParameters(prefMap);
				}
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				if (e instanceof FileNotFoundException)
					System.out.println("Error for loading preferences");
			}
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

		// Saving preferences
		try {
			PreferenceUtils.savePreferencesMap(paramDialog.getInputPath(), paramDialog.getOutputPath(),
					paramDialog.getRandomSelector(), paramDialog.getVariantsNumber());
		} catch (IOException e) {
			System.out.println("Error for saving preferences");
			e.printStackTrace();
		}

		// Start the generator process
		// final for the thread and because nbVariants and valRand can't be
		// final
		final int nbVariantsForThread = nbVariants;
		final int valRandForThread = valRand;
		new Thread(new Runnable() {

			@Override
			public void run() {
				VariantsGenerator varGen = new VariantsGenerator(paramDialog.getInputPath(),
						paramDialog.getOutputPath(), nbVariantsForThread, valRandForThread);
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

		// get the selection
		ArtefactModel artefactModel = null;
		if (selection instanceof IStructuredSelection) {
			artefactModel = (ArtefactModel) ((IStructuredSelection) selection).getFirstElement();
			artefactModel.getOwnedArtefacts().clear();
			// create artefact and set some attributes
			for (int i = 1; i <= nbVariantsForThread; i++) {
				Artefact a = ArtefactModelFactory.eINSTANCE.createArtefact();
				String output_variant = paramDialog.getOutputPath() + File.separator + VariantsUtils.VARIANT + i;
				a.setName("Variant" + i);
				a.setArtefactURI(new File(output_variant).toURI().toString());
				// add to the artefact model
				artefactModel.getOwnedArtefacts().add(a);
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
