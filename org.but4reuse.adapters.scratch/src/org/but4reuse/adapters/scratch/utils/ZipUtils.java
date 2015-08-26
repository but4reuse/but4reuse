package org.but4reuse.adapters.scratch.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
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
}
