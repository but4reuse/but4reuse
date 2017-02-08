package org.but4reuse.adapters.images;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.images.utils.ImageUtils;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;

/**
 * Image adapter
 * 
 * @author jabier.martinez
 */
public class ImageAdapter implements IAdapter {

	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		File file = FileUtils.getFile(uri);
		if (file != null && file.exists() && !file.isDirectory() && ImageUtils.isImageFile(file.getName())) {
			return true;
		}
		return false;
	}

	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		List<IElement> elements = new ArrayList<IElement>();
		File file = FileUtils.getFile(uri);
		Image image = ImageUtils.getImage(file.getAbsolutePath());
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
				// Only add if it is not completely transparent
				if (pixel.alpha != 0) {
					pixel.position = new Point(x, y);
					// A pixel depends on its position
					pixel.addDependency(new PositionDependencyObject(pixel.position));
					elements.add(pixel);
				}
			}
		}
		return elements;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		try {
			// Use the given file or use a default name if a folder was given
			if (uri.toString().endsWith("/")) {
				uri = new URI(uri.toString() + "construct.png");
			}
			// Create file if it does not exist
			File file = FileUtils.getFile(uri);
			FileUtils.createFile(file);

			// Calculate max width and max height
			int maxWidth = -1;
			int maxHeight = -1;
			for (IElement element : elements) {
				if (element instanceof PixelElement) {
					PixelElement pElement = (PixelElement) element;
					if (maxWidth < pElement.position.x) {
						maxWidth = pElement.position.x;
					}
					if (maxHeight < pElement.position.y) {
						maxHeight = pElement.position.y;
					}
				}
			}

			// Start creating the image
			int IMAGE_DEPTH = 32;
			ImageData imageData = ImageUtils
					.createEmptyDirectPaletteImageData(maxWidth + 1, maxHeight + 1, IMAGE_DEPTH);
			for (IElement element : elements) {
				if (element instanceof PixelElement) {
					PixelElement pElement = (PixelElement) element;
					imageData.setAlpha(pElement.position.x, pElement.position.y, pElement.alpha);
					imageData.setPixel(pElement.position.x, pElement.position.y, pElement.color.hashCode());
				}
			}

			// Save it
			ImageUtils.saveImageToFile(imageData, file.getAbsolutePath(), SWT.IMAGE_PNG);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
