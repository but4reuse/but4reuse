package org.but4reuse.wordclouds.util;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
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
	 * @param cmp
	 *            The ScrolledComposite the string will be drawn.
	 * @param cloud
	 *            This cloud contains strings that you want to draw in your
	 *            canvas.
	 */

	public static void drawWordCloud(Composite cmp, Cloud cloud) {
		int x = 10, y = 10;
		int maxH = 0;
		for(Control c : cmp.getChildren())
			c.dispose();
		cmp.update();
		int spaceHint = 5;
		for (Tag t : cloud.tags()) {
			
			Label l = new Label(cmp,SWT.NORMAL);
			Font f = new Font(Display.getCurrent(), "Arial", t.getWeightInt(), SWT.ITALIC);
			l.setFont(f);
			l.setText(t.getName());
			l.pack();
			
			if (x + l.getBounds().width > 500 - 25) {
				x = 10;
				y += maxH + spaceHint;
				maxH = 0;
			}
			
			if (maxH < l.getBounds().height)
				maxH = l.getBounds().height;
			
			l.setLocation(x, y);
			x += spaceHint+l.getBounds().width;
		}
		
	
		
	}

	/**
	 * The same method than drawWordCloud but here we use inverse documents frequency
	 * to define the size for strings.
	 * @param cmp The composite where the word cloud will be drawn.
	 * @param clouds The clouds list.
	 * @param ind_cloud The index for the cloud that we want to draw.
	 */
	public static void drawWordCloudIDF(Composite cmp, List<Cloud> clouds, int ind_cloud) {
		
		int nbBlock_isPresent = 0;
		int nbBlock = clouds.size();
	
		Cloud cloud = clouds.get(ind_cloud);
		Cloud cloud_IDF = new Cloud(Case.CAPITALIZATION);

		cloud_IDF.setMinWeight(5);
        cloud_IDF.setMinWeight(50);
		for (Tag tag : cloud.tags()) {
			nbBlock_isPresent = nbCloudsContainTag(clouds, tag);
			double idf = Math.log((1.0 * nbBlock) / nbBlock_isPresent);
			double score = tag.getScore()*idf;
			
			Tag t = new Tag(tag.getName(),score);
			cloud_IDF.addTag(t);
			
		}
		
		
		drawWordCloud(cmp, cloud_IDF);

	}

	/**
	 * Count how many cloud contain the Tag tag
	 * @param clouds Cloud list where we search.
	 * @param tag The tag that we want to find.
	 * @return How many time we find the tag.
	 */
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
