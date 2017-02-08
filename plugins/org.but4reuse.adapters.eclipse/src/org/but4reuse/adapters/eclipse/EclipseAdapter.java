package org.but4reuse.adapters.eclipse;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.eclipse.plugin_infos_extractor.utils.DependenciesBuilder;
import org.but4reuse.adapters.eclipse.plugin_infos_extractor.utils.PluginInfosExtractor;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Eclipse adapter
 * 
 * @author Fjorilda Gjermizi
 * @author Krista Drushku
 * @author Diana MALABARD
 * @author Jason CHUMMUN
 * 
 */
public class EclipseAdapter implements IAdapter {

	private URI rootURI;

	/**
	 * This method check if the artefact is adaptable with the EclipseAdapter
	 */

	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		File file = FileUtils.getFile(uri);
		if (file.isDirectory()) {
			File pluginsFolder = new File(file.getAbsolutePath() + "/plugins");
			return pluginsFolder.exists() && pluginsFolder.isDirectory();
		}
		return false;
	}

	Map<String, String> bundlesInfoLines;

	/**
	 * Provides the atomic elements (plugins) this distribution is made of
	 * 
	 * @param uri
	 *            URI of the distribution
	 * @param monitor
	 */
	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {

		List<IElement> elements = new ArrayList<IElement>();
		File file = FileUtils.getFile(uri);
		rootURI = file.toURI();

		// A hashmap of bundle symbolic names and the complete line in the
		// bundles.info file
		bundlesInfoLines = PluginInfosExtractor.createBundlesInfoMap(uri);

		// start the containment tree traversal, with null as initial container
		adapt(file, elements, null);

		// plugin dependencies
		for (IElement elem : elements) {
			if (elem instanceof PluginElement) {
				PluginElement pe = (PluginElement) elem;
				DependenciesBuilder.build(pe, elements);
			}
		}

		// in elements we have the result
		return elements;
	}

	/**
	 * adapt recursively
	 * 
	 * @param file
	 * @param elements
	 * @param container
	 */
	private void adapt(File file, List<IElement> elements, IElement container) {
		FileElement newElement = null;
		if (PluginInfosExtractor.isAPlugin(file)) {
			try {
				// Unzipped plugin
				if (file.isDirectory()) {
					newElement = PluginInfosExtractor.getPluginInfosFromManifest(file.getAbsolutePath()
							+ "/META-INF/MANIFEST.MF");
				} else {
					// Jar plugin
					newElement = PluginInfosExtractor.getPluginInfosFromJar(file.getAbsolutePath());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			newElement = new FileElement();
		}

		// Set the relevant information
		newElement.setUri(file.toURI());
		newElement.setRelativeURI(rootURI.relativize(file.toURI()));

		// Add dependency to the parent folder
		if (container != null) {
			newElement.addDependency("container", container);
		}

		// Add the bundles info
		if (newElement instanceof PluginElement) {
			PluginElement plugin = (PluginElement) newElement;
			String line = bundlesInfoLines.get(plugin.getSymbName());
			// in the case of source code plugins, line will be null but no
			// problem
			plugin.setBundleInfoLine(line);

			// if (plugin.getName() == null || plugin.getName().contains("%")) {
			// System.out.println("EclipseAdapter.adapt() No name found: " +
			// " isFragment:" + plugin.isFragment()
			// + "  " + plugin.getSymbName() + " at " + file.getAbsolutePath());
			// }

		}

		// Add to the list
		addElement(elements, newElement);

		// Go for the files in case of folder
		if (file.isDirectory()) {
			// Exclude the features folder
			if (!newElement.getRelativeURI().toString().equals("features/")) {
				File[] files = file.listFiles();
				for (File subFile : files) {
					adapt(subFile, elements, newElement);
				}
			}
		}
	}

	/**
	 * This method was created just to be overriden by the benchmark adapter
	 * 
	 * @param elements
	 * @param newElement
	 */
	protected void addElement(List<IElement> elements, FileElement newElement) {
		elements.add(newElement);
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		boolean constructBundlesInfo = false;
		String bundlesInfoContent = "#version=1\n";
		for (IElement element : elements) {
			// check user cancel for each element
			if (!monitor.isCanceled()) {
				// provide user info
				monitor.subTask(element.getText());
				if (element instanceof FileElement) {
					FileElement fileElement = (FileElement) element;
					if (fileElement.getRelativeURI().toString().equals(PluginInfosExtractor.BUNDLESINFO_RELATIVEPATH)) {
						constructBundlesInfo = true;
					}
					try {
						// Create parent folders structure
						URI newDirectoryURI = uri.resolve(fileElement.getRelativeURI());
						File destinationFile = FileUtils.getFile(newDirectoryURI);
						if (destinationFile != null && !destinationFile.getParentFile().exists()) {
							destinationFile.getParentFile().mkdirs();
						}
						if (destinationFile != null && !destinationFile.exists()) {
							// Copy the content. In the case of a folder, its
							// content is not copied
							File file = FileUtils.getFile(fileElement.getUri());
							Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// prepare the bundles.info configuration file
				// just in case we need to construct it
				if (element instanceof PluginElement) {
					PluginElement pluginElement = (PluginElement) element;
					String line = pluginElement.getBundleInfoLine();
					if (line != null) {
						String[] lineFields = line.split(",");
						bundlesInfoContent += pluginElement.getSymbName() + ",";
						bundlesInfoContent += pluginElement.getVersion() + ",";
						bundlesInfoContent += pluginElement.getRelativeURI() + ",";
						bundlesInfoContent += lineFields[3] + ",";
						bundlesInfoContent += lineFields[4] + "\n";
					}
				}
			}
			monitor.worked(1);
		}
		// Replace bundles.info content
		if (constructBundlesInfo) {
			try {
				File tmpFile = File.createTempFile("tempBundles", "info");
				FileUtils.appendToFile(tmpFile, bundlesInfoContent);
				File file = FileUtils.getFile(uri);
				File bundlesInfo = new File(file.getAbsolutePath() + "/"
						+ PluginInfosExtractor.BUNDLESINFO_RELATIVEPATH);
				FileUtils.replace(bundlesInfo, tmpFile);
				tmpFile.deleteOnExit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
