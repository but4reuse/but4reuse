package org.but4reuse.adapters.images.utils;

/**
 * @author Arthur
 * 
 *         This class couples colors name with R,G,B value.
 */

public class ColorName {

	/**
	 * The red color value.
	 */

	public int r;
	/**
	 * The green color value.
	 */

	public int g;
	/**
	 * The blue color value.
	 */
	public int b;

	/**
	 * The name of the color.
	 */
	public String name;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            The color name.
	 * @param r
	 *            The red quantity in the color.
	 * @param g
	 *            The green quantity in the color.
	 * @param b
	 *            The blue quantity in the color.
	 */
	public ColorName(String name, int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.name = name;
	}

	/**
	 * 
	 * @return The red quantity in the color.
	 */
	public int getR() {
		return r;
	}

	/***
	 * 
	 * @return The green quantity in the color.
	 */
	public int getG() {
		return g;
	}

	/**
	 * 
	 * @return The blue quantity in the color.
	 */
	public int getB() {
		return b;
	}

	/**
	 * 
	 * @return The color name.
	 */
	public String getName() {
		return name;
	}

}