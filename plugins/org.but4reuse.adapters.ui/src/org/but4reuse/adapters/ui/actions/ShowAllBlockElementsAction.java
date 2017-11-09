package org.but4reuse.adapters.ui.actions;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.IElement;
import org.but4reuse.utils.strings.StringUtils;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.eclipse.contribution.visualiser.views.Menu;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * Show All Block Elements
 * 
 * @author jabier.martinez
 */
public class ShowAllBlockElementsAction implements IViewActionDelegate {

	Menu menu;

	@Override
	public void run(IAction action) {
		AdaptedModel adaptedModel = AdaptedModelManager.getAdaptedModel();
		if (adaptedModel != null) {
			StringBuilder sText = new StringBuilder();
			for (Block block : adaptedModel.getOwnedBlocks()) {
				sText.append("Block: ");
				sText.append(block.getName());
				sText.append("\n");
				for (BlockElement blockElement : block.getOwnedBlockElements()) {
					IElement element = (IElement) blockElement.getElementWrappers().get(0).getElement();
					sText.append(StringUtils.removeNewLines(element.getText()));
					sText.append("\n");
				}
				sText.append("\n");
			}
			// Show
			ScrollableMessageDialog dialog = new ScrollableMessageDialog(Display.getCurrent().getActiveShell(),
					"All Blocks", "", sText.toString());
			dialog.open();
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
