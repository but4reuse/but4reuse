package org.but4reuse.adapters.images.utils;

import java.io.FileInputStream;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Display;

/**
 * Image Utils
 * @author jabier.martinez
 */
public class ImageUtils {
	
	public static final String[] IMAGE_EXTENSIONS = { "*.gif", "*.png", "*.bmp", "*.jpg" };
	
	public static void saveImageToFile(ImageData imageData, String imagePath, int format) {
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { imageData };
		loader.save(imagePath, format);
	}

	
	public static boolean isImageFile(String fileName) {
		int dot = fileName.lastIndexOf(".");
		if (dot == -1) {
			return false;
		} else {
			String fileExt = fileName.substring(dot, fileName.length());
			for (String extension : IMAGE_EXTENSIONS) {
				// remove *
				extension = extension.substring(1);
				if (fileExt.equalsIgnoreCase(extension)) {
					return true;
				}
			}
		}
		return false;
	}

	
	public static Image getImage(String absolutePath) {
		try {
			if (absolutePath == null) {
				return null;
			}
			return new Image(Display.getDefault(), new FileInputStream(absolutePath));
		} catch (Exception e) {
			// If any exception happens return null
			return null;
		}
	}
	
	public static ImageData createEmptyDirectPaletteImageData(int width, int height, int depth) {
		// direct palette with standard rgb masks
		PaletteData paletteData = new PaletteData(0xff, 0xff00, 0xff0000);
		ImageData newImageData = new ImageData(width, height, depth, paletteData);
		// transparency, otherwise it starts in black
		byte[] alphaData = new byte[width * height];
		for (int i = 0; i < alphaData.length; i++){
			// 0 means transparent
			alphaData[i] = 0;
		}
		newImageData.alphaData = alphaData;
		return newImageData;
	}
}
