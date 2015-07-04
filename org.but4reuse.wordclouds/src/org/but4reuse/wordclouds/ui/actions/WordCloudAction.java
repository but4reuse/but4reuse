package org.but4reuse.wordclouds.ui.actions;



import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Cloud.Case;
import org.mcavallo.opencloud.Tag;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.worldcouds.visualisation.WordCloudVis;

/**
 * @author Arthur
 * 
 *  A Pop-Up Menu. It will create a new view for renaming blocks.
 *  
 */
public class WordCloudAction implements IObjectActionDelegate {
	
	
	/**
	 * A list which contains a word cloud for each identified features.
	 */
	private static ArrayList<Cloud> clouds =  null;
	
	/**
	 *  This method returns the word cloud list.
	 * @return the value of the attribute clouds.
	 */
	public static ArrayList<Cloud> getClouds()
	{
		return clouds;
	}
	
	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		
		
		if(AdaptedModelManager.getAdaptedModel() == null)
		{
			System.out.println("Indentification des features necessaire");
			return;
		}
		
		clouds = new ArrayList<Cloud>();
		int cpt = 0;
		AdaptedModel adaptedModel = AdaptedModelManager.getAdaptedModel();
		
		for(Block b : adaptedModel.getOwnedBlocks())
		{
			
			Cloud cloud = null;
		    cloud  = new Cloud(Case.CAPITALIZATION);
			cloud.setMaxWeight(50);
			cloud.setMinWeight(5);
		
			
			/*
			 * For each block we get all elements owned
			 * We use the method getWords for having 
			 * several strings which could be used as
			 * block name.
			 */
			for(BlockElement e : b.getOwnedBlockElements())
			{
				for(ElementWrapper wr : e.getElementWrappers())
				{
					AbstractElement element = (AbstractElement)(wr.getElement());
					
					/*
					 * We put each String in the cloud.
					 */
					addWords(cloud,element.getWords());
				}
				
			}
			clouds.add(cloud);
		}
	
		
		try {
			
			/*
			 * We get the Workbench for showing the view.
			 */
			
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.but4reuse.wordclouds.view");
		
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		WordCloudVis.update(0,false);
	}
	
	/**
	 * Add strings in a cloud
	 * @param cloud This is the cloud where strings will be added.
	 * @param words The list which contains all strings to add in the cloud. 
	 */
	private static void addWords(Cloud cloud, List<String> words)
	{
		for(String word : words)
			cloud.addTag(new Tag(word));
	}	
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
		
	}

}
