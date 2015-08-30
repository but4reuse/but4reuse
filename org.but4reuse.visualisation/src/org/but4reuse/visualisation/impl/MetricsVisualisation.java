package org.but4reuse.visualisation.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.feature.constraints.impl.ConstraintsHelper;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.but4reuse.visualisation.IVisualisation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;

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
				appendUsedAdapters(text);
				text.append("--------------------------------------------\n");
				text.append("Artefacts= " + adaptedModel.getOwnedAdaptedArtefacts().size() + "\n");

				// Get IElement types
				List<String> iElementTypes = new ArrayList<String>();
				for (IAdapter adapter : AdaptedModelManager.getAdapters()) {
					iElementTypes.addAll(AdaptersHelper.getAdapterIElements(adapter));
				}

				List<Double> nElementsPerArtefact = new ArrayList<Double>();
				for (AdaptedArtefact aa : adaptedModel.getOwnedAdaptedArtefacts()) {
					double nElements = aa.getOwnedElementWrappers().size();
					nElementsPerArtefact.add(nElements);
				}
				addMetrics(text, "Number of Elements per Artefact", nElementsPerArtefact);

				text.append("\n\nElement types per Artefact\n");
				text.append(";");
				for (String elementType : iElementTypes) {
					text.append(elementType.substring(elementType.lastIndexOf(".") + 1, elementType.length()) + ";");
				}
				text.setLength(text.length()-1);
				text.append("\n");
				for (AdaptedArtefact aa : adaptedModel.getOwnedAdaptedArtefacts()) {
					text.append(aa.getArtefact().getName() + ";");
					for (String elementType : iElementTypes) {
						text.append(AdaptedModelHelper.getNumberOfElementsOfType(aa, elementType) + ";");
					}
					text.setLength(text.length()-1);
					text.append("\n");
				}

				text.append("--------------------------------------------\n");
				text.append("Blocks= " + adaptedModel.getOwnedBlocks().size() + "\n");
				List<Double> nElementsPerBlock = new ArrayList<Double>();
				for (Block block : adaptedModel.getOwnedBlocks()) {
					double nElements = block.getOwnedBlockElements().size();
					nElementsPerBlock.add(nElements);
				}
				addMetrics(text, "Number of Elements per Block", nElementsPerBlock);

				text.append("\n\nElement types per Block\n");
				text.append(";");
				for (String elementType : iElementTypes) {
					text.append(elementType.substring(elementType.lastIndexOf(".") + 1, elementType.length()) + ";");
				}
				text.setLength(text.length()-1);
				text.append("\n");
				for (Block block : adaptedModel.getOwnedBlocks()) {
					text.append(block.getName() + ";");
					for (String elementType : iElementTypes) {
						text.append(AdaptedModelHelper.getNumberOfElementsOfType(block, elementType) + ";");
					}
					text.setLength(text.length()-1);
					text.append("\n");
				}

				appendBlocksOnArtefacts(text);

				text.append("--------------------------------------------\n");
				text.append("Number of Block Constraints= "
						+ ConstraintsHelper.getCalculatedConstraints(adaptedModel).size() + "\n");

				if (featureList != null) {
					// Feature Related Metrics
					text.append("--------------------------------------------\n");
					text.append("Features= " + featureList.getOwnedFeatures().size() + "\n");
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

					text.append("\n\nBlocks on Features\n");
					for (Feature feature : featureList.getOwnedFeatures()) {
						text.append(feature.getName() + " = ");
						List<Block> blocks = ConstraintsHelper.getCorrespondingBlocks(adaptedModel, feature);
						for (Block b : blocks) {
							text.append(b.getName() + ", ");
						}
						// remove last comma
						if (!blocks.isEmpty()) {
							text.setLength(text.length() - 2);
						}
						text.append("\n");
					}

					text.append("\nBlocks on Features\n");
					text.append(";");
					for (Block b : adaptedModel.getOwnedBlocks()) {
						text.append(b.getName() + ";");
					}
					text.setLength(text.length()-1);
					text.append("\n");
					for (Feature feature : featureList.getOwnedFeatures()) {
						text.append(feature.getName() + ";");
						List<Block> blocks = ConstraintsHelper.getCorrespondingBlocks(adaptedModel, feature);
						for (Block b : adaptedModel.getOwnedBlocks()) {
							if (blocks.contains(b)) {
								text.append("1;");
							} else {
								text.append("0;");
							}
						}
						text.setLength(text.length()-1);
						text.append("\n");
					}

					addMetrics(text, "Number of Elements assigned to a Feature", nElementsInFeatures);
					text.append("\n");
				}

				text.append("--------------------------------------------\n");
				text.append("Times in milliseconds\n");
				for (Entry<String, Long> entry : AdaptedModelManager.getElapsedTimeRegistry().entrySet()) {
					text.append(entry.getKey() + "= " + entry.getValue() + "\n");
				}

				String name = AdaptedModelHelper.getName(adaptedModel);
				if (name == null) {
					name = "";
				}

				MetricsVisualisationView view = (MetricsVisualisationView) WorkbenchUtils
						.forceShowView(MetricsVisualisationView.ID);

				view.scrollable.setText(text.toString());
			}

			private void appendUsedAdapters(StringBuilder text) {
				text.append("Adapter= ");
				for (IAdapter adapter : AdaptedModelManager.getAdapters()) {
					text.append(AdaptersHelper.getAdapterName(adapter) + ",");
				}
				text.setLength(text.length() - 1);
				text.append("\n");
			}

			private void appendBlocksOnArtefacts(StringBuilder text) {
				text.append("\nBlocks on Artefacts\n;");
				for (Block b : adaptedModel.getOwnedBlocks()) {
					text.append(b.getName() + ";");
				}
				text.setLength(text.length()-1);
				text.append("\n");
				for (AdaptedArtefact aa : adaptedModel.getOwnedAdaptedArtefacts()) {
					text.append(aa.getArtefact().getName() + ";");
					List<Block> blocksOfAA = AdaptedModelHelper.getBlocksOfAdaptedArtefact(aa);
					for (Block b : adaptedModel.getOwnedBlocks()) {
						if (blocksOfAA.contains(b)) {
							text.append("1;");
						} else {
							text.append("0;");
						}
					}
					text.setLength(text.length()-1);
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
		if (!data.isEmpty()) {
			Collections.sort(data);
			stringBuilder.append("\n" + title);
			stringBuilder.append("\nMin= " + data.get(0));
			stringBuilder.append("\nMax= " + data.get(data.size() - 1));
			Double mean = mean(data);
			stringBuilder.append("\nMean= " + mean);
			stringBuilder.append("\nMedian= " + median(data));
			stringBuilder.append("\nStdDev= " + standardDeviation(data, mean));
		}
	}

}
