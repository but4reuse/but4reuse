package org.but4reuse.adapters.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.ui.views.BlockContentProvider;
import org.but4reuse.adapters.ui.views.BlockLabelProvider;
import org.but4reuse.adapters.ui.views.PluginContentProvider;
import org.but4reuse.adapters.ui.views.PluginLabelProvider;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.but4reuse.visualisation.visualiser.adaptedmodel.BlockElementsMarkupProvider;
import org.but4reuse.visualisation.visualiser.adaptedmodel.BlockElementsOnArtefactsVisualisation;
import org.but4reuse.visualisation.visualiser.adaptedmodel.BlockMarkupKind;
import org.eclipse.contribution.visualiser.core.ProviderDefinition;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.views.Menu;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

/**
 * @author jabier.martinez
 */
public class ShowBlockDependenciesAction implements IViewActionDelegate {

	Menu menu;
	private List<Block> elements;

	@Override
	public void run(IAction action) {
		ProviderDefinition definition = BlockElementsOnArtefactsVisualisation.getBlockElementsOnVariantsProvider();
		BlockElementsMarkupProvider markupProvider = (BlockElementsMarkupProvider) definition.getMarkupInstance();
		
		elements = new ArrayList<Block>();
		
		for (Object o : markupProvider.getAllMarkupKinds()) {
			IMarkupKind kind = (IMarkupKind) o;
			if (menu.getActive(kind)) {
				// get the text of the Block elements
				if (kind instanceof BlockMarkupKind) {
					BlockMarkupKind markupKind = (BlockMarkupKind) kind;
					Block block = markupKind.getBlock();
					
					/*
					String sText = "";
					for (BlockElement blockElement : block.getOwnedBlockElements()){
						IElement element = (IElement)blockElement.getElementWrappers().get(0).getElement();
						sText = sText + element.getText() + "\n";

					}*/
					
					elements.add(block);

				}
			}
		}
		
		Display.getDefault().syncExec(
				new Runnable(){
					public void run(){

						Display d = Display.getCurrent();
						Shell shell = new Shell();
						shell.setText("Visualization");
						shell.setLayout(new FillLayout(SWT.VERTICAL));
						shell.setSize(700,700);
					
						GraphViewer viewer = new GraphViewer(shell,SWT.NONE);
						
						
						// ===================================
						BlockContentProvider contentProvider = new BlockContentProvider();
						contentProvider.setBlockList(elements);
						BlockLabelProvider labelProvider = new BlockLabelProvider();
						viewer.setContentProvider(contentProvider);
						viewer.setLabelProvider(labelProvider);
						
						 // Definition du layout 
				        viewer.setLayoutAlgorithm(new 
				                SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING)); 
				        viewer.applyLayout(); 
						
						viewer.setInput(elements); 
						// ===================================

						shell.open();
						while (!shell.isDisposed()) {
							while (!d.readAndDispatch()) {
								d.sleep();
							}
						}
					} 
				}
				);
		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

	@Override
	public void init(IViewPart view) {
		menu = (Menu) view;
	}


}
