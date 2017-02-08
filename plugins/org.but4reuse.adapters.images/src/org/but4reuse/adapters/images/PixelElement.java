package org.but4reuse.adapters.images;

import java.util.ArrayList;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.images.utils.ColorUtils;
import org.but4reuse.adapters.images.utils.ImageUtils;
import org.but4reuse.adapters.images.utils.PixelManualComparison;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.impl.IManualComparison;
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
		String text = position + ", " + color;
		// show alpha only if it is not completely opaque
		if (alpha != 255) {
			text = text + ", Alpha: " + alpha;
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
			if (position.equals(pixel.position) && alpha == pixel.alpha) {
				return ImageUtils.getColorSimilarity(color, pixel.color);
			}
		}
		return 0;
	}

	@Override
	public IManualComparison getManualComparison(double calculatedSimilarity, IElement anotherElement) {
		if (anotherElement instanceof PixelElement) {
			return new PixelManualComparison(calculatedSimilarity, this, (PixelElement) anotherElement);
		} else {
			return super.getManualComparison(calculatedSimilarity, anotherElement);
		}
	}

	@Override
	public ArrayList<String> getWords() {
		/*
		 * We initiate the ColorUtil if if we need to do it.
		 */
		ArrayList<String> words = new ArrayList<String>();
		if (!ColorUtils.isInit())
			ColorUtils.init();

		String name = ColorUtils.getColorName(color);
		if (!name.equals("Erreur"))
			words.add(name);
		return words;
	}

}
