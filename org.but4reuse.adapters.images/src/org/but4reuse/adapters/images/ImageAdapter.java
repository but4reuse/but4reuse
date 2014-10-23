package org.but4reuse.adapters.images;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Image adapter
 * @author jabier.martinez
 */
public class ImageAdapter implements IAdapter {
	public static final String[] IMAGE_EXTENSIONS = { "*.gif", "*.png", "*.bmp", "*.jpg" };

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

	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		File file = FileUtils.getFile(uri);
		if (file != null && file.exists() && !file.isDirectory() && isImageFile(file.getName())) {
			return true;
		}
		return false;
	}

	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		List<IElement> elements = new ArrayList<IElement>();
		File file = FileUtils.getFile(uri);
		Image image = getImage(file.getAbsolutePath());
		ImageData imageData = image.getImageData();

		for (int y = 0; y < imageData.height; y++) {
			for (int x = 0; x < imageData.width; x++) {
				PixelElement pixel = new PixelElement();
				int paletteInt = imageData.getPixel(x, y);
				RGB rgb = imageData.palette.getRGB(paletteInt);
				pixel.color = rgb;

				// Take care of transparency types
				if (imageData.getTransparencyType() == SWT.TRANSPARENCY_PIXEL
						&& imageData.transparentPixel == paletteInt) {
					pixel.alpha = 0;
				} else {
					// getAlpha return the correct value or if there is not
					// alphaData it returns 255
					pixel.alpha = imageData.getAlpha(x, y);
				}
				pixel.position = new Point(x, y);
				elements.add(pixel);
			}
		}
		return elements;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		// TODO construct image
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Construction",
						"For the moment, construction is not available");
			}
		});
	}
}
