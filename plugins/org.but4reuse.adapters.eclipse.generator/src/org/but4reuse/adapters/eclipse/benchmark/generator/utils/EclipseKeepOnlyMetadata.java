package org.but4reuse.adapters.eclipse.benchmark.generator.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.eclipse.EclipseAdapter;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.files.ZipUtils;
import org.eclipse.core.runtime.NullProgressMonitor;

/**
 * This is to reduce the size of Eclipse variants
 * 
 * @author jabier.martinez
 */
public class EclipseKeepOnlyMetadata {

	/**
	 * Clean
	 * 
	 * @param eclipse
	 *            is the folder we have the plugins and features folders
	 */
	public static void cleanAndKeepOnlyMetadata(File eclipse) {
		List<File> notDeletedYet = new ArrayList<File>();

		// Check that it is an eclipse
		if (eclipse.exists() && new EclipseAdapter().isAdaptable(eclipse.toURI(), new NullProgressMonitor())) {

			// first remove everything that is not plugins or features
			List<File> filesToRemove = new ArrayList<File>();
			for (File f : eclipse.listFiles()) {
				if (f.getName().equals(VariantsUtils.FEATURES) || f.getName().equals(VariantsUtils.PLUGINS)) {
					// we keep them
				} else {
					filesToRemove.add(f);
				}
			}
			for (File f : filesToRemove) {
				FileUtils.deleteFile(f);
			}

			// remove everything that it is not metadata from plugins and
			// features
			filesToRemove.clear();

			// plugins
			File pluginsFolder = new File(eclipse, VariantsUtils.PLUGINS);
			for (File plu : pluginsFolder.listFiles()) {
				// take only directories because we want to keep jars
				if (plu.isDirectory()) {
					List<File> files = FileUtils.getAllFiles(plu);
					for (File f : files) {
						if (!isMetadataFile(f)) {
							filesToRemove.add(f);
						}
					}
				}
			}
			for (File f : filesToRemove) {
				FileUtils.deleteFile(f);
			}
			filesToRemove.clear();

			// features
			File featuresFolder = new File(eclipse, VariantsUtils.FEATURES);
			for (File feat : featuresFolder.listFiles()) {
				if (feat.isDirectory()) {
					List<File> files = FileUtils.getAllFiles(feat);
					for (File f : files) {
						if (!isMetadataFile(f)) {
							filesToRemove.add(f);
						}
					}
				}
			}

			for (File f : filesToRemove) {
				FileUtils.deleteFile(f);
			}
			filesToRemove.clear();

			// plugins and features folder, unzip folders, remove everything
			// that it is not xml or properties

			File[] jars = FileAndDirectoryUtils.getAllJarsInDirectory(pluginsFolder);
			for (File fi : jars) {
				File output = new File(pluginsFolder,
						fi.getName().substring(0, fi.getName().length() - ".jar".length()));
				output.mkdirs();
				ZipUtils.unJarOnlyFilesOfGivenExtensions(fi, output, metaDataExtensions);
				try {
					FileUtils.deleteFile(fi);
				} catch (Exception e) {
					// try to delete later
					notDeletedYet.add(fi);
					continue;
				}
			}
		}

		// delete
		for (File f : notDeletedYet) {
			if (f.exists()) {
				FileUtils.deleteFile(f);
			}
		}
	}

	public final static String[] metaDataExtensions = new String[] { "xml", "properties", "MF" };

	public static boolean isMetadataFile(File file) {
		String extension = FileUtils.getExtension(file);
		for (String metaDataExtension : metaDataExtensions) {
			if (extension.equals(metaDataExtension)) {
				return true;
			}
		}
		return false;
	}

}