package org.but4reuse.adapters.ui.actions;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.ui.xmlgenerator.SaveDataUtils;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.contribution.visualiser.views.Menu;
import org.eclipse.core.resources.IContainer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class ShowResultInGraphs implements IViewActionDelegate {

	Menu menu;

	@Override
	public void run(IAction action) {
		// Graph generator
		try {
			// path of the xml files
			IContainer output = AdaptedModelManager.getDefaultOutput();
			if(output != null) {
				File outputFile = WorkbenchUtils.getFileFromIResource(output);

				// path of this script
				String scriptPath = ShowResultInGraphs.class.getProtectionDomain().getCodeSource().getLocation().getPath();
				
				File target = new File("");
				
				// run python script
				String os = System.getProperty("os.name").toLowerCase(); 
				
				if(os.contains("mac")) {
					target = SaveDataUtils.saveDataInFile();
				}
				else {
					// file chooser
					JFileChooser chooser = new JFileChooser(outputFile.getAbsolutePath()+"/reuseAnalysis");
					FileNameExtensionFilter filter = new FileNameExtensionFilter(
							"XML File", "xml");
					chooser.setFileFilter(filter);
					int returnVal = chooser.showOpenDialog(null);
					if(returnVal == JFileChooser.APPROVE_OPTION) {
						target = chooser.getSelectedFile();
					}
				}
				
				String command = "";
				
				try{
					if(os.contains("win")){
						scriptPath = scriptPath.substring(1);
						command = scriptPath+"GraphGenerator/windows/pythonScript_dynamicGraphs.exe "+target.getAbsolutePath();
					}else if (os.contains("mac")){
						command ="/Library/Frameworks/Python.framework/Versions/3.7/bin/python3 "+scriptPath+"GraphGenerator/mac/pythonScript_dynamicGraphs.py "+target.getAbsolutePath();
					}else if (os.contains("nix") || os.contains("nux")){
						command = scriptPath+"GraphGenerator/unix/pythonScript_dynamicGraphs "+target.getAbsolutePath();
					}
					System.out.println("command: "+command);
					Process p = Runtime.getRuntime().exec(command);
					
				}catch(Exception e){
					System.out.println("Exception: "+e.getMessage());
					System.out.println("command: "+command);
					command ="python3 "+scriptPath+"GraphGenerator/pythonScript_dynamicGraphs.py "+target.getAbsolutePath();
					System.out.println("Need python3 and python libraries: matplotlib, tkinter and numpy");
					Process p = Runtime.getRuntime().exec(command);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

	@Override
	public void init(IViewPart view) {
		menu = (Menu) view;
	}

}
