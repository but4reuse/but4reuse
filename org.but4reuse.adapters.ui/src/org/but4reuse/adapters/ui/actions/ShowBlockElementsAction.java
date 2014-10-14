package org.but4reuse.adapters.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.but4reuse.visualisation.visualiser.BlockElementsMarkupProvider;
import org.but4reuse.visualisation.visualiser.BlockElementsOnArtefactsVisualisation;
import org.eclipse.contribution.visualiser.core.ProviderDefinition;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleMarkupKind;
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
				// TODO get the text of the Block elements
				if (kind instanceof SimpleMarkupKind) {
					SimpleMarkupKind markupKind = (SimpleMarkupKind) kind;
					List<IElement> elements = new ArrayList<IElement>(); // markupKind.getElements();
					String sText = "";

					// Show
					// TODO get block name
					ScrollableMessageDialog dialog = new ScrollableMessageDialog(Display.getCurrent().getActiveShell(),
							"Block", elements.size() + " Elements", sText);
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
