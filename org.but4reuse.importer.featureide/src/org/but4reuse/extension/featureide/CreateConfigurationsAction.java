package org.but4reuse.extension.featureide;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.utils.ui.dialogs.URISelectionDialog;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.but4reuse.visualisation.visualiser.adaptedmodel.BlockElementsContentProvider;
import org.but4reuse.visualisation.visualiser.adaptedmodel.BlockElementsMarkupProvider;
import org.but4reuse.visualisation.visualiser.adaptedmodel.BlockElementsOnArtefactsVisualisation;
import org.eclipse.contribution.visualiser.core.ProviderDefinition;
import org.eclipse.contribution.visualiser.core.Stripe;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.interfaces.IMember;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * Create configurations action
 * 
 * @author jabier.martinez
 */
public class CreateConfigurationsAction implements IViewActionDelegate {

	@Override
	public void run(IAction action) {
		try {
			// Get construction uri from user
			URISelectionDialog inputDialog = new URISelectionDialog(Display.getCurrent().getActiveShell(),
					"Construction URI", "Insert folder URI for the configurations",
					"platform:/resource/projectName/configs/");
			if (inputDialog.open() != Dialog.OK) {
				return;
			}

			String constructionURI = inputDialog.getValue();
			URI configsURI = new URI(constructionURI);
			Map<String, List<String>> configsMap = new HashMap<String, List<String>>();

			ProviderDefinition definition = BlockElementsOnArtefactsVisualisation.getBlockElementsOnVariantsProvider();
			BlockElementsMarkupProvider markupProvider = (BlockElementsMarkupProvider) definition.getMarkupInstance();
			BlockElementsContentProvider contentProvider = (BlockElementsContentProvider) definition
					.getContentProvider();

			List membersList = contentProvider.getAllMembers();
			for (Object memberO : membersList) {

				IMember member = (IMember) memberO;
				List<String> currentConfigFeatures = new ArrayList<String>();
				// when names are empty and uri is used we get the last part
				String memberName = member.getName();
				if (memberName.contains("/")) {
					memberName = memberName.substring(memberName.lastIndexOf("/") + 1, memberName.length());
				}
				configsMap.put(memberName, currentConfigFeatures);

				List markups = markupProvider.getMemberMarkups(member);
				for (Object o : markups) {
					Stripe stripe = (Stripe) o;
					List<IMarkupKind> kinds = stripe.getKinds();
					for (IMarkupKind kind : kinds) {
						if (!currentConfigFeatures.contains(kind.getFullName())) {
							currentConfigFeatures.add(kind.getFullName());
						}
					}
				}
			}

			FeatureIDEUtils.createConfigurations(configsURI, configsMap);
			WorkbenchUtils.refreshAllWorkspace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

	@Override
	public void init(IViewPart view) {

	}

}
