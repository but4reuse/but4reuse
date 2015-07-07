package org.but4reuse.adapters.images.utils;

import java.io.FileInputStream;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Image Utils
 * 
 * @author jabier.martinez
 */
public class ImageUtils {

	public static final String[] IMAGE_EXTENSIONS = { "*.gif", "*.png", "*.bmp", "*.jpg" };

	/**
	 * Save image to file
	 * 
	 * @param imageData
	 * @param imagePath
	 * @param format
	 */
	public static void saveImageToFile(ImageData imageData, String imagePath, int format) {
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { imageData };
		loader.save(imagePath, format);
	}

	/**
	 * Check if a file is an image based on the extension
	 * 
	 * @param fileName
	 * @return true if it is an image
	 */
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

	/**
	 * Get image from absolute path
	 * 
	 * @param absolutePath
	 * @return
	 */
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

	/**
	 * Create an empty direct palette image data
	 * 
	 * @param width
	 * @param height
	 * @param depth
	 * @return imageData
	 */
	public static ImageData createEmptyDirectPaletteImageData(int width, int height, int depth) {
		// direct palette with standard rgb masks
		PaletteData paletteData = new PaletteData(0xff, 0xff00, 0xff0000);
		ImageData newImageData = new ImageData(width, height, depth, paletteData);
		// transparency, otherwise it starts in black
		byte[] alphaData = new byte[width * height];
		for (int i = 0; i < alphaData.length; i++) {
			// 0 means transparent
			alphaData[i] = 0;
		}
		newImageData.alphaData = alphaData;
		return newImageData;
	}

	/**
	 * Color similarity based on euclidean distance
	 * 
	 * @param color1
	 * @param color2
	 * @return a range from 0 to 1: 0 for black to white, 1 for completely equal
	 */
	public static double getColorSimilarity(RGB color1, RGB color2) {
		int r1 = color1.red;
		int r2 = color2.red;
		int g1 = color1.green;
		int g2 = color2.green;
		int b1 = color1.blue;
		int b2 = color2.blue;
		double distance = Math.sqrt(Math.pow(r2 - r1, 2) + Math.pow(g2 - g1, 2) + Math.pow(b2 - b1, 2));
		double percentageDifference = distance / Math.sqrt(Math.pow(255, 2) + Math.pow(255, 2) + Math.pow(255, 2));
		return 1 - percentageDifference;
	}
}
