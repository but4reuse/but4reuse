package org.but4reuse.adapters.eclipse.benchmark.actions;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;
import org.but4reuse.adapters.eclipse.benchmark.FeatureHelper;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.featurelist.FeatureListFactory;
import org.but4reuse.utils.emf.EMFUtils;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Actual Features Action
 * 
 * @author jabier.martinez
 */
public class ActualFeaturesAction implements IObjectActionDelegate {

	private ISelection selection;

	@Override
	public void run(IAction action) {
		if (selection instanceof IStructuredSelection) {
			Object art = ((IStructuredSelection) selection).getFirstElement();
			if (art instanceof ArtefactModel) {
				ArtefactModel artefactModel = ((ArtefactModel) art);
				List<ActualFeature> features = new ArrayList<ActualFeature>();
				for (Artefact a : AdaptersHelper.getActiveArtefacts(artefactModel)) {
					// example of uri
					// file:/C:/keplerPackages/eclipse-cpp-kepler-SR1-win32-x86_64/eclipse/
					List<ActualFeature> currentFeatures = FeatureHelper.getFeaturesOfEclipse(a.getArtefactURI());
					for (ActualFeature af : currentFeatures) {
						int index = features.indexOf(af);
						if (index == -1) {
							af.getArtefacts().add(a);
							features.add(af);
						} else {
							ActualFeature p = features.get(index);
							p.getArtefacts().add(a);
						}
					}
				}

				// Sort list
				Collections.sort(features);

				String name = artefactModel.getName();
				if (name == null || name.isEmpty()) {
					name = "eclipse";
				}

				// Create the feature list
				FeatureList fl = FeatureListFactory.eINSTANCE.createFeatureList();
				fl.setName("eclipse_" + name);
				fl.setArtefactModel(artefactModel);

				for (ActualFeature af : features) {
					Feature f = FeatureListFactory.eINSTANCE.createFeature();
					f.setId(af.getId());
					f.setName(af.getName());
					f.setDescription(af.getDescription());
					f.getImplementedInArtefacts().addAll(af.getArtefacts());
					fl.getOwnedFeatures().add(f);
				}

				// Save feature list
				IResource res = EMFUtils.getIResource(artefactModel.eResource());
				IContainer container = res.getParent();
				try {
					EMFUtils.saveEObject(new URI(container.getLocationURI() + "/eclipse_" + name + ".featurelist"), fl);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Save the plugins of each feature
				try {
					for (ActualFeature af : features) {
						File fileF = new File(new URI(container.getLocationURI() + "/actualFeatures/" + af.getId()
								+ ".txt"));
						if (!fileF.exists()) {
							FileUtils.createFile(fileF);
						}
						StringBuilder sb = new StringBuilder();
						for (String plu : af.getPlugins()) {
							sb.append(plu + "\n");
						}
						// remove last \n
						if (sb.length() > 0) {
							sb.setLength(sb.length() - 1);
						}
						FileUtils.writeFile(fileF, sb.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}


				
				// Refresh
				WorkbenchUtils.refreshIResource(container);
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

}
