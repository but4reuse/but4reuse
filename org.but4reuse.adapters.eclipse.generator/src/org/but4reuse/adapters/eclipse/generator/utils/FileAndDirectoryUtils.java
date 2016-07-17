package org.but4reuse.adapters.eclipse.generator.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

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
	 * Return all the subdirectories files contains in a directory, null if the
	 * parameter is null or not a directory.
	 * 
	 * @param dir
	 *            : The directory
	 */
	public static File[] getAllSubDirectories(File dir) {
		if (dir == null || dir.isFile())
			return null;
		return dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
	}

	/**
	 * Copy a directory to an output, with a filter.
	 * 
	 * @param dir
	 *            : The directory to copy
	 * @param output
	 *            : The destination
	 * @param filter
	 *            : The FileFilter
	 * @throws IOException
	 *             if the copy is impossible
	 */
	public static void copyDirectoryWithFilters(File dir, String output, FileFilter filter) throws IOException {
		try {
			File newDir = new File(output + File.separator + dir.getName());
			if (filter == null)
				FileUtils.copyDirectory(dir, newDir);
			else
				FileUtils.copyDirectory(dir, newDir, filter);

		} catch (IOException exc) {
			throw new IOException("(Method copyDirectoryWithFilters) Copy impossible because : " + exc);
		}
	}

	/**
	 * Copy a directory to an output.
	 * 
	 * @param dir
	 *            : The directory to copy
	 * @param output
	 *            : The destination
	 * @throws IOException
	 *             if the copy is impossible
	 */
	public static void copyDirectory(File dir, String output) throws IOException {
		try {
			File newDir = new File(output + File.separator + dir.getName());
			FileUtils.copyDirectory(dir, newDir);

		} catch (IOException exc) {
			throw new IOException("(Method copyDirectory) Copy impossible because : " + exc);
		}
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
	public static void copyFilesAndDirectories(String output, File... files) throws IOException {
		try {
			for (File fic : files) {
				if (fic.isDirectory())
					copyDirectory(fic, output);
				else
					FileUtils.copyFileToDirectory(fic, new File(output));
			}
		} catch (IOException exc) {
			throw new IOException("(Method copyFilesAndDirectories) Copy impossible because : " + exc);
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
			FileUtils.forceDelete(file);
		}
	}

	/**
	 * Return a random (based on the percentage parameter) list of File, from
	 * one liste. For example, getSomeFiles( [2,8,9,1,13], 50 ) can return [8,
	 * 1, 13].
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
				if (Math.random() * 100 < percentage)
					res.add(file);
			}
			return res.toArray(new File[res.size()]);
		}
		return listFiles;
	}
}
