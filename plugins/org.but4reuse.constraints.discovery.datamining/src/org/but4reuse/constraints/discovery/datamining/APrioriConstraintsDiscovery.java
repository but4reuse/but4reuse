package org.but4reuse.constraints.discovery.datamining;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.constraints.discovery.datamining.utils.ArffUtils;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.IConstraintsDiscovery;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;

/**
 * Apriori association rules
 * 
 * @author jabier.martinez
 */
public class APrioriConstraintsDiscovery implements IConstraintsDiscovery {

	public List<IConstraint> discover(FeatureList featureList, final AdaptedModel adaptedModel, Object extra,
			IProgressMonitor monitor) {
		monitor.subTask("Constraints discovery with APriori association rules learner");

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
		File dataminingFolder = new File(artefactModelFile.getParentFile(), "dataminingFolder");
		dataminingFolder.mkdir();

		String arffFileContent = ArffUtils.createArffFileContent(adaptedModel, false);
		File data = new File(dataminingFolder, "data.arff");
		try {
			FileUtils.writeFile(data, arffFileContent);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Refresh
		WorkbenchUtils.refreshIResource(res.getParent());

		// TODO we rely on another tool
		List<IConstraint> constraints = new ArrayList<IConstraint>();
		return constraints;
	}

}
