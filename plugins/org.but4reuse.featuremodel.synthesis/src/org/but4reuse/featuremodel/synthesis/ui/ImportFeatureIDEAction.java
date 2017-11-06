package org.but4reuse.featuremodel.synthesis.ui;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.artefactmodel.ArtefactModelFactory;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.featurelist.FeatureListFactory;
import org.but4reuse.featurelist.helpers.FeatureListHelper;
import org.but4reuse.featuremodel.synthesis.utils.FeatureIDEUtils;
import org.but4reuse.utils.emf.EMFUtils;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.ui.dialogs.URISelectionDialog;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import de.ovgu.featureide.fm.core.base.FeatureUtils;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;

/**
 * Import features and links from features to configurations
 * 
 * @author jabier.martinez
 */
public class ImportFeatureIDEAction implements IObjectActionDelegate {

	@Override
	public void run(IAction action) {
		if (selection instanceof IStructuredSelection) {
			Object featureListObject = ((IStructuredSelection) selection).getFirstElement();
			if (featureListObject instanceof FeatureList) {
				FeatureList featureList = ((FeatureList) featureListObject);

				ArtefactModel artefactModel = FeatureListHelper.getArtefactModel(featureList);
				if (artefactModel == null) {
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error",
							"No linked artefact model.\nPlease, create and load an artefact model in this editor before");
					return;
				}
				if (featureList.getArtefactModel() == null) {
					featureList.setArtefactModel(artefactModel);
				}
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

				IFile fmifile = (IFile) WorkbenchUtils.getIResourceFromURI(fmURI);
				File fmfile = fmifile.getRawLocation().makeAbsolute().toFile();
				FeatureModel featureModel = FeatureIDEUtils.load(fmfile);
				Map<String, IFeature> table = featureModel.getFeatureTable();

				// Create/update features
				for (String fID : table.keySet()) {
					IFeature f = table.get(fID);
					Feature feat = FeatureListHelper.getFeature(featureList, fID);
					if (feat == null) {
						// it did not exist, create it
						feat = FeatureListFactory.eINSTANCE.createFeature();
						featureList.getOwnedFeatures().add(feat);
					}
					feat.setId(fID);
					feat.setName(f.getName());
					feat.setDescription(FeatureUtils.getDescription(f));
				}

				// Associate features to artefacts
				// TODO get project information about configs folder path.
				// configs is only by default
				IResource res = fmifile.getProject().findMember("configs");
				if (res.exists() && res instanceof IFolder) {
					IFolder resf = (IFolder) res;
					try {
						for (IResource a : resf.members()) {
							if (a.getFileExtension() != null && a.getFileExtension().equals("config")) {
								// create artefact
								Artefact artefact = ArtefactModelFactory.eINSTANCE.createArtefact();
								artefact.setName(a.getName().substring(0, a.getName().lastIndexOf(".")));
								artefactModel.getOwnedArtefacts().add(artefact);

								File file = WorkbenchUtils.getFileFromIResource(a);
								List<String> lines = FileUtils.getLinesOfFile(file);
								for (String fidline : lines) {
									for (Feature f : featureList.getOwnedFeatures()) {
										if (f.getId() != null && f.getId().equals(fidline)) {
											f.getImplementedInArtefacts().add(artefact);
											break;
										}
									}
								}
							}
						}
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
				EMFUtils.saveResource(artefactModel.eResource());
				EMFUtils.saveResource(featureList.eResource());
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
