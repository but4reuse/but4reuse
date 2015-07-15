package org.but4reuse.wordclouds.feature.location;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.wordclouds.util.WordCloudUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Cloud.Case;

public class FeatureLocationWordCloud implements IFeatureLocation {

	@Override
	public void locateFeatures(FeatureList featureList, AdaptedModel adaptedModel, IProgressMonitor monitor) {

		ArrayList<Cloud> list = new ArrayList<Cloud>();
		for(Block b : adaptedModel.getOwnedBlocks())
		{
			Cloud cloud = new Cloud(Case.CAPITALIZATION);
			cloud.setMaxWeight(50);
			cloud.setMinWeight(5);
			cloud.setMaxTagsToDisplay(50);
			for (BlockElement e : b.getOwnedBlockElements()) {
				for (ElementWrapper wr : e.getElementWrappers()) {
					
					AbstractElement element = (AbstractElement) (wr.getElement());
					for(String s : element.getWords())
						cloud.addTag(s.trim());
				}
			}
			list.add(cloud);
		} 
		System.out.println(list.size());
		Cloud cloud_f = new Cloud(Case.CAPITALIZATION);
		cloud_f.setMaxWeight(50);
		cloud_f.setMinWeight(5);
		cloud_f.setMaxTagsToDisplay(50);
		for(Feature f : featureList.getOwnedFeatures())
		{
			monitor.subTask("Locating " + f.getName()+" ...");
			cloud_f.clear();
			if(f.getName() != null)
			{
				StringTokenizer tk = new StringTokenizer(f.getName(), " :!?*+²&~\"#'{}()[]-|`_\\^°,.;/§");
				
				while (tk.hasMoreTokens()) {
					for (String w : tk.nextToken().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
						cloud_f.addTag(w.trim());
					}
				}
			}
			
			if(f.getDescription() != null)
			{
				StringTokenizer tk = new StringTokenizer(f.getDescription(), " :!?*+²&~\"#'{}()[]-|`_\\^°,.;/§");
				while (tk.hasMoreTokens()) {
					for (String w : tk.nextToken().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
						cloud_f.addTag(w.trim());
					}
			}
			
			

			}
			for(Cloud c : list)
			{
				Block b = adaptedModel.getOwnedBlocks().get(list.indexOf(c));
				double d = WordCloudUtil.cmpClouds(cloud_f, c);
				
				if(d > 0.65){
					b.getCorrespondingFeatures().add(f);
				}
			}
		}
		
		
	}
	
	

}
