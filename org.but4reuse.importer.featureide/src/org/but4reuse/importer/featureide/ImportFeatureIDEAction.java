package org.but4reuse.importer.featureide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.artefactmodel.ArtefactModelFactory;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.featurelist.FeatureListFactory;
import org.but4reuse.utils.emf.EMFUtils;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.ui.dialogs.URISelectionDialog;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
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
 * 
 * @author jabier.martinez
 */
public class ImportFeatureIDEAction implements IObjectActionDelegate {

	@Override
	public void run(IAction action) {
		// Get current featureList
		if (selection instanceof IStructuredSelection) {
			Object featureListObject = ((IStructuredSelection) selection).getFirstElement();
			if (featureListObject instanceof FeatureList) {
				FeatureList featureList = ((FeatureList) featureListObject);
				org.eclipse.emf.common.util.URI artefactModelURI = featureList.eResource().getResourceSet().getResources().get(1).getURI();
				ArtefactModel artefactModel = null;
				try {
					artefactModel = (ArtefactModel)EMFUtils.getEObject(new URI(artefactModelURI.toString()));
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
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
				IFile fmifile = WorkbenchUtils.getIFileFromURI(fmURI);
				FeatureModel featureModel = new FeatureModel();
				FeatureModelReaderIFileWrapper modelReader = new FeatureModelReaderIFileWrapper(
						new XmlFeatureModelReader(featureModel));
				try {
					modelReader.readFromFile(fmifile);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (UnsupportedModelException e) {
					e.printStackTrace();
				}
				Hashtable<String, de.ovgu.featureide.fm.core.Feature> table = featureModel.getFeatureTable();

				// Create features
				for (String fID : table.keySet()) {
					de.ovgu.featureide.fm.core.Feature f = table.get(fID);
					Feature feat = FeatureListFactory.eINSTANCE.createFeature();
					feat.setId(fID);
					feat.setName(f.getName());
					feat.setDescription(f.getDescription());
					featureList.getOwnedFeatures().add(feat);
				}

				// Associate features to artefacts
				IResource res = fmifile.getProject().findMember("configs");
				if (res.exists() && res instanceof IFolder) {
					IFolder resf = (IFolder) res;
					try {
						for (IResource a : resf.members()) {
							if (a.getFileExtension() != null && a.getFileExtension().equals("config")) {
								// create artefact
								Artefact artefact = ArtefactModelFactory.eINSTANCE.createArtefact();
								artefact.setName(a.getName().substring(0,a.getName().lastIndexOf(".")));
								artefactModel.getOwnedArtefacts().add(artefact);
								
								File file = WorkbenchUtils.getFileFromIResource(a);
								List<String> lines = FileUtils.getLinesOfFile(file);
								for(String fidline : lines){
									for(Feature f : featureList.getOwnedFeatures()){
										if(f.getId()!=null && f.getId().equals(fidline)){
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
				saveResource(artefactModel.eResource());
				saveResource(featureList.eResource());
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
	
	  public static void saveResource(Resource resource) {
		     Map saveOptions = ((XMLResource)resource).getDefaultSaveOptions();
		     saveOptions.put(XMLResource.OPTION_CONFIGURATION_CACHE, Boolean.TRUE);
		     saveOptions.put(XMLResource.OPTION_USE_CACHED_LOOKUP_TABLE, new ArrayList());
		     try {
		        resource.save(saveOptions);
		     } catch (IOException e) {
		        throw new RuntimeException(e);
		     }
		  }

}
