package org.but4reuse.visualisation.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.feature.constraints.impl.ConstraintsHelper;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.but4reuse.visualisation.IVisualisation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Metrics visualisation
 * 
 * @author jabier.martinez
 */
public class MetricsVisualisation implements IVisualisation {
	FeatureList featureList;
	AdaptedModel adaptedModel;

	@Override
	public void prepare(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor) {
		this.featureList = featureList;
		this.adaptedModel = adaptedModel;
		monitor.subTask("Metrics Visualisation");
	}

	@Override
	public void show() {
		// asyncExec to avoid SWT invalid thread access
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				StringBuilder text = new StringBuilder();
				// General metrics of the Adapted model
				text.append("Artefacts= " + adaptedModel.getOwnedAdaptedArtefacts().size() + "\n");
				List<Double> nElementsPerArtefact = new ArrayList<Double>();
				for (AdaptedArtefact aa : adaptedModel.getOwnedAdaptedArtefacts()) {
					double nElements = aa.getOwnedElementWrappers().size();
					nElementsPerArtefact.add(nElements);
				}
				addMetrics(text, "Number of Elements per Artefact", nElementsPerArtefact);
				
				text.append("\n\nBlocks= " + adaptedModel.getOwnedBlocks().size() + "\n");
				List<Double> nElementsPerBlock = new ArrayList<Double>();
				for (Block block : adaptedModel.getOwnedBlocks()) {
					double nElements = block.getOwnedBlockElements().size();
					nElementsPerBlock.add(nElements);
				}
				addMetrics(text, "Number of Elements per Block", nElementsPerBlock);

				text.append("\n\nBlock Constraints= " + ConstraintsHelper.getCalculatedConstraints(adaptedModel).size());
				
				appendBlocksOnArtefacts(text);
				
				
				if (featureList != null) {
					// Feature Related Metrics
					List<Double> nBlocksInFeatures = new ArrayList<Double>();
					List<Double> nElementsInFeatures = new ArrayList<Double>();
					for (Feature feature : featureList.getOwnedFeatures()) {
						List<Block> blocks = ConstraintsHelper.getCorrespondingBlocks(adaptedModel, feature);
						double nBlocks = blocks.size();
						double nElements = 0;
						for (Block block : blocks) {
							nElements += block.getOwnedBlockElements().size();
						}
						nBlocksInFeatures.add(nBlocks);
						nElementsInFeatures.add(nElements);
					}
					addMetrics(text, "Number of Blocks assigned to a Feature", nBlocksInFeatures);
					addMetrics(text, "Number of Elements assigned to a Feature", nElementsInFeatures);
				}
				
				

				
				// Open window
				Display display = Display.getDefault();
				Shell shell = new Shell(display);
				ScrollableMessageDialog m = new ScrollableMessageDialog(shell, "Metrics", "", text.toString());
				m.open();

			}

			private void appendBlocksOnArtefacts(StringBuilder text) {
				text.append("\n\nBlocks on Artefacts\n;");
				for(Block b : adaptedModel.getOwnedBlocks()){
					text.append(b.getName() + ";");
				}
				text.append("\n");
				for (AdaptedArtefact aa : adaptedModel.getOwnedAdaptedArtefacts()) {
					text.append(aa.getArtefact().getName()+";");
					List<Block> blocksOfAA = AdaptedModelHelper.getBlocksOfAdaptedArtefact(aa);
					for(Block b : adaptedModel.getOwnedBlocks()){
						if(blocksOfAA.contains(b)){
							text.append("1;");
						} else {
							text.append("0;");
						}
					}
					text.append("\n");
				}
			}
		});
	}

	public static double mean(List<Double> list) {
		double sum = 0;
		for (int i = 0; i < list.size(); i++) {
			sum += list.get(i);
		}
		return sum / list.size();
	}

	// must be sorted list
	public static double median(List<Double> list) {
		int middle = list.size() / 2;
		if (list.size() % 2 == 1) {
			return list.get(middle);
		} else {
			return (list.get(middle - 1) + list.get(middle)) / 2.0;
		}
	}

	public static double standardDeviation(List<Double> list, Double mean) {
		Double numerator = 0.0;
		for (int i = 0; i < list.size(); i++) {
			numerator += Math.pow((list.get(i) - mean), 2);
		}
		numerator = numerator / list.size();
		return Math.sqrt(numerator);
	}

	public static void addMetrics(StringBuilder stringBuilder, String title, List<Double> data) {
		Collections.sort(data);
		stringBuilder.append("\n\n" + title);
		stringBuilder.append("\nMin= " + data.get(0));
		stringBuilder.append("\nMax= " + data.get(data.size() - 1));
		Double mean = mean(data);
		stringBuilder.append("\nMean= " + mean);
		stringBuilder.append("\nMedian= " + median(data));
		stringBuilder.append("\nStdDev= " + standardDeviation(data, mean));
	}

}
