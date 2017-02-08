package org.but4reuse.adapters.eclipse.generator.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.utils.files.FileUtils;

/**
 * File and directory utils
 * 
 * @author Julien Margarido
 * @author Felix Lima Gorito
 */
public class FileAndDirectoryUtils {

	/**
	 * Return all the ".jar" files contains in a directory, null if the
	 * parameter is null or not a directory.
	 * 
	 * @param dir
	 *            : The directory
	 */
	public static File[] getAllJarsInDirectory(File dir) {
		if (dir == null || dir.isFile())
			return null;
		return dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".jar");
			}
		});
	}

	/**
	 * Copy all directories in parameter to an output.
	 * 
	 * @param output
	 *            : The destination
	 * @param files
	 *            : All the files to copy
	 * @throws IOException
	 *             if the copy is impossible
	 */
	public static void copyFilesAndDirectories(File output, File... files) throws IOException {
		for (File fic : files) {
			if (fic.isDirectory()) {
				FileUtils.copyDirectoryToDirectory(fic, output);
			} else {
				FileUtils.copyFileToDirectory(fic, output);
			}
		}
	}

	/**
	 * Delete the file, if exist.
	 * 
	 * @param outputFile
	 *            : The files to copy
	 * @throws IOException
	 *             if delete is impossible
	 */
	public static void deleteFile(File file) throws IOException {
		if (file == null || !file.exists()) {
			throw new IOException("(Method deleteFileIfExist) file is null or not exist.");
		} else {
			file.delete();
		}
	}

	/**
	 * Return a random (based on the percentage parameter) list of File, from
	 * one list. For example, getSomeFiles( [2,8,9,1,13], 50 ) can return [8, 1,
	 * 13].
	 * 
	 * @param listFiles
	 *            : The File list
	 * @param percentage
	 *            : The random (between 0 and 100%)
	 * @return
	 */
	public static File[] getSomeFiles(File[] listFiles, int percentage) {
		if (listFiles != null || percentage == 100) {
			List<File> res = new ArrayList<>();
			for (File file : listFiles) {
				if (Math.random() * 100 < percentage) {
					res.add(file);
				}
			}
			return res.toArray(new File[res.size()]);
		}
		return listFiles;
	}
}
