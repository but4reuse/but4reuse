package org.but4reuse.adapters.scratch.utils;

import java.io.File;
import java.net.URL;
import java.util.zip.ZipOutputStream;

import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.files.ZipUtils;

import com.eclipsesource.json.JsonObject;

public class ScratchUtils {
	public static boolean downloadFile(String md5, File file) {
		try {
			URL url = new URL("http://assets.scratch.mit.edu/internalapi/asset/" + md5 + "/get/");
			return FileUtils.downloadFileFromURL(url, file);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void costume(ZipOutputStream zipOut, JsonObject costume, int id) throws Exception {
		String md5 = costume.get("baseLayerMD5").toString().replace("\"", "");
		String extension = md5.substring(md5.lastIndexOf("."));
		String filename = id + extension;

		File file = new File("tmp");
		ScratchUtils.downloadFile(md5, file);

		ZipUtils.addFileToZip(zipOut, file, filename);
		file.delete();
		costume.set("baseLayerID", id);
	}

	public static void sound(ZipOutputStream zipOut, JsonObject sound, int id) throws Exception {
		String md5 = sound.get("md5").toString().replace("\"", "");
		String extension = md5.substring(md5.lastIndexOf("."));
		String filename = id + extension;

		File file = new File("tmp");
		ScratchUtils.downloadFile(md5, file);

		ZipUtils.addFileToZip(zipOut, file, filename);
		file.delete();
		sound.set("soundID", id);
	}
}
