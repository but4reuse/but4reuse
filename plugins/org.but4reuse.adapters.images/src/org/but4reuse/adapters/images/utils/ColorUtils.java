package org.but4reuse.adapters.images.utils;

import java.util.ArrayList;
import org.eclipse.swt.graphics.RGB;

/**
 * @author Arthur
 * 
 *         This is a toolbox to find colors names when you have red, green and
 *         blue quantities of the color.
 */
public class ColorUtils {

	/**
	 * The current ColorUtils.
	 */
	private static ColorUtils singleton = null;

	/**
	 * A ColorName list.
	 */
	private ArrayList<ColorName> colorList;

	/**
	 * This method creates the current ColorUtils(It creates the singleton).
	 */
	public static void init() {
		if (singleton == null) {
			singleton = new ColorUtils();
			singleton.initColorList();
		}
	}

	/**
	 * Check if the singleton have been created.
	 * 
	 * @return True is singleton is not null false otherwise.
	 */
	public static boolean isInit() {
		return singleton != null;
	}

	/**
	 * Create a new color list.
	 * 
	 * @return The color list.
	 */
	private ArrayList<ColorName> initColorList() {
		colorList = new ArrayList<ColorName>();
		colorList.add(new ColorName("Aquamarine", 112, 219, 147));
		colorList.add(new ColorName("Azure", 240, 255, 255));
		colorList.add(new ColorName("Beige", 245, 245, 220));
		colorList.add(new ColorName("Black", 0, 0, 0));
		colorList.add(new ColorName("Blue", 0, 0, 255));
		colorList.add(new ColorName("Brown", 165, 42, 42));
		colorList.add(new ColorName("Chocolate", 210, 105, 30));
		colorList.add(new ColorName("Coral", 255, 127, 0));
		colorList.add(new ColorName("Cyan", 0, 255, 255));
		colorList.add(new ColorName("DarkGreen", 47, 79, 47));
		colorList.add(new ColorName("DarkOrange", 255, 140, 0));
		colorList.add(new ColorName("DarkSeaGreen", 143, 188, 143));
		colorList.add(new ColorName("DarkTurquoise", 0, 206, 209));
		colorList.add(new ColorName("DarkViolet", 148, 0, 211));
		colorList.add(new ColorName("DeepPink", 255, 20, 147));
		colorList.add(new ColorName("DeepSkyBlue", 0, 191, 255));
		colorList.add(new ColorName("ForestGreen", 34, 139, 34));
		colorList.add(new ColorName("Fuchsia", 255, 0, 255));
		colorList.add(new ColorName("GhostWhite", 248, 248, 255));
		colorList.add(new ColorName("Gold", 255, 215, 0));
		colorList.add(new ColorName("GoldenRod", 219, 219, 112));
		colorList.add(new ColorName("Gray", 190, 190, 190));
		colorList.add(new ColorName("Green", 0, 255, 0));
		colorList.add(new ColorName("GreenYellow", 173, 255, 47));
		colorList.add(new ColorName("HotPink", 255, 105, 180));
		colorList.add(new ColorName("IndianRed", 205, 92, 92));
		colorList.add(new ColorName("Indigo", 111, 0, 255));
		colorList.add(new ColorName("Ivory", 255, 255, 240));
		colorList.add(new ColorName("Khaki", 240, 230, 140));
		colorList.add(new ColorName("Lavender", 230, 230, 250));
		colorList.add(new ColorName("LemonChiffon", 255, 250, 205));
		colorList.add(new ColorName("LightBlue", 173, 216, 230));
		colorList.add(new ColorName("LightCoral", 240, 128, 128));
		colorList.add(new ColorName("LightCyan", 224, 255, 255));
		colorList.add(new ColorName("LightGray", 211, 211, 211));
		colorList.add(new ColorName("LightPink", 255, 182, 193));
		colorList.add(new ColorName("LightSeaGreen", 32, 178, 170));
		colorList.add(new ColorName("LightSkyBlue", 176, 226, 255));
		colorList.add(new ColorName("LightYellow", 255, 255, 224));
		colorList.add(new ColorName("LimeGreen", 50, 205, 50));
		colorList.add(new ColorName("Magenta", 255, 0, 255));
		colorList.add(new ColorName("Maroon", 176, 48, 96));
		colorList.add(new ColorName("MidnightBlue", 25, 25, 112));
		colorList.add(new ColorName("Navy", 0, 0, 128));
		colorList.add(new ColorName("Orange", 255, 165, 0));
		colorList.add(new ColorName("OrangeRed", 255, 69, 0));
		colorList.add(new ColorName("PaleGreen", 152, 251, 152));
		colorList.add(new ColorName("PaleTurquoise", 187, 238, 238));
		colorList.add(new ColorName("PaleVioletRed", 219, 112, 147));
		colorList.add(new ColorName("PeachPuff", 255, 218, 185));
		colorList.add(new ColorName("Pink", 255, 192, 203));
		colorList.add(new ColorName("Purple", 128, 0, 128));
		colorList.add(new ColorName("Red", 255, 0, 0));
		colorList.add(new ColorName("SeaGreen", 46, 139, 87));
		colorList.add(new ColorName("SeaShell", 255, 245, 238));
		colorList.add(new ColorName("Sienna", 142, 107, 35));
		colorList.add(new ColorName("Silver", 230, 232, 250));
		colorList.add(new ColorName("SkyBlue", 135, 206, 235));
		colorList.add(new ColorName("Snow", 255, 250, 250));
		colorList.add(new ColorName("SpringGreen", 0, 255, 127));
		colorList.add(new ColorName("SteelBlue", 35, 107, 142));
		colorList.add(new ColorName("Thistle", 216, 191, 216));
		colorList.add(new ColorName("Tomato", 255, 99, 71));
		colorList.add(new ColorName("Turquoise", 64, 224, 208));
		colorList.add(new ColorName("Violet", 238, 130, 238));
		colorList.add(new ColorName("Wheat", 245, 222, 179));
		colorList.add(new ColorName("White", 255, 255, 255));
		colorList.add(new ColorName("WhiteSmoke", 245, 245, 245));
		colorList.add(new ColorName("Yellow", 255, 255, 0));

		return colorList;
	}

	/**
	 * It takes the different amounts of red,green and blue and returns the
	 * color names associates with these values.
	 * 
	 * @param r
	 *            The red quantity in the color.
	 * @param g
	 *            The green quantity in the color.
	 * @param b
	 *            The blue quantity in the color.
	 * @return The color name associates with quantities you gave as parameter .
	 */
	private String getColorNameFromRgb(int r, int g, int b) {
		ColorName color = null;
		int distMin = Math.abs(r - colorList.get(0).r) + Math.abs(g - colorList.get(0).g)
				+ Math.abs(b - colorList.get(0).b);
		;

		for (ColorName c : colorList) {
			int dist = Math.abs(r - c.r) + Math.abs(g - c.g) + Math.abs(b - c.b);
			if (dist < distMin) {
				distMin = dist;
				color = c;
			}
		}

		return color.name;

	}

	/**
	 * It takes an RGB object and returns the color names associates with the
	 * RGB object.
	 * 
	 * @param color
	 *            The color whose you want the name.
	 */
	public static String getColorName(RGB color) {
		return singleton.getColorNameFromRgb(color.red, color.green, color.blue);
	}
}
