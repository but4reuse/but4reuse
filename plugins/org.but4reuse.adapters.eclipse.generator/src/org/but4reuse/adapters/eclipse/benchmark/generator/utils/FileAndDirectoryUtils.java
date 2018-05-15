package org.but4reuse.adapters.eclipse.benchmark.generator.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * File and directory utils
 * 
 * @author Julien Margarido
 * @author Felix Lima Gorito
 */
public class FileAndDirectoryUtils {

	/**
	 * Return all the ".jar" files contained in a directory, null if the
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
			List<File> res = new ArrayList<File>();
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
