package org.but4reuse.wordclouds.ui.actions;



import java.util.ArrayList;

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

public class WordCloudAction implements IObjectActionDelegate {
	
	
	private static ArrayList<Cloud> clouds =  null;
	
	public static ArrayList<Cloud> getClouds()
	{
		return clouds;
	}
	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		System.out.println("Action OK");
		
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
			cloud.setMaxTagsToDisplay(50);
			cloud.setTagCase(Case.CAPITALIZATION);
			
			for(BlockElement e : b.getOwnedBlockElements())
			{
				for(ElementWrapper wr : e.getElementWrappers())
				{
					AbstractElement element = (AbstractElement)(wr.getElement());
				//	System.out.println("\nClasse : "+element.getClass());
					addWords(cloud,element.getWords());
				}
				
			}
			clouds.add(cloud);
		}
	
		
		System.out.println("WordCloud Creation ... done");
		try {
			
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.but4reuse.wordclouds.view");
		
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WordCloudVis.update(0,false);
		System.out.println("View Update ... OK");
	}
	
	private static void addWords(Cloud cloud, ArrayList<String> words)
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
