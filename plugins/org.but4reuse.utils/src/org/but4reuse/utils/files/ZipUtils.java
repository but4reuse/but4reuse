package org.but4reuse.utils.files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Zip Utils
 * 
 */
public class ZipUtils {

	/**
	 * Create zip output stream
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public static ZipOutputStream constructZIP(String path) throws FileNotFoundException {
		FileOutputStream dest = new FileOutputStream(path);
		BufferedOutputStream buff = new BufferedOutputStream(dest);
		ZipOutputStream out = new ZipOutputStream(buff);
		return out;
	}

	/**
	 * Add a file to a zip file
	 * 
	 * @param zipOut
	 * @param file
	 * @param filename
	 * @throws Exception
	 */
	public static void addFileToZip(ZipOutputStream zipOut, File file, String filename) throws Exception {
		final int BUFFER = 2048;
		byte data[] = new byte[BUFFER];

		FileInputStream fi = new FileInputStream(file);
		BufferedInputStream buffi = new BufferedInputStream(fi, BUFFER);
		ZipEntry entry = new ZipEntry(filename);
		zipOut.putNextEntry(entry);
		int count;
		while ((count = buffi.read(data, 0, BUFFER)) != -1) {
			zipOut.write(data, 0, count);
		}
		zipOut.closeEntry();
		buffi.close();
	}

	/**
	 * unZip
	 * 
	 * @param zipFile
	 * @param outputFolder
	 */
	public static void unZip(File zipFile, File outputFolder) {
		byte[] buffer = new byte[1024];
		try {
			// get the zip file content
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator + fileName);
				// create all non exists folders
				// else you will hit FileNotFoundException for compressed folder
				if (ze.isDirectory()) {
					newFile.mkdirs();
				} else {
					newFile.getParentFile().mkdirs();
					try {
						FileOutputStream fos = new FileOutputStream(newFile);
						int len;
						while ((len = zis.read(buffer)) > 0) {
							fos.write(buffer, 0, len);
						}
						fos.close();
					} catch (Exception e) {
						// ouch, at least continue with the next...
						e.printStackTrace();
					}
				}
				// go for the next
				ze = zis.getNextEntry();
			}
			// close
			zis.closeEntry();
			zis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void unJar(File jarFile, File outputFolder) {
		try {
			JarFile jar = new JarFile(jarFile);
			Enumeration<JarEntry> enumEntries = jar.entries();
			while (enumEntries.hasMoreElements()) {
				JarEntry file = enumEntries.nextElement();
				File f = new File(outputFolder + java.io.File.separator + file.getName());
				f.getParentFile().mkdirs();
				if (file.isDirectory()) { // if its a directory, create it
					f.mkdirs();
					continue;
				}
				InputStream is = jar.getInputStream(file); // get the input
															// stream
				FileOutputStream fos = new FileOutputStream(f);
				while (is.available() > 0) { // write contents of 'is' to 'fos'
					fos.write(is.read());
				}
				fos.close();
				is.close();
			}
			jar.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void unJarOnlyFilesOfGivenExtensions(File jarFile, File outputFolder, String[] extensions) {
		try {
			JarFile jar = new JarFile(jarFile);
			Enumeration<JarEntry> enumEntries = jar.entries();
			while (enumEntries.hasMoreElements()) {
				JarEntry file = enumEntries.nextElement();
				boolean validExtension = false;
				for (String extension : extensions) {
					if (file.getName() != null && FileUtils.getExtension(file.getName()).equalsIgnoreCase(extension)) {
						validExtension = true;
					}
				}
				if (validExtension) {
					File f = new File(outputFolder + java.io.File.separator + file.getName());
					f.getParentFile().mkdirs();
					if (file.isDirectory()) {
						f.mkdirs();
						continue;
					}
					InputStream is = jar.getInputStream(file); 
					FileOutputStream fos = new FileOutputStream(f);
					while (is.available() > 0) { 
						fos.write(is.read());
					}
					fos.close();
					is.close();
				}
			}
			jar.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
