package org.but4reuse.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

/**
 * But4Reuse Perspective factory
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

		IFolderLayout bottom = factory.createFolder("bottomRight", // NON-NLS-1
				IPageLayout.BOTTOM, 0.75f, factory.getEditorArea());
		//bottom.addView("org.but4reuse.input.drop.view"); // NON-NLS-1
		//bottom.addView("org.eclipse.contribution.visualiser.views.Menu");
		//bottom.addView("org.eclipse.contribution.visualiser.views.Visualiser");
		bottom.addView("org.eclipse.ui.views.PropertySheet");
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottom.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);

		IFolderLayout topLeft = factory.createFolder("topLeft", // NON-NLS-1
				IPageLayout.LEFT, 0.25f, factory.getEditorArea());
		topLeft.addView(IPageLayout.ID_RES_NAV);

		// factory.addFastView("org.eclipse.team.ccvs.ui.RepositoriesView",0.50f);
		// //NON-NLS-1
		// factory.addFastView("org.eclipse.team.sync.views.SynchronizeView",
		// 0.50f); //NON-NLS-1
	}

	private void addActionSets() {
		factory.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET); // NON-NLS-1
	}

	private void addPerspectiveShortcuts() {
		// factory.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective"); // NON-NLS-1
	}

	private void addNewWizardShortcuts() {
		factory.addNewWizardShortcut("org.but4reuse.variantsmodel.presentation.VariantsModelModelWizardID");// NON-NLS-1
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");// NON-NLS-1
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");// NON-NLS-1
	}

	@SuppressWarnings("deprecation")
	private void addViewShortcuts() {
		factory.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
		factory.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		factory.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		// factory.addShowViewShortcut("org.but4reuse.input.drop.view");
		factory.addShowViewShortcut(IPageLayout.ID_OUTLINE);
	}

}
