package org.but4reuse.fca.visualisation;

import java.io.File;
import java.io.IOException;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.fca.utils.FCAUtils;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.but4reuse.visualisation.IVisualisation;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IProgressMonitor;

import fr.labri.galatea.ConceptOrder;
import fr.labri.galatea.Context;
import fr.labri.galatea.io.GenerateDot;
import fr.labri.galatea.io.GenerateHTML;

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
		IContainer output = AdaptedModelManager.getDefaultOutput();
		File outputFile = WorkbenchUtils.getFileFromIResource(output);
		String name = AdaptedModelHelper.getName(adaptedModel);
		if (name == null) {
			name = "default";
		}
		
		// create folder
		File folder = new File(outputFile, "formalContextAnalysis");
		folder.mkdir();

		// Save conceptLattice
		Context fc = FCAUtils.createArtefactsBlocksFormalContext(adaptedModel);
		ConceptOrder cl = FCAUtils.createConceptLattice(fc);
		// Save
		File file = new File(folder, name + "_artefactsBlocksConceptLattice.dot");
		File fil = new File(folder, name + "_artefactsBlocks.html");
		GenerateDot dot = new GenerateDot(cl);
		GenerateHTML html = new GenerateHTML(fc);
		dot.generateCode();
		html.generateCode();
		try {
			dot.toFile(file.getAbsolutePath());
			html.toFile(fil.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// If we have feature list
		if (featureList != null) {
			ConceptOrder cl2 = FCAUtils.createConceptLattice(FCAUtils
					.createArtefactsFeaturesFormalContext(featureList));
			File file2 = new File(folder, name + "_artefactsFeaturesConceptLattice.dot");
			GenerateDot dot2 = new GenerateDot(cl2);
			dot2.generateCode();
			try {
				dot2.toFile(file2.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}

			ConceptOrder cl3 = FCAUtils.createConceptLattice(FCAUtils.createArtefactsFeaturesAndBlocksFormalContext(
					featureList, adaptedModel));
			File file3 = new File(folder, name + "_artefactsFeaturesBlocksConceptLattice.dot");
			GenerateDot dot3 = new GenerateDot(cl3);
			dot3.generateCode();
			try {
				dot3.toFile(file3.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Refresh
		WorkbenchUtils.refreshIResource(output);
	}

	@Override
	public void show() {
		// export is on prepare method
	}

}
