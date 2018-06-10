package org.but4reuse.adapters.eclipse.benchmark.generator;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.eclipse.EclipseAdapter;
import org.but4reuse.adapters.eclipse.FileElement;
import org.but4reuse.adapters.eclipse.PluginElement;
import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;
import org.but4reuse.adapters.eclipse.benchmark.FeatureHelper;
import org.but4reuse.adapters.eclipse.benchmark.generator.utils.DependencyAnalyzer;
import org.but4reuse.adapters.eclipse.benchmark.generator.utils.EclipseKeepOnlyMetadata;
import org.but4reuse.adapters.eclipse.benchmark.generator.utils.VariantsUtils;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;
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
public class VariantsPercentageBasedGenerator implements IVariantsGenerator {

	private String input;
	private String output;
	private int nbVariants;
	private int percentage;
	private Long randomSeed;
	private boolean keepOnlyMetadata;
	private boolean noOutputOnlyStatistics;

	String generatorSummary;
	EclipseAdapter adapter;
	StringBuffer message;
	Random random;

	public VariantsPercentageBasedGenerator(String input, String output, int nbVariants, int percentage,
			Long randomSeed, boolean keepOnlyMetadata, boolean noOutputOnlyStatistics) {
		this.input = input;
		this.output = output;
		this.nbVariants = nbVariants;
		this.percentage = percentage;
		this.randomSeed = randomSeed;
		this.keepOnlyMetadata = keepOnlyMetadata;
		this.noOutputOnlyStatistics = noOutputOnlyStatistics;
		adapter = new EclipseAdapter();
		message = new StringBuffer();
		if (randomSeed == null) {
			// this will use system time-stamp as seed
			random = new Random();
		} else {
			random = new Random(randomSeed);
		}
	}

	public String generate(IProgressMonitor monitor) {
		monitor.subTask("Preparation");
		message = new StringBuffer();
		long startTime = System.currentTimeMillis();
		message.append("Parameters:\n");
		message.append("-input = " + input + "\n");
		message.append("-output = " + output + "\n");
		message.append("-variants number = " + nbVariants + "\n");
		message.append("-percentage = " + percentage + " %" + "\n");
		if (randomSeed != null) {
			message.append("-random seed = " + randomSeed + "\n");
		}
		message.append("-keepOnlyMetadata = " + keepOnlyMetadata + "\n");
		message.append("-onlyStatistics= " + noOutputOnlyStatistics + "\n\n");

		File eclipse = new File(input);

		if (!eclipse.exists()) {
			message.append(input + " does not exist.\n");
			return message.toString();
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
			message.append(input + " seems not to be an eclipse installation.\n");
			return message.toString();
		}

		URI inputURI = new File(input).toURI();
		List<ActualFeature> allFeatures;
		try {
			allFeatures = FeatureHelper.getFeaturesOfEclipse(inputURI.toString());
		} catch (Exception e) {
			message.append("Error in generator : Impossible to get all features.\n");
			e.printStackTrace();
			return message.toString();
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
			if (elem instanceof PluginElement) {
				allPlugins.add((PluginElement) elem);
			} else if (elem instanceof FileElement) {
				allFileElements.add((FileElement) elem);
			}
		}

		message.append("Total features number in the input = " + allFeatures.size() + "\n");
		// message.append("Total plugins number in the input = " +
		// allPluginsGen.size() + "\n\n");

		// Analyse the dependencies only once before starting
		monitor.subTask("Preparation: Dependency analysis");
		DependencyAnalyzer depAnalyzer = new DependencyAnalyzer(allFeatures, allPlugins, inputURI.toString());

		long stopTimePreparation = System.currentTimeMillis();
		long elapsedTimePreparation = stopTimePreparation - startTime;
		message.append("Preparation time (milliseconds): " + elapsedTimePreparation + "\n\n");

		message.append(
				"\"Variant\";\"Name\";\"Randomly selected features\";\"Features after dependency resolution\";\"Plugins\";\"Milliseconds\"\n");

		// preparation is finished
		monitor.worked(1);

		// Loop for each variant
		for (int i = 1; i <= nbVariants; i++) {
			monitor.subTask("Generating variant " + i + " out of " + nbVariants);

			// User pressed the cancel button
			if (monitor.isCanceled()) {
				break;
			}

			long startTimeThisVariant = System.currentTimeMillis();
			String output_variant = output + File.separator + VariantsUtils.VARIANT + "_" + i;
			int nbSelectedFeatures = 0;

			List<PluginElement> pluginsList = null;
			List<ActualFeature> chosenFeatures = null;

			if (percentage == 100) {
				nbSelectedFeatures = allFeatures.size();
				pluginsList = allPlugins;
				chosenFeatures = allFeatures;
			} else {
				pluginsList = new ArrayList<PluginElement>();
				chosenFeatures = new ArrayList<ActualFeature>();
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

					List<ActualFeature> allFeatureDependencies = depAnalyzer.getFeatureDependencies(oneFeature);
					if (allFeatureDependencies != null) {
						for (ActualFeature depFeat : allFeatureDependencies) {
							if (!chosenFeatures.contains(depFeat)) {
								// Avoid duplicates dependencies in the
								// chosenFeatures list
								chosenFeatures.add(depFeat);
							}
						}
					}

				} // end of iterate through allFeatures

				for (ActualFeature one_manda : depAnalyzer.getMandatoryFeaturesForThisInput()) {
					if (!chosenFeatures.contains(one_manda)) {
						chosenFeatures.add(one_manda);
					}
				}

				// Get all plugins from chosen features
				for (ActualFeature chosenFeature : chosenFeatures) {
					List<PluginElement> allPluginDependencies = depAnalyzer.getPluginDependencies(chosenFeature);
					if (allPluginDependencies != null) {
						for (PluginElement depPlugin : allPluginDependencies) {
							// Avoid duplicated dependencies in the plugins list
							if (!pluginsList.contains(depPlugin)) {
								pluginsList.add(depPlugin);
							}
						}
					}
				}
				pluginsList.addAll(depAnalyzer.getPluginsWithoutAnyFeatureDependencies());
			}

			if (!noOutputOnlyStatistics) {

				// Create all dirs and copy features and plugins
				File output_variantFile = new File(output_variant);
				output_variantFile.mkdirs();

				for (File file_eclipse : eclipse.listFiles()) {
					// Copy eclipse files & dirs (except features & plugins)
					if (!file_eclipse.getName().equals(VariantsUtils.FEATURES)
							&& !file_eclipse.getName().equals(VariantsUtils.PLUGINS)) {
						FileUtils.copyFileOrDirectoryToDirectory(file_eclipse, output_variantFile);
					}
				}

				// features copy
				File featuresFolder = new File(output_variantFile, VariantsUtils.FEATURES);
				File[] allFilesFeatures = new File[chosenFeatures.size()];
				for (int j = 0; j < chosenFeatures.size(); j++) {
					allFilesFeatures[j] = new File(depAnalyzer.getPathFromFeature(chosenFeatures.get(j)));
					FileUtils.copyFileOrDirectoryToDirectory(allFilesFeatures[j], featuresFolder);
				}

				// plugins copy
				File pluginsFolder = new File(output_variantFile, VariantsUtils.PLUGINS);
				File[] allFilesPlugins = new File[pluginsList.size()];
				for (int j = 0; j < pluginsList.size(); j++) {
					allFilesPlugins[j] = new File(pluginsList.get(j).getAbsolutePath());
					FileUtils.copyFileOrDirectoryToDirectory(allFilesPlugins[j], pluginsFolder);
				}

				if (!keepOnlyMetadata) {
					// This call adapter construct mainly to fix the bundle.info
					// configuration file to have a functional eclipse
					List<IElement> allElements = new ArrayList<IElement>();
					allElements.addAll(allFileElements);
					allElements.addAll(pluginsList);

					URI outputUri = output_variantFile.toURI();
					adapter.construct(outputUri, allElements, new NullProgressMonitor());
				}

				if (keepOnlyMetadata) {
					// We keep only manifests, properties and xmls
					EclipseKeepOnlyMetadata.cleanAndKeepOnlyMetadata(output_variantFile);
				}
			}

			long stopTimeThisVariant = System.currentTimeMillis();
			long elapsedTimeThisVariant = stopTimeThisVariant - startTimeThisVariant;
			message.append(i + ";Variant_" + i + ";" + nbSelectedFeatures + ";" + chosenFeatures.size() + ";"
					+ pluginsList.size() + ";" + elapsedTimeThisVariant + "\n");

			monitor.worked(1);
		} // end of variants loop

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		message.append("\nGeneration finished! Miliseconds: " + elapsedTime);
		monitor.done();
		return message.toString();
	}

	/**
	 * Decide if a feature is chosen
	 * 
	 * @param feature
	 * @return true or false
	 */
	private boolean wasChosen(ActualFeature feature) {
		if (feature == null || percentage == 0) {
			return false;
		} else if (percentage == 100 || random.nextDouble() * 100 < percentage) {
			return true;
		} else {
			return false;
		}
	}

}
