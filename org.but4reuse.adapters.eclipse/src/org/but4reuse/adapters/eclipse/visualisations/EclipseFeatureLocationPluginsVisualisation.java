package org.but4reuse.adapters.eclipse.visualisations;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adapters.eclipse.PluginElement;
import org.but4reuse.feature.constraints.impl.ConstraintsHelper;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.but4reuse.visualisation.IVisualisation;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;

/**
 * List of plugins per feature
 * @author jabier.martinez
 */
public class EclipseFeatureLocationPluginsVisualisation implements IVisualisation {

	FeatureList featureList;
	AdaptedModel adaptedModel;

	@Override
	public void prepare(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor) {
		this.featureList = featureList;
		this.adaptedModel = adaptedModel;
	}

	@Override
	public void show() {
		// TODO modify visualisation extension to allow adapter specific
		// visualisation.
		if (featureList != null && featureList.getName()!=null && featureList.getName().contains("Eclipse")) {
			// TODO improve checks!
			// Here we try to find the folder to save it
			URI uri = adaptedModel.getOwnedAdaptedArtefacts().get(0).getArtefact().eResource().getURI();
			java.net.URI uri2 = null;
			try {
				uri2 = new java.net.URI(uri.toString());
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
			IResource res = WorkbenchUtils.getIResourceFromURI(uri2);
			File artefactModelFile = WorkbenchUtils.getFileFromIResource(res);

			// create folder
			File newFolder = new File(artefactModelFile.getParentFile(), "eclipseFeatureLocations");
			newFolder.mkdir();

			for (Feature feature : featureList.getOwnedFeatures()) {
				StringBuilder text = new StringBuilder();
				File file = new File(newFolder, feature.getName() + ".txt");
				List<Block> blocks = ConstraintsHelper.getCorrespondingBlocks(adaptedModel, feature);
				for (Block b : blocks) {
					for(BlockElement be : b.getOwnedBlockElements()){
						Object o = be.getElementWrappers().get(0).getElement();
						if(o instanceof PluginElement){
							text.append(((PluginElement)o).getPluginSymbName() + "\n");
						}
					}
				}
				// remove last \n
				if(text.length()>0){
					text.setLength(text.length()-1);
				}
				try {
					FileUtils.writeFile(file, text.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			// Refresh
			WorkbenchUtils.refreshIResource(res.getParent());
		}
	}

}
