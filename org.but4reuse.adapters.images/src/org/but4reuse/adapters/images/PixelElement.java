package org.but4reuse.adapters.images;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;

/**
 * Pixel Element
 * 
 * @author jabier.martinez
 */
public class PixelElement extends AbstractElement {

	public RGB color;
	public int alpha;
	public Point position;

	@Override
	public String getText() {
		String text = position + ", ";
		// if completely transparent, do not show the color
		if (alpha == 0) {
			text = text + "Alpha: 0";
		} else {
			text = text + color;
			// show alpha only if it is not completely opaque
			if (alpha != 255) {
				text = text + ", Alpha: " + alpha;
			}
		}
		return text;
	}

	@Override
	public int hashCode() {
		return position.hashCode();
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof PixelElement) {
			PixelElement pixel = (PixelElement) anotherElement;
			if (position.equals(pixel.position) && color.equals(pixel.color) && alpha == pixel.alpha) {
				return 1;
			}
		}
		return 0;
	}
}
