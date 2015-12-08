package org.but4reuse.fca.visualisation;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.fca.utils.FCAUtils;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.but4reuse.visualisation.IVisualisation;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;

import com.googlecode.erca.clf.ConceptLattice;
import com.googlecode.erca.clf.ConceptLatticeFamily;
import com.googlecode.erca.framework.io.out.ClfToDot;
import com.googlecode.erca.framework.io.out.LatticeToDot;

/**
 * Visualisation
 * 
 * @author jabier.martinez
 * 
 */
public class FCAVisualisation implements IVisualisation {

	@Override
	public void prepare(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor) {

		monitor.subTask("Saving formal context analysis visualisations");

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
		File folder = new File(artefactModelFile.getParentFile(), "formalContextAnalysis");
		folder.mkdir();

		// Save conceptLattice
		ConceptLattice cl = FCAUtils.createConceptLattice(FCAUtils.createArtefactsBlocksFormalContext(adaptedModel));
		// Save
		File file = new File(folder, artefactModelFile.getName() + "_conceptLattice.dot");
		LatticeToDot dot = new LatticeToDot(cl);
		dot.generateCode();
		try {
			dot.toFile(file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// If we have feature list then Concept Lattice Family
		if (featureList != null) {
			ConceptLatticeFamily clf = FCAUtils.createArtefactsBlocksFeaturesConceptLatticeFamily(featureList,
					adaptedModel);

			// Save
			File file2 = new File(folder, artefactModelFile.getName() + "_conceptLatticeFamily.dot");
			ClfToDot dot2 = new ClfToDot(clf);
			dot2.generateCode();
			try {
				dot2.toFile(file2.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void show() {
		// export is on prepare method
	}

}
