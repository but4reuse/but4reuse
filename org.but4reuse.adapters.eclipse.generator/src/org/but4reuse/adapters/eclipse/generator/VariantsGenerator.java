package org.but4reuse.adapters.eclipse.generator;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.eclipse.EclipseAdapter;
import org.but4reuse.adapters.eclipse.FileElement;
import org.but4reuse.adapters.eclipse.PluginElement;
import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;
import org.but4reuse.adapters.eclipse.benchmark.FeatureHelper;
import org.but4reuse.adapters.eclipse.generator.dependencies.DependencyAnalyzer;
import org.but4reuse.adapters.eclipse.generator.interfaces.IListener;
import org.but4reuse.adapters.eclipse.generator.interfaces.ISender;
import org.but4reuse.adapters.eclipse.generator.interfaces.IVariantsGenerator;
import org.but4reuse.adapters.eclipse.generator.utils.EclipseKeepOnlyMetadata;
import org.but4reuse.adapters.eclipse.generator.utils.FileAndDirectoryUtils;
import org.but4reuse.adapters.eclipse.generator.utils.PluginElementGenerator;
import org.but4reuse.adapters.eclipse.generator.utils.VariantsUtils;
import org.eclipse.core.runtime.NullProgressMonitor;

/**
 * Percentage-based random selection of features: A unique user-specified
 * percentage is used as probability for including or not each of the features
 * of the initial Eclipse. Once the features are randomly selected using this
 * method, we still need to guarantee that the feature configuration is valid.
 * Therefore, a second step includes the missing features by analysing feature
 * dependencies. Using this final list of selected features, the generation of
 * functional Eclipse variants is performed.
 * 
 * We ignore org.eclipse.epp.package.* This is an abstract feature (abstract
 * means that it does not have associated plugins) that requires all the
 * features belonging to this Eclipse package. We do not want it because if this
 * feature is selected then everything will be selected.
 * 
 * @author Julien Margarido
 * @author Felix Lima Gorito
 * @author jabier.martinez
 * 
 */
public class VariantsGenerator implements IVariantsGenerator, ISender {

	private String input;
	private String output;
	private int nbVariants;
	private int percentage;
	private boolean keepOnlyMetadata;

	String generatorSummary;
	List<IListener> listeners;
	EclipseAdapter adapter;

	public VariantsGenerator(String input, String output, int nbVariants, int percentage, boolean keepOnlyMetadata) {
		this.input = input;
		this.output = output;
		this.nbVariants = nbVariants;
		this.percentage = percentage;
		this.keepOnlyMetadata = keepOnlyMetadata;
		adapter = new EclipseAdapter();
	}

	public void generate() {

		long startTime = System.currentTimeMillis();
		sendToAll("Starting generation with :");
		sendToAll("-input = " + input);
		sendToAll("-output = " + output);
		sendToAll("-variants number = " + nbVariants);
		sendToAll("-percentage = " + percentage + " %");
		sendToAll("-keepOnlyMetadata = " + keepOnlyMetadata + "\n");

		sendToAll("Please wait until Generation finished\n");

		File eclipse = new File(input);

		if (!eclipse.exists()) {
			sendToAll(input + " not exists !");
			return;
		}

		// if the eclipse dir is inside the input
		if (eclipse.list().length == 1 && eclipse.listFiles()[0].getName().equals("eclipse")) {
			if (input.endsWith(File.separator))
				input += "eclipse" + File.separator;
			else
				input += File.separator + "eclipse" + File.separator;
			eclipse = new File(input);
		}

		// check if it's an eclipse directory
		if (!VariantsUtils.isEclipseDir(eclipse)) {
			sendToAll(input + " is not an eclipse !");
			return;
		}

		URI inputURI = new File(input).toURI();
		List<ActualFeature> allFeatures;
		try {
			allFeatures = FeatureHelper.getFeaturesOfEclipse(inputURI.toString());
		} catch (Exception e) {
			sendToAll("Error in generator : Impossible to get all features.");
			e.printStackTrace();
			return;
		}

		// ignore epp package
		List<ActualFeature> toRemove = new ArrayList<ActualFeature>();
		for (ActualFeature f : allFeatures) {
			if (f.getId().startsWith("org.eclipse.epp.package.")) {
				toRemove.add(f);
			}
		}
		allFeatures.removeAll(toRemove);

		List<PluginElement> allPlugins = new ArrayList<PluginElement>();
		List<FileElement> allFileElements = new ArrayList<FileElement>();

		List<IElement> iElems = adapter.adapt(inputURI, new NullProgressMonitor());
		for (IElement elem : iElems) {
			if (elem instanceof PluginElement)
				allPlugins.add((PluginElement) elem);
			else if (elem instanceof FileElement)
				allFileElements.add((FileElement) elem);
		}

		// Permits to use PluginElement without launch an Eclipse Application
		List<PluginElementGenerator> allPluginsGen = PluginElementGenerator.transformInto(allPlugins);

		sendToAll("Total features number in the input = " + allFeatures.size());
		sendToAll("Total plugins number in the input = " + allPluginsGen.size() + "\n");

		// Analyse the dependencies only once before starting
		DependencyAnalyzer depAnalyzer = new DependencyAnalyzer(allFeatures, allPluginsGen, inputURI.toString());

		long stopTimePreparation = System.currentTimeMillis();
		long elapsedTimePreparation = stopTimePreparation - startTime;
		sendToAll("Preparation time (milliseconds): " + elapsedTimePreparation + "\n");

		sendToAll("\"Variant\";\"Name\";\"Randomly selected features\";\"Features after dependency resolution\";\"Plugins\";\"Milliseconds\"");

		// Loop for each variant
		for (int i = 1; i <= nbVariants; i++) {
			long startTimeThisVariant = System.currentTimeMillis();
			String output_variant = output + File.separator + VariantsUtils.VARIANT + "_" + i;
			int nbSelectedFeatures = 0;

			List<PluginElementGenerator> pluginsList = null;
			List<ActualFeature> chosenFeatures = null;

			if (percentage == 100) {
				nbSelectedFeatures = allFeatures.size();
				pluginsList = allPluginsGen;
				chosenFeatures = allFeatures;
			} else {
				pluginsList = new ArrayList<>();
				chosenFeatures = new ArrayList<>();
			}

			if (percentage < 100 && percentage > 0) {
				for (int cptFeature = 0; cptFeature < allFeatures.size(); cptFeature++) {
					ActualFeature oneFeature = allFeatures.get(cptFeature);
					boolean wasChosen = wasChosen(oneFeature);
					if (wasChosen) {
						nbSelectedFeatures++;
					}
					if (chosenFeatures.contains(oneFeature) || !wasChosen) {
						// Check if not exists and apply the random choice
						continue;
					}
					chosenFeatures.add(oneFeature);

					List<ActualFeature> allFeaturesDependencies = depAnalyzer.getFeaturesDependencies(oneFeature);
					if (allFeaturesDependencies != null) {
						for (ActualFeature depFeat : allFeaturesDependencies) {
							if (!chosenFeatures.contains(depFeat)) {
								// Avoid duplicates dependencies in the
								// chosenFeatures list
								chosenFeatures.add(depFeat);
							}
						}
					}

				} // end of iterate through allFeatures

				for (ActualFeature one_manda : depAnalyzer.getFeaturesMandatoriesByInput()) {
					if (!chosenFeatures.contains(one_manda))
						chosenFeatures.add(one_manda);
				}

				// Get all plugins from chosen features
				for (ActualFeature chosenFeature : chosenFeatures) {
					List<PluginElementGenerator> allPluginsDependencies = depAnalyzer
							.getPluginsDependencies(chosenFeature);
					if (allPluginsDependencies != null) {
						for (PluginElementGenerator depPlugin : allPluginsDependencies) {
							// Avoid duplicates dependencies in the plugins list
							if (!pluginsList.contains(depPlugin)) {
								pluginsList.add(depPlugin);
							}
						}
					}
				}

				pluginsList.addAll(depAnalyzer.getPluginsWithoutAnyFeaturesDependencies());

			}
			try {
				// Create all dirs and copy features and plugins
				File output_variantFile = new File(output_variant);
				org.apache.commons.io.FileUtils.forceMkdir(output_variantFile);

				for (File file_eclipse : eclipse.listFiles()) {
					// Copy eclipse files & dirs (except features & plugins)
					if (!file_eclipse.getName().equals(VariantsUtils.FEATURES)
							&& !file_eclipse.getName().equals(VariantsUtils.PLUGINS)) {
						FileAndDirectoryUtils.copyFilesAndDirectories(output_variant, file_eclipse);
					}
				}

				// features copy
				File[] allFilesFeatures = new File[chosenFeatures.size()];
				for (int j = 0; j < chosenFeatures.size(); j++) {
					allFilesFeatures[j] = new File(depAnalyzer.getPathFromFeature(chosenFeatures.get(j)));
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
			} catch (Exception e) {
				e.printStackTrace();
			}

			File output_VariantFile = new File(output_variant);

			if (!keepOnlyMetadata) {
				// This call adapter construct mainly to fix the bundle.info
				// configuration file to have a functional eclipse
				List<IElement> allElements = new ArrayList<IElement>();
				allElements.addAll(allFileElements);
				allElements.addAll(pluginsList);

				URI outputUri = output_VariantFile.toURI();
				adapter.construct(outputUri, allElements, new NullProgressMonitor());
			}

			if (keepOnlyMetadata) {
				// We keep only manifests, properties and xmls
				EclipseKeepOnlyMetadata.cleanAndKeepOnlyMetadata(output_VariantFile);
			}

			long stopTimeThisVariant = System.currentTimeMillis();
			long elapsedTimeThisVariant = stopTimeThisVariant - startTimeThisVariant;
			sendToAll(i + ";Variant_" + i + ";" + nbSelectedFeatures + ";" + chosenFeatures.size() + ";"
					+ pluginsList.size() + ";" + elapsedTimeThisVariant);
		} // end of variants loop

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		sendToAll("\nGeneration finished ! Miliseconds: " + elapsedTime);

	}

	@Override
	public void addListener(IListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<IListener>();
		}
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

	private boolean wasChosen(ActualFeature feature) {
		if (feature == null || percentage == 0) {
			return false;
		} else if (percentage == 100 || Math.random() * 100 < percentage) {
			return true;
		} else {
			return false;
		}
	}

}
