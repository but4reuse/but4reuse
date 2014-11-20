package org.but4reuse.importer.featureide;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Hashtable;

import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.featurelist.FeatureListFactory;
import org.but4reuse.utils.ui.dialogs.URISelectionDialog;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.core.io.FeatureModelReaderIFileWrapper;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelReader;

/**
 * Import features
 * @author jabier.martinez
 * TODO set as modified
 * TODO associate features to configurations
 */
public class ImportFeatureIDEAction implements IObjectActionDelegate {

	@Override
	public void run(IAction action) {
		// Get current featureList
		if (selection instanceof IStructuredSelection) {
			Object featureListObject = ((IStructuredSelection) selection).getFirstElement();
			if (featureListObject instanceof FeatureList) {
				FeatureList featureList = ((FeatureList) featureListObject);

				// Select the model
				URISelectionDialog inputDialog = new URISelectionDialog(Display.getCurrent().getActiveShell(),
						"Feature Model from FeatureIDE", "Insert feature model xml file uri",
						"platform:/resource/projectName/");
				if (inputDialog.open() != Dialog.OK) {
					return;
				}
				String fmURIString = inputDialog.getValue();
				URI fmURI = null;
				try {
					fmURI = new URI(fmURIString);
				} catch (Exception e) {
					e.printStackTrace();
				}
				IFile fmifile = WorkbenchUtils.getIFileFromURI(fmURI);
				FeatureModel featureModel = new FeatureModel();
				FeatureModelReaderIFileWrapper modelReader = new FeatureModelReaderIFileWrapper(new XmlFeatureModelReader(featureModel));
				try {
					modelReader.readFromFile(fmifile);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (UnsupportedModelException e) {
					e.printStackTrace();
				}
				Hashtable<String, de.ovgu.featureide.fm.core.Feature> table = featureModel.getFeatureTable();

				for(String fID : table.keySet()){
					de.ovgu.featureide.fm.core.Feature f = table.get(fID);
					Feature feat = FeatureListFactory.eINSTANCE.createFeature();
					feat.setId(fID);
					feat.setName(f.getName());
					feat.setDescription(f.getDescription());
					featureList.getOwnedFeatures().add(feat);
				}
			}
		}
	}

	ISelection selection;

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

}
