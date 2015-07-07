package org.but4reuse.wordclouds.util;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Cloud.Case;
import org.mcavallo.opencloud.Tag;

/**
 * @author Arthur A Toolbox to draw word cloud
 */
public class WordCloudUtil {

	/**
	 * Draw the word cloud cloud in the canvas can.
	 * 
	 * @param can
	 *            The canvas where the string will be drawn.
	 * @param cloud
	 *            This cloud contains strings that you want to draw in your
	 *            canvas.
	 */

	public static void drawWordCloud(Canvas can, Cloud cloud) {
		int x = 10, y = 10;
		int maxH = 0;
		GC gc = new GC(can);
		can.redraw();
		can.update();

		for (Tag t : cloud.tags()) {
			Font f = new Font(Display.getCurrent(), "Arial", t.getWeightInt(), SWT.ITALIC);
			gc.setFont(f);
			/*
			 * In the following lines we try to find the size in pixel of each
			 * string in order to not draw several strings in the same place. We
			 * use the length of the string and chars to define the height and
			 * width of the string.
			 */

			int width = (int) (t.getName().length() * t.getWeightInt() * 0.80);
			width += (int) (20 * 1.0 / t.getName().length());

			/*
			 * M,m,W,w are larger than other letters when we find one of them in
			 * the string we add some pixel in the string length in pixel
			 */

			for (int i = 1; i < t.getName().length(); i++)
				if (t.getName().charAt(i) == 'w' || t.getName().charAt(i) == 'm')
					width += t.getWeightInt() * 0.5;

			if (t.getName().charAt(0) == 'W' || t.getName().charAt(0) == 'M')
				width += t.getWeightInt() * 0.6;

			/*
			 * if a part of the string will be out of the canvas we draw the
			 * string below others strings
			 */

			if (x + width > can.getSize().x - 25) {
				x = 10;
				y += maxH;
				maxH = 0;
			}
			/*
			 * p,q,g,j are bigger than other letters so when we find one of them
			 * we add pixel in the string height in pixel
			 */
			int height = (int) (1.25 * t.getWeightInt());
			height += (int) (t.getWeightInt() / 5);
			if (t.getName().contains("p") || t.getName().contains("q") || t.getName().contains("g")
					|| t.getName().contains("j")) {
				height += (int) (0.3 * t.getWeightInt());
			}

			if (maxH < height)
				maxH = height;

			gc.drawString(t.getName(), x, y);
			x += width;
		}
	}

	public static void drawWordCloudIDF(Canvas can, List<Cloud> clouds, int ind_cloud) {
		
		int nbBlock_isPresent = 0;
		int nbBlock = clouds.size();

		Cloud cloud = clouds.get(ind_cloud);
		Cloud cloud_IDF = new Cloud(Case.CAPITALIZATION);

		cloud_IDF.setMinWeight(5);
        cloud_IDF.setMinWeight(50);
		for (Tag tag : cloud.tags()) {
			nbBlock_isPresent = nbCloudsContainTag(clouds, tag);
			double idf = (1.0 * nbBlock) / nbBlock_isPresent;
			Tag t = new Tag(tag.getName(),idf);
			cloud_IDF.addTag(t);
			
		}

		drawWordCloud(can, cloud_IDF);

	}

	private static int nbCloudsContainTag(List<Cloud> clouds, Tag tag) {
		int cpt = 0;
		for (Cloud c : clouds) {
			for (Tag t : c.tags()) {
				if (t.getName().equalsIgnoreCase(tag.getName())) {
					cpt++;
					break;
				}
			}
		}
		return cpt;
	}
}
