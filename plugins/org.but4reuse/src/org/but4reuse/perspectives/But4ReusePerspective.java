package org.but4reuse.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

/**
 * BUT4Reuse Perspective factory
 * 
 * @author jabier.martinez
 */
public class But4ReusePerspective implements IPerspectiveFactory {

	private IPageLayout factory;

	public But4ReusePerspective() {
		super();
	}

	public void createInitialLayout(IPageLayout factory) {
		this.factory = factory;
		addViews();
		addActionSets();
		addNewWizardShortcuts();
		addPerspectiveShortcuts();
		addViewShortcuts();
	}

	@SuppressWarnings("deprecation")
	private void addViews() {
		// Creates the overall folder layout.
		// Note that each new Folder uses a percentage of the remaining
		// EditorArea.

		IFolderLayout topLeft = factory.createFolder("topLeft", // NON-NLS-1
				IPageLayout.LEFT, 0.25f, factory.getEditorArea());
		topLeft.addView(IPageLayout.ID_RES_NAV);

		IFolderLayout bottomRight = factory.createFolder("bottomRight", // NON-NLS-1
				IPageLayout.BOTTOM, 0.75f, factory.getEditorArea());

		IFolderLayout bottomLeft = factory.createFolder("bottomLeft", // NON-NLS-1
				IPageLayout.LEFT, 0.75f, "bottomRight");

		bottomLeft.addView("org.but4reuse.input.inputdropview"); // NON-NLS-1

		bottomLeft.addView("org.eclipse.ui.views.PropertySheet");// NON-NLS-1
		bottomLeft.addView(IPageLayout.ID_PROBLEM_VIEW);

		bottomRight.addView("org.eclipse.contribution.visualiser.views.Menu");// NON-NLS-1
		bottomLeft.addView("org.eclipse.contribution.visualiser.views.Visualiser");// NON-NLS-1

		bottomLeft.addView("org.but4reuse.feature.constraints.ui.view");// NON-NLS-1
		bottomLeft.addView("org.but4reuse.visualisation.metrics");
		bottomLeft.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);
	}

	private void addActionSets() {
		factory.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET); // NON-NLS-1
	}

	private void addPerspectiveShortcuts() {
		// factory.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective");
		// // NON-NLS-1
	}

	private void addNewWizardShortcuts() {
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.project");// NON-NLS-1
		factory.addNewWizardShortcut("org.but4reuse.artefactmodel.presentation.ArtefactModelModelWizardID");// NON-NLS-1
		factory.addNewWizardShortcut("org.but4reuse.featurelist.presentation.FeatureListModelWizardID");// NON-NLS-1
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");// NON-NLS-1
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");// NON-NLS-1
	}

	@SuppressWarnings("deprecation")
	private void addViewShortcuts() {
		factory.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
		factory.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		factory.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		factory.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		factory.addShowViewShortcut("org.but4reuse.input.inputdropview");// NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.ui.views.PropertySheet");// NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.contribution.visualiser.views.Menu");// NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.contribution.visualiser.views.Visualiser");// NON-NLS-1
		factory.addShowViewShortcut("org.but4reuse.feature.constraints.ui.view");// NON-NLS-1
		factory.addShowViewShortcut("org.but4reuse.visualisation.metrics");// NON-NLS-1
	}

}
