package org.but4reuse.adapters.ui.actions;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.IElement;
import org.but4reuse.utils.strings.StringUtils;
import org.but4reuse.visualisation.helpers.VisualisationsHelper;
import org.but4reuse.visualisation.impl.visualiser.adaptedmodel.BlockElementsMarkupProvider;
import org.but4reuse.visualisation.impl.visualiser.adaptedmodel.BlockElementsOnArtefactsVisualisation;
import org.but4reuse.visualisation.impl.visualiser.adaptedmodel.BlockMarkupKind;
import org.eclipse.contribution.visualiser.core.ProviderDefinition;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.views.Menu;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * @author jabier.martinez
 */
public class ShowBlockElementsAction implements IViewActionDelegate {

	Menu menu;

	@Override
	public void run(IAction action) {
		ProviderDefinition definition = BlockElementsOnArtefactsVisualisation.getBlockElementsOnVariantsProvider();
		BlockElementsMarkupProvider markupProvider = (BlockElementsMarkupProvider) definition.getMarkupInstance();

		for (Object o : markupProvider.getAllMarkupKinds()) {
			IMarkupKind kind = (IMarkupKind) o;
			if (menu.getActive(kind)) {
				// get the text of the Block elements
				if (kind instanceof BlockMarkupKind) {
					BlockMarkupKind markupKind = (BlockMarkupKind) kind;
					Block block = markupKind.getBlock();
					StringBuilder sText = new StringBuilder();
					for (BlockElement blockElement : block.getOwnedBlockElements()) {
						IElement element = (IElement) blockElement.getElementWrappers().get(0).getElement();
						sText.append(StringUtils.removeNewLines(element.getText()));
						sText.append("\n");
					}

					// Remove the last \n
					if (sText.length() > 0) {
						sText.setLength(sText.length() - 1);
					}

					// Show
					// TODO Show also the artefacts where the block is present
					ScrollableMessageChangeNameDialog dialog = new ScrollableMessageChangeNameDialog(
							Display.getCurrent().getActiveShell(), markupKind.getName(),
							block.getOwnedBlockElements().size() + " Elements", sText.toString());
					dialog.open();
					if (dialog.name != null && !dialog.name.equals(block.getName())) {
						block.setName(dialog.name);
						AdaptedModel adaptedModel = (AdaptedModel) block.eContainer();
						// TODO feature model!
						VisualisationsHelper.notifyVisualisations(AdaptedModelManager.getFeatureList(), adaptedModel,
								null, new NullProgressMonitor());
					}
				}
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

	@Override
	public void init(IViewPart view) {
		menu = (Menu) view;
	}

}
