package org.but4reuse.worldcouds.visualisation;

import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.visualisation.helpers.VisualisationsHelper;
import org.but4reuse.wordclouds.ui.actions.WordCloudAction;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

public class WordCloudVis extends ViewPart {

	

	static private WordCloudVis singleton = null;
	
	private Combo combo;
	private List list;
	private Canvas canvas;
	private Text text;
	private Button accept;
	private Button renameAll;
	private Button renameOne;
	
	public WordCloudVis() {
		// TODO Auto-generated constructor stub
		super();
		this.singleton = this;
	}

	public static WordCloudVis getSingleton()
	{
		return singleton;
	}
	
	public Combo getCombo()
	{
		return combo;
	}
	
	public List getList()
	{
		return list;
	}
	
	public Canvas getCanvas()
	{
		return canvas;
	}
	
	public Text getText()
	{
		return text;
	}
	
	public static void update(int index,boolean redraw)
	{
		if(singleton == null ||
				AdaptedModelManager.getAdaptedModel() == null)
			return;
		
		singleton.getCombo().clearSelection();
		singleton.getCombo().removeAll();
		singleton.getList().removeAll();
		singleton.getText().setText("");
		
		for(Block b : AdaptedModelManager.getAdaptedModel().getOwnedBlocks())
			singleton.getCombo().add(b.getName());
		singleton.getCombo().select(index);
		
		GC gc = new GC(singleton.getCanvas());
		singleton.getCanvas().update();
	
			singleton.getCanvas().redraw();
			singleton.getCanvas().print(gc);
		
		int x = 10,y=10;
		int maxH = 0;
		
		for(Tag t : WordCloudAction.getClouds().get(index).tags())
		{
			System.out.println("Draw");
			singleton.getList().add(t.getName());
			
			Font f = new Font(Display.getCurrent(), "Arial", t.getWeightInt(), SWT.ITALIC );
			gc.setFont(f);
			
			
			
			int width = (int)(t.getName().length()*t.getWeightInt()*0.80);
			width += (int)(20 *1.0 /t.getName().length());
			
			for(int i = 1;i<t.getName().length();i++)
				if(t.getName().charAt(i) == 'w' || t.getName().charAt(i) == 'm')
					width+=t.getWeightInt()*0.5;
			
			if(t.getName().charAt(0) == 'W' || t.getName().charAt(0) == 'M')
				width+=t.getWeightInt()*0.6;
			
			if(x+width > singleton.getCanvas().getSize().x)
			{
				x = 10;
				y+=maxH;
				maxH =0;
			}
			
			int height = (int)(1.25* t.getWeightInt());
			height += (int)(t.getWeightInt()/5);
			if(t.getName().contains("p") 
					|| t.getName().contains("q") 
						|| t.getName().contains("g") 
							|| t.getName().contains("j"))
			{
				height+=(int)(0.3*t.getWeightInt());
			}
			
			if(maxH <height)
				maxH = height;
			
			gc.drawString(t.getName(), x, y);
			x+=width;
		}
	 
		VisualisationsHelper.notifyVisualisations
		(AdaptedModelManager.getFeatureList(), AdaptedModelManager.getAdaptedModel(), null, new NullProgressMonitor());
		
	
	}
	
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		
		GridLayout grid = new GridLayout();
		grid.numColumns = 2;
		grid.marginHeight = 3;
		GridData data;
		
		parent.setLayout(grid);
		
		
		
		list = new List(parent, SWT.BORDER  | SWT.READ_ONLY);
		canvas = new Canvas(parent,SWT.BORDER);
		combo = new Combo(parent, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		text = new Text(parent, SWT.BORDER);
		renameOne = new Button(parent, SWT.NORMAL);	
		accept = new Button(parent, SWT.NORMAL);
		renameAll = new Button(parent, SWT.NORMAL);
	
		
		data = new GridData();
		data.heightHint= 400;
		data.widthHint = 500;
		canvas.setLayoutData(data);
		
		data = new GridData();
		data.heightHint= 25;
		data.widthHint = 380;
		combo.setLayoutData(data);
		
		data = new GridData();
		data.heightHint= 400;
		data.widthHint = 400;
		list.setLayoutData(data);
		
		
	   data = new GridData();
	   data.heightHint = 25;
	   data.widthHint = 150;
	   renameOne.setLayoutData(data);
	   renameOne.setText("Rename Current Block Auto");
		   
	   data = new GridData();
	   data.heightHint = 25;
	   data.widthHint = 150;
	   renameAll.setLayoutData(data);
	   renameAll.setText("Rename All Auto");
	   
	   data = new GridData();
	   data.heightHint = 25;
	   data.widthHint = 150;
	   text.setLayoutData(data);
	   
	   data = new GridData();
	   data.heightHint = 25;
	   data.widthHint = 150;
	   accept.setLayoutData(data);
	   accept.setText("Rename Block");
	   
	   
	   renameAll.addListener(SWT.Selection, new Listener() {
		      
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				switch(event.type)
				{
				 case SWT.Selection:
					
					 
					 int ind = 0;
					 for(Block b : AdaptedModelManager.getAdaptedModel().getOwnedBlocks())
					 {
						 
						 Cloud c = WordCloudAction.getClouds().get(ind);
						 if(c.tags().size() == 0)
							 return;
						 
						 Tag last = c.tags().get(0);
						 for(Tag t : c.tags())
						 {
							 if(t.getWeight() > last.getWeight())
								last = t; 
						 }
						 b.setName(last.getName());
						 ind++;
					 }
					WordCloudVis.update(combo.getSelectionIndex(),true);
					break;
					 
				}
			}
		      });
	   
	   renameOne.addListener(SWT.Selection, new Listener() {
		      
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				switch(event.type)
				{
				 case SWT.Selection:
					
					 int ind = combo.getSelectionIndex();
					 Block b = AdaptedModelManager.getAdaptedModel().getOwnedBlocks().get(ind);
					 Cloud c = WordCloudAction.getClouds().get(ind);
					 if(c.tags().size() == 0)
						 return;
					 
					 Tag last = c.tags().get(0);
					 for(Tag t : c.tags())
					 {
						 if(t.getWeight() > last.getWeight())
							last = t; 
					 }
					 b.setName(last.getName());
					 WordCloudVis.update(combo.getSelectionIndex(),true);
					 break;
					 
				}
			}
		      });
	   
	   accept.addListener(SWT.Selection, new Listener() {
		      
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				switch(event.type)
				{
				 case SWT.Selection:
					
					if(text.getText().equals(""))
						return;
					
					int ind = combo.getSelectionIndex();
					Block b = AdaptedModelManager.getAdaptedModel().getOwnedBlocks().get(ind);
					b.setName(text.getText());
					WordCloudVis.update(combo.getSelectionIndex(),true);
					break;
					 
				}
			}
		      });
	  
	   combo.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					System.out.println("Hello selected");
					Combo c = (Combo)e.getSource();
					WordCloudVis.update(c.getSelectionIndex(),true);
									
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
				
				}
			});
	   
	   list.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				List l = (List)e.getSource();
				
				Block b = AdaptedModelManager.getAdaptedModel().getOwnedBlocks().get(combo.getSelectionIndex());
				b.setName(l.getItem(l.getSelectionIndex()));
				WordCloudVis.update(combo.getSelectionIndex(),true);
								
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			
			}
		});
		
			if(AdaptedModelManager.getAdaptedModel() == null)
				return;
			
			for(Block b : AdaptedModelManager.getAdaptedModel().getOwnedBlocks())
				combo.add(b.getName());
			
			if(combo.getItemCount() > 0)
				combo.select(0);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	

}
