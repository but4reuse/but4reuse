package org.but4reuse.adapters.eclipse.benchmark.visualisations;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.eclipse.PluginElement;
import org.but4reuse.adapters.eclipse.benchmark.PrecisionRecall;
import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.feature.location.LocatedFeature;
import org.but4reuse.feature.location.LocatedFeaturesManager;
import org.but4reuse.feature.location.LocatedFeaturesUtils;
import org.but4reuse.feature.location.helper.FeatureLocationHelper;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.emf.EMFUtils;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.but4reuse.visualisation.IVisualisation;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * List of plugins per feature
 * 
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
		if (featureList != null && featureList.getName() != null && featureList.getName().contains("eclipse")) {

			// TODO improve getting this resource
			IResource res = EMFUtils
					.getIResource(adaptedModel.getOwnedAdaptedArtefacts().get(0).getArtefact().eResource());
			File artefactModelFile = WorkbenchUtils.getFileFromIResource(res);

			// create folder
			File results = new File(artefactModelFile.getParentFile(), "benchmark_results");
			results.mkdir();

			IFeatureLocation algoUsed = FeatureLocationHelper.getSelectedFeatureLocation();
			File currentResults = new File(results,
					algoUsed.getClass().getSimpleName() + "_" + System.currentTimeMillis());
			currentResults.mkdir();

			File flocationsFolder = new File(currentResults, "retrievedFeatureLocations");
			flocationsFolder.mkdir();

			// put the calculated feature locations in one file per feature
			for (Feature feature : featureList.getOwnedFeatures()) {
				StringBuilder text = new StringBuilder();
				File file = new File(flocationsFolder, feature.getId() + ".txt");

				List<LocatedFeature> locatedFeatures = LocatedFeaturesManager.getLocatedFeatures();

				// Add plugins of whole blocks
				List<Block> blocks = LocatedFeaturesUtils.getBlocksOfFeature(locatedFeatures, feature);
				for (Block b : blocks) {
					for (BlockElement be : b.getOwnedBlockElements()) {
						Object o = be.getElementWrappers().get(0).getElement();
						if (o instanceof PluginElement) {
							text.append(((PluginElement) o).getSymbName() + "\n");
						}
					}
				}

				// Add plugins
				List<IElement> plugins = LocatedFeaturesUtils.getElementsOfFeature(locatedFeatures, feature);
				for (IElement element : plugins) {
					if (element instanceof PluginElement) {
						text.append(((PluginElement) element).getSymbName() + "\n");
					}
				}

				// remove last \n
				if (text.length() > 0) {
					text.setLength(text.length() - 1);
				}
				try {
					FileUtils.writeFile(file, text.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// Create precision and recall file
			File actualFeatures = new File(artefactModelFile.getParentFile(), "benchmark/actualFeatures");
			if (actualFeatures.exists()) {
				String content = PrecisionRecall.createResultsFile(actualFeatures, flocationsFolder);
				try {
					FileUtils.writeFile(new File(currentResults, "metrics.csv"), content);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// Create time measures file
			StringBuilder text = new StringBuilder();
			for (Entry<String, Long> entry : AdaptedModelManager.getElapsedTimeRegistry().entrySet()) {
				text.append(entry.getKey() + ";" + entry.getValue() + "\n");
			}
			try {
				FileUtils.writeFile(new File(currentResults, "timeMeasures.csv"), text.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Refresh
			WorkbenchUtils.refreshIResource(res.getParent());
		}
	}

}
