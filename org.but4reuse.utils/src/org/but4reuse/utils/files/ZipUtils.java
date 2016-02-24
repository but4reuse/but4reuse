package org.but4reuse.utils.files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
	public static ZipOutputStream constructZIP(String path) throws FileNotFoundException {
		FileOutputStream dest = new FileOutputStream(path);
		BufferedOutputStream buff = new BufferedOutputStream(dest);
		ZipOutputStream out = new ZipOutputStream(buff);
		return out;
	}

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
	 * Unzip it
	 * 
	 * @param zipFile
	 *            input zip file
	 * @param output
	 *            zip file output folder
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
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
