package org.but4reuse.adapters.eclipse.generator;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.eclipse.EclipseAdapter;
import org.but4reuse.adapters.eclipse.FileElement;
import org.but4reuse.adapters.eclipse.PluginElement;
import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;
import org.but4reuse.adapters.eclipse.benchmark.FeatureHelper;
import org.but4reuse.adapters.eclipse.generator.utils.FeaturesAndPluginsDependencies;
import org.but4reuse.adapters.eclipse.generator.utils.FileAndDirectoryUtils;
import org.but4reuse.adapters.eclipse.generator.utils.IListener;
import org.but4reuse.adapters.eclipse.generator.utils.ISender;
import org.but4reuse.adapters.eclipse.generator.utils.VariantsUtils;
import org.eclipse.core.runtime.NullProgressMonitor;

public class VariantsGenerator implements IVariantsGenerator, ISender {

	private String input;
	private String output;
	private int nbVariants;
	private int percentage;

	String generatorSummary;
	List<IListener> listeners;
	EclipseAdapter adapter;

	public VariantsGenerator(String input, String output, int nbVariants, int percentage) {
		this.input = input;
		this.output = output;
		this.nbVariants = nbVariants;
		this.percentage = percentage;
		adapter = new EclipseAdapter();
	}

	@SuppressWarnings("unchecked")
	public void generate() {
		sendToAll("Starting generate with :");
		sendToAll("-input = " + input);
		sendToAll("-output = " + output);
		sendToAll("-variants number = " + nbVariants);
		sendToAll("-percentage = " + percentage + " %\n");

		File eclipse = new File(input);
		File outputFile = new File(output);

		if (!eclipse.exists()) {
			sendToAll(input + " not exists !");
			return;
		}

		try { // Clear the output
			FileAndDirectoryUtils.deleteFile(outputFile);
		} catch (Exception e) {
		}

		URI inputURI = new File(input).toURI();
		List<ActualFeature> allFeatures;
		try {
			allFeatures = FeatureHelper.getFeaturesOfEclipse(inputURI.toString());
		} catch (Exception e) {
			sendToAll("Error in generator : Impossible to get all features.");
			return;
		}

		List<PluginElement> allPlugins = new ArrayList<>();
		List<FileElement> allFileElements = new ArrayList<>();
		{
			List<IElement> iElems = adapter.adapt(inputURI, new NullProgressMonitor());
			for (IElement elem : iElems) {
				if (elem instanceof PluginElement)
					allPlugins.add((PluginElement) elem);
				else if (elem instanceof FileElement)
					allFileElements.add((FileElement) elem);
			}
		}
		sendToAll("Total features number in the input = " + allFeatures.size());
		sendToAll("Total plugins number in the input = " + allPlugins.size() + "\n");

		FeaturesAndPluginsDependencies depOperator = new FeaturesAndPluginsDependencies(allFeatures,
				(List<PluginElement>) allPlugins, inputURI.toString());

		for (int i = 1; i <= nbVariants; i++) {
			String output_variant = output + File.separator + VariantsUtils.VARIANT + i;
			int nbSelectedFeatures = 0;

			List<PluginElement> pluginsList = null;
			List<ActualFeature> chosenFeatures = null;

			if (percentage == 100) {
				nbSelectedFeatures = allFeatures.size();
				pluginsList = allPlugins;
				chosenFeatures = allFeatures;
			} else {
				pluginsList = new ArrayList<>();
				chosenFeatures = new ArrayList<>();
			}

			if (percentage != 100 && percentage != 0) {
				for (int cptFeature = 0; cptFeature < allFeatures.size(); cptFeature++) {
					ActualFeature oneFeature = allFeatures.get(cptFeature);
					boolean wasChosen = wasChosen(oneFeature);
					if (wasChosen)
						nbSelectedFeatures++;

					if (chosenFeatures.contains(oneFeature) || !wasChosen)
						continue; // Check if not exists and apply the random
									// choice
					chosenFeatures.add(oneFeature);

					if (percentage < 100) {
						List<ActualFeature> allFeaturesDependencies = depOperator.getFeaturesDependencies(oneFeature);
						if (allFeaturesDependencies != null) {
							for (ActualFeature depFeat : allFeaturesDependencies) {
								if (!chosenFeatures.contains(depFeat))
									chosenFeatures.add(depFeat); // Avoid
																	// duplicates
																	// dependencies
																	// in the
																	// chosenFeatures
																	// list
							}
						}
					}

				} // end of iterate through allFeatures

				for (ActualFeature chosenFeature : chosenFeatures) { // Get all
																		// plugins
																		// from
																		// chosen
																		// features
					List<PluginElement> allPluginsDependencies = depOperator.getPluginsDependencies(chosenFeature);
					if (allPluginsDependencies != null) {
						for (PluginElement depPlugin : allPluginsDependencies) {
							if (!pluginsList.contains(depPlugin))
								pluginsList.add(depPlugin); // Avoid duplicates
															// dependencies in
															// the plugins list
						}
					}
				}

				pluginsList.addAll(depOperator.getPluginsWithoutAnyFeaturesDependencies());
			}
			try { // Create all dirs and copy features and plugins
				FileUtils.forceMkdir(new File(output_variant));

				for (File file_eclipse : eclipse.listFiles()) { // Copy eclipse
																// files & dirs
																// (except
																// features &
																// plugins)
					if (!file_eclipse.getName().equals(VariantsUtils.FEATURES)
							&& !file_eclipse.getName().equals(VariantsUtils.PLUGINS)) {
						FileAndDirectoryUtils.copyFilesAndDirectories(output_variant, file_eclipse);
					}
				}

				// features copy
				File[] allFilesFeatures = new File[chosenFeatures.size()];
				for (int j = 0; j < chosenFeatures.size(); j++) {
					allFilesFeatures[j] = new File(depOperator.getPathFromFeature(chosenFeatures.get(j)));
				}
				FileAndDirectoryUtils.copyFilesAndDirectories(output_variant + File.separator + VariantsUtils.FEATURES,
						allFilesFeatures);

				// plugins copy
				File[] allFilesPlugins = new File[pluginsList.size()];
				for (int j = 0; j < pluginsList.size(); j++) {
					allFilesPlugins[j] = new File(pluginsList.get(j).getAbsolutePath());
				}
				FileAndDirectoryUtils.copyFilesAndDirectories(output_variant + File.separator + VariantsUtils.PLUGINS,
						allFilesPlugins);

				System.out.println("(Variant " + i + ") features created.");
			} catch (Exception e) {
				System.out.println("(Variant " + i + ") features error : " + e);
			}

			sendToAll("Total of features selected with the random for variant n°" + i + " = " + nbSelectedFeatures
					+ "\nTotal of features (all selected, including required and \n"
					+ "included dependencies, direct or indirect) for variant n°" + i + " = " + chosenFeatures.size()
					+ "\nPlugins number for variant n°" + i + "= " + pluginsList.size() + "\n");

			List<IElement> allElements = new ArrayList<>((List<IElement>) (List<?>) allFileElements);
			allElements.addAll((List<IElement>) (List<?>) pluginsList);
			adapter.construct(inputURI, allElements, new NullProgressMonitor());

		} // end of variantes loop

		sendToAll("\nGeneration finished !");
	}

	@Override
	public void addListener(IListener listener) {
		if (listeners == null)
			listeners = new ArrayList<IListener>();
		listeners.add(listener);
	}

	@Override
	public void sendToAll(String msg) {
		if (msg != null && listeners != null && !listeners.isEmpty()) {
			for (IListener oneListener : listeners) {
				oneListener.receive(msg);
			}
		}
	}

	@Override
	public void sendToOne(IListener listener, String msg) {
		if (listeners != null && !listeners.isEmpty()) {
			int index = listeners.indexOf(listener);
			listeners.get(index).receive(msg);
		}
	}

	private boolean wasChosen(ActualFeature feature) {
		if (feature == null || percentage == 0)
			return false;
		else if (percentage == 100 || Math.random() * 100 < percentage)
			return true;
		else
			return false;
	}

}
