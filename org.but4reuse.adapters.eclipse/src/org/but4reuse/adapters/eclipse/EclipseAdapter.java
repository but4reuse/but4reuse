package org.but4reuse.adapters.eclipse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

	/**
	 * This method check if the artefact is adaptable with the EclipseAdapter
	 */

	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		File file = FileUtils.getFile(uri);
		if (file.isDirectory()) {
			File pluginsFolder = new File(file.getAbsolutePath() + "/plugins");
			if (pluginsFolder.exists() && pluginsFolder.isDirectory()) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

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
		if (file != null && file.exists() && file.isDirectory()) {
			elements.addAll(adaptFolder(file.getAbsolutePath() + "/dropins", monitor));
			elements.addAll(adaptFolder(file.getAbsolutePath() + "/plugins", monitor));
			// For each element, build the dependencies map, depending
			// on the plugins installed in the considered distribution
			// and the values retrieved in its RequiredBundle field
			for (IElement elem : elements) {
				DependenciesBuilder builder = new DependenciesBuilder((PluginElement) elem, elements);
				builder.run();
			}
		}
		return elements;
	}

	/**
	 * Searches for plugins in the given folder
	 * 
	 * @param uri
	 *            URI of an Eclipse folder
	 */
	private List<IElement> adaptFolder(String uri, IProgressMonitor monitor) {
		List<IElement> elements = new ArrayList<IElement>();
		File file = new File(uri);
		File[] fichiers = file.listFiles();

		for (int i = 0; i < fichiers.length; i++) {
			// Plugin as a folder
			if (fichiers[i].isDirectory()) {
				try {
					elements.add(PluginInfosExtractor.getPluginInfosFromManifest(fichiers[i].getAbsolutePath()
							+ "/META-INF/MANIFEST.MF"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Plugin as a jar
			} else if (fichiers[i].getPath().endsWith(".jar")) {
				try {
					elements.add(PluginInfosExtractor.getPluginInfosFromJar(fichiers[i].getAbsolutePath()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return elements;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		// System.out.println("Enter construct");
		//
		// File dest = new
		// File("C:/UPMC/M2/GPSTL/fevrier/runtime-EclipseApplication/test1/plugins/");
		// deleteFolder(dest);
		// // File dest = new
		// //
		// File("C:/UPMC/M2/GPSTL/fevrier/runtime-EclipseApplication/test/plugins/");
		// // System.out.println(dest.mkdirs());
		// for (IElement element : elements) {
		// System.out.println("************* ENTRE element");
		// URI uri2 = uri.resolve(uri);
		// System.out.println(uri2);
		//
		// if (!monitor.isCanceled()) {
		// monitor.subTask(element.getText());
		// if (element instanceof PluginElement) {
		// System.out.println("*********PluginELEMENT **********");
		// PluginElement fileElement = (PluginElement) element;
		//
		// try {
		// String pluginAddr = fileElement.getAbsolutePath();
		// System.out.println("plugin : " + pluginAddr);
		// String pluginName = tokenize(pluginAddr);
		// // URI newDirectoryURI = uri.resolve(pluginAddr);
		// // System.out.println("uri ==========="+
		// // newDirectoryURI.toString());
		// //
		// System.out.println("Eclipse i ri ------------------------"+newDirectoryURI+"plugins/"+pluginName);
		// FileUtils.downloadFileFromURL(new URL("file:///" + pluginAddr), new
		// File(
		// "C:/UPMC/M2/GPSTL/fevrier/runtime-EclipseApplication/test1/plugins/"
		// + pluginName));
		// System.out.println("pass");
		//
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// monitor.worked(1);
		// }
	}

	// private String tokenize(String addresse) {
	// String resultat = null;
	// StringTokenizer st = new StringTokenizer(addresse, "\\");
	// while (st.hasMoreTokens()) {
	// resultat = st.nextToken().toString();
	// }
	// return resultat;
	// }
	//
	// private void deleteFolder(File folder) {
	// File[] files = folder.listFiles();
	// if (files != null) {
	// // some JVMs return null for empty dirs
	// for (File f : files) {
	// if (f.isDirectory()) {
	// deleteFolder(f);
	// } else {
	// f.delete();
	// }
	// }
	// }
	// }
}
