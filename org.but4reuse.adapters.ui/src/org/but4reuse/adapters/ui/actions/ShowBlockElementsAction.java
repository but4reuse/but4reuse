package org.but4reuse.adapters.ui.actions;

import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adapters.IElement;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.but4reuse.visualisation.visualiser.BlockElementsMarkupProvider;
import org.but4reuse.visualisation.visualiser.BlockElementsOnArtefactsVisualisation;
import org.but4reuse.visualisation.visualiser.BlockMarkupKind;
import org.eclipse.contribution.visualiser.core.ProviderDefinition;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.views.Menu;
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
					String sText = "";
					for (BlockElement blockElement : block.getOwnedBlockElements()){
						IElement element = (IElement)blockElement.getElementWrappers().get(0).getElement();
						sText = sText + element.getText() + "\n";
					}
					
					// Remove the last \n
					if (sText.length() > 0) {
						sText = sText.substring(0, sText.length() - 1);
					}
					
					// Show
					// TODO Show also the artefacts where the block is present
					ScrollableMessageDialog dialog = new ScrollableMessageDialog(Display.getCurrent().getActiveShell(),
							markupKind.getName(), block.getOwnedBlockElements().size() + " Elements", sText);
					dialog.open();
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
