package org.but4reuse.utils.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

/**
 * File Utils class
 * @author jabier.martinez
 */
public class FileUtils {
	
	/**
	 * Try to return the expected icon for a file name
	 * @param fileName
	 * @return
	 */
	public static ImageDescriptor getIconFromFileName(String fileName){
		if(fileName!=null && fileName.contains(".")) {
			String extension = fileName.substring(fileName.lastIndexOf("."));
			return PlatformUI.getWorkbench().getEditorRegistry().getImageDescriptor("random."+extension);
		}
		return null;
	}
	
	/**
	 * Get an image from a plugin
	 * @param pluginID
	 * @param imagePath for example "/icons/myIcon.gif"
	 * @return an imageDescriptor
	 */
	public static ImageDescriptor getImageFromPlugin(String pluginID, String imagePath) {
		final Bundle pluginBundle = Platform.getBundle(pluginID);
		final Path imageFilePath = new Path(imagePath);
		final URL imageFileUrl = FileLocator.find(pluginBundle, imageFilePath, null);
		return ImageDescriptor.createFromURL(imageFileUrl);
	}
	
	/**
	 * Try to return a file related to a uri
	 * @param uri
	 * @return
	 */
	public static File getFile(URI uri) {
		File file = null;
		if (uri.getScheme().equals("file")) {
			file = new File(uri);
		} else if (uri.getScheme().equals("platform")){
			String path = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
			path = path + uri.toString().substring("platform:/resource".length());
			file = new File(path);
		}
		return file;
	}

	/**
	 * Create file if it does not exist
	 * @param file
	 * @throws IOException
	 */
	public static void createFile(File file) throws IOException {
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}
	}
	
	/**
	 * Append line to file
	 * @param file
	 * @param text
	 * @throws Exception
	 */
	public static void appendToFile(File file, String text) throws Exception {
		BufferedWriter output;
		output = new BufferedWriter(new FileWriter(file, true));
		output.append(text);
		output.newLine();
		output.close();
	}
	
}
