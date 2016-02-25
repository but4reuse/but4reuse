package org.but4reuse.adapters.eclipse.generator.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.but4reuse.adapters.eclipse.generator.VariantsGenerator;
import org.but4reuse.adapters.eclipse.generator.utils.ParametersDialog;
import org.but4reuse.adapters.eclipse.generator.utils.IListener;
import org.but4reuse.adapters.eclipse.generator.utils.PreferenceUtils;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class CreateEclipseVariantsAction implements IListener, IObjectActionDelegate {

	@SuppressWarnings("unused")
	private ISelection selection;
	private CreateEclipseVariantsAction context;
	
	public CreateEclipseVariantsAction() {
		super();
		this.context = this;
	}
	
	private boolean isAllOK = true;
	private ScrollableMessageDialog dialog;
	private ParametersDialog paramDialog;
	private Map<String, String> prefMap;
	
	public void run(IAction action) {
		
		if(paramDialog==null){ // Not create a new dialog if it's a "re-open" (parameters not good).
			paramDialog = new ParametersDialog(Display.getCurrent().getActiveShell());
		}
		
		try {  // Load preferences
			prefMap = PreferenceUtils.getPreferencesMap();
			if( prefMap.containsKey("user.name") && prefMap.get("user.name").equals(System.getProperty("user.name")) ){ // Look below, in registration
				paramDialog.addPreferenceParameters(prefMap);
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			if(e instanceof FileNotFoundException)	System.out.println("Error for loading preferences");
		}
		
		if(paramDialog.open() != Window.OK) return; // Open the dialog and stop execution while a button is not pressed
		
		// Settings checking
		int valRand=0;
		int nbVariants=0;

		if(!new File(paramDialog.getInputPath()).exists()){
			paramDialog.setInputState(false);
			isAllOK = false;
		} else {
			paramDialog.setInputState(true);
		}
		
		try{
			valRand=Integer.parseInt(paramDialog.getRandomSelector());
			if((valRand<=0 || valRand>100)){
				isAllOK=false;
				paramDialog.setRandomSelectorState(false);
			} else {
				paramDialog.setRandomSelectorState(true);
			}
		}catch(NumberFormatException e){
			isAllOK=false;
			paramDialog.setRandomSelectorState(false);
		}
		
		try{
			nbVariants=Integer.parseInt(paramDialog.getVariantsNumber());
			if(nbVariants<=0){
				isAllOK=false;
				paramDialog.setVariantsNumberState(false);
			} else {
				paramDialog.setVariantsNumberState(true);
			}
		}catch(NumberFormatException e){
			isAllOK=false;
			paramDialog.setVariantsNumberState(false);
		}
		
		if(!isAllOK){
			this.run(action);
			return;
		}
		
		// Saving preferences
		try{
			PreferenceUtils.savePreferencesMap(getMapWithAllParameters());
		} catch (IOException e) {
			System.out.println("Error for saving preferences");
		}
		
		// Start the generator process
		final int nbVariantsForThread = nbVariants; // final for the thread and because nbVariants and valRand can't be final 
		final int valRandForThread = valRand;	
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				VariantsGenerator varGen = new VariantsGenerator(paramDialog.getInputPath(), paramDialog.getOutputPath(), nbVariantsForThread, valRandForThread, paramDialog.getOnlyMetadataState());
				varGen.addListener(context);
				varGen.generate();   // Long time to execute
				
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						waitWhileParameterIsNull(dialog);
						if(!dialog.isDisposed()) dialog.setCloseable(true);
					}

				});
			}
		}).start();
		
		
		final Shell shell = Display.getCurrent().getActiveShell();
    	
    	// Open the Summary Dialog
		try{
			dialog = new ScrollableMessageDialog(shell, "Summary" , null, "", false);
			dialog.open();
			synchronized (this) { this.notifyAll(); } // Security for not make things on a null dialog in receive method
		} catch (Exception e) {
			MessageDialog.openError(shell,"Error in summary dialog",e.toString());
			e.printStackTrace();
		}
		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {}
	
	@Override
	public void receive(final String msg) {
		if(msg != null && !msg.isEmpty()){
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					
					waitWhileParameterIsNull(dialog);
					
					if (!dialog.isDisposed()){
						String scrollText = dialog.getScrollableText();
						if(scrollText==null) scrollText = "";
				    	scrollText += msg + "\r\n\n";

						dialog.setScrollableText(scrollText);
					}
				}
			});
		}
	}
	
	/**
	 * This method interrupt the current thread while the parameter is null
	 */
	private <T> void waitWhileParameterIsNull(T param) {
		if(param==null){ // If dialog is null, we wait.
			synchronized (this) {
				try {
					while(param==null){
						this.wait(); // Double checking method
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Return the Map<String, String> of all parameters (input path, random number, ...) for preferences
	 */
	private Map<String, String> getMapWithAllParameters(){
		Map<String, String> map = new HashMap<>();
		map.put("input", paramDialog.getInputPath());
		map.put("output", paramDialog.getOutputPath());
		map.put("randomSelector", paramDialog.getRandomSelector());
		map.put("numberVariant", paramDialog.getVariantsNumber());
		map.put("onlyMetaData", ((Boolean)paramDialog.getOnlyMetadataState()).toString() );
		map.put("user.name", System.getProperty("user.name")); // Display OUR preferences (maybe an other prefMap.ser was committed)
		return map;
	}
	
}
