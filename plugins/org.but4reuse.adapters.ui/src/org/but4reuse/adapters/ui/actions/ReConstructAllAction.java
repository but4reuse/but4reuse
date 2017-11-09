package org.but4reuse.adapters.ui.actions;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.utils.ui.dialogs.URISelectionDialog;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * Re-Construct All Action TODO add progress monitor
 * 
 * @author jabier.martinez
 */
public class ReConstructAllAction implements IViewActionDelegate {

	@Override
	public void run(IAction action) {
		try {
			// Get construction uri from user
			String out = "/projectName";
			IContainer output = AdaptedModelManager.getDefaultOutput();
			if (output != null) {
				out = output.getFullPath().toString();
			}
			URISelectionDialog inputDialog = new URISelectionDialog(Display.getCurrent().getActiveShell(),
					"Re-Construction URI", "Insert folder URI for the artefacts re-construction",
					"platform:/resource" + out + "/reconstruction/");
			if (inputDialog.open() != Dialog.OK) {
				return;
			}

			String constructionURI = inputDialog.getValue();
			AdaptedModel adaptedModel = AdaptedModelManager.getAdaptedModel();

			// construct each adapted artefact
			for (AdaptedArtefact aa : adaptedModel.getOwnedAdaptedArtefacts()) {

				// separate the elements by its adapter
				Map<String, List<IElement>> elementsMapByAdapter = new HashMap<String, List<IElement>>();
				Map<String, IAdapter> adaptersMap = new HashMap<String, IAdapter>();
				String name = AdaptedModelHelper.getArtefactName(aa.getArtefact());

				for (ElementWrapper ew : aa.getOwnedElementWrappers()) {
					Object oElement = ew.getElement();
					if (oElement instanceof IElement) {
						IElement ie = (IElement) oElement;
						IAdapter adapter = AdaptersHelper.getAdapter(ie);
						if (adapter != null) {
							String key = adapter.getClass().getName();
							adaptersMap.put(key, adapter);
							List<IElement> list = elementsMapByAdapter.get(key);
							if (list == null) {
								list = new ArrayList<IElement>();
							}
							list.add(ie);
							elementsMapByAdapter.put(key, list);
						} else {
							// TODO Report the error to the user
							System.out.println("No adapter declared for " + ie.getClass());
							break;
						}
					}
				}

				// here we have all the info to start the construction
				String artefactConstructionURI = constructionURI + name + "/";
				URI artefactURI = new URI(artefactConstructionURI);
				// Construct through adapter
				for (String key : adaptersMap.keySet()) {
					IAdapter adap = adaptersMap.get(key);
					adap.construct(artefactURI, elementsMapByAdapter.get(key), new NullProgressMonitor());
				}
			}

			// Refresh if platform uri
			URI configsURI = new URI(constructionURI);
			IResource r = WorkbenchUtils.getIResourceFromURI(configsURI);
			if (r != null) {
				WorkbenchUtils.refreshIResource(r);
			}
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
