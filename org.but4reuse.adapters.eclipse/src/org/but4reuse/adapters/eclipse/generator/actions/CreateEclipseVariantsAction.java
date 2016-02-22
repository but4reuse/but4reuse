package org.but4reuse.adapters.eclipse.generator.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.but4reuse.adapters.eclipse.generator.VariantsGenerator;
import org.but4reuse.adapters.eclipse.generator.utils.IListener;
import org.but4reuse.adapters.eclipse.generator.utils.PreferenceUtils;
import org.but4reuse.adapters.eclipse.generator.utils.VariantsUtils;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
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
	
	private JTextField input;
	private JTextField output;
	private JTextField numberVariant;
	private JTextField randomSelector;
	private JCheckBox onlyMetaData;
//	private JFileChooser fc = new JFileChooser();
	
	private boolean isAllOK = true;
	private ScrollableMessageDialog dialog;
	private JLabel inputLabel;
	private JLabel variantsLabel;
	private JLabel randomLabel;
	private JButton validButton;
	
	public void run(IAction action) {
		
		if(isAllOK){ // init
			input = new JTextField("C:\\");
			output = new JTextField("C:\\");
			numberVariant = new JTextField(0);
			randomSelector = new JTextField(0);
			onlyMetaData = new JCheckBox();
			
			try {  // Load preferences
				Map<String, String> map = PreferenceUtils.getPreferencesMap(this);
				if( map.containsKey("user.name") && map.get("user.name").equals(System.getProperty("user.name")) ){ // Look below, in registration
					input.setText((String) map.get("input"));
					output.setText((String) map.get("output"));
					numberVariant.setText((String) map.get("numberVariant"));
					randomSelector.setText((String) map.get("randomSelector"));
					onlyMetaData.setSelected( Boolean.parseBoolean(map.get("onlyMetaData")));
				}
				
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				if(e instanceof FileNotFoundException)
				System.out.println("Error for loading preferences");
			}
			
			
			inputLabel = new JLabel(VariantsUtils.INPUT_TEXT);
			variantsLabel = new JLabel(VariantsUtils.VARIANTS_NUMBER_TEXT);
			randomLabel = new JLabel(VariantsUtils.RANDOM_NUMBER_TEXT);
			onlyMetaData.setText(VariantsUtils.METADATA_TEXT);
			
			validButton = new JButton(VariantsUtils.VALID);
			validButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(!new File(input.getText()).exists()){
						validButton.setForeground(Color.RED);
					} else {
						validButton.setForeground(Color.GREEN);
					}
				}
			});
		}
		
		final JComponent[] inputs = new JComponent[] {
				inputLabel,
				input,
				validButton,
				new JLabel(VariantsUtils.OUTPUT_TEXT),
				output,
				variantsLabel,
				numberVariant,
				randomLabel,
				randomSelector,
				onlyMetaData
		};
		
		int result = JOptionPane.showConfirmDialog(null, inputs, VariantsUtils.PARAMETERS_TITLE,
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if(result==JOptionPane.CANCEL_OPTION || result==JOptionPane.CLOSED_OPTION) return;
		
		// Settings checking
		int valRand=0;
		int nbVariants=0;
		isAllOK=true;

		if(!new File(input.getText()).exists()){
			validButton.setForeground(Color.RED);
			isAllOK = false;
		} else {
			validButton.setForeground(Color.GREEN);
		}
		
		try{
			valRand=Integer.parseInt(randomSelector.getText());
			if((valRand<=0 || valRand>100)){
				isAllOK=false;
				randomLabel.setForeground(Color.RED);
			} else {
				randomLabel.setForeground(null);
			}
		}catch(NumberFormatException e){
			isAllOK=false;
			randomLabel.setForeground(Color.RED);
		}
		
		try{
			nbVariants=Integer.parseInt(numberVariant.getText());
			if(nbVariants<=0 ){
				isAllOK=false;
				variantsLabel.setForeground(Color.RED);
			} else {
				variantsLabel.setForeground(null);
			}
		}catch(NumberFormatException e){
			isAllOK=false;
			variantsLabel.setForeground(Color.RED);
		}
		
		if(!isAllOK){
			this.run(action);
			return;
		}
		
		// Saving preferences
		try{
			PreferenceUtils.savePreferencesMap(getMapWithAllParameters(), context);
		} catch (IOException e) {
			System.out.println("Error for saving preferences");
		}
		
		// Start the generator process
		final int nbVariantsForThread = nbVariants; // final for the thread and because nbVariants and valRand can't be final 
		final int valRandForThread = valRand;	
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				VariantsGenerator varGen = new VariantsGenerator(input.getText(), output.getText(), nbVariantsForThread, valRandForThread, onlyMetaData.isSelected());
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
		map.put("input", input.getText());
		map.put("output", output.getText());
		map.put("randomSelector", randomSelector.getText());
		map.put("numberVariant", numberVariant.getText());
		map.put("onlyMetaData", ((Boolean)onlyMetaData.isSelected()).toString() );
		map.put("user.name", System.getProperty("user.name")); // Display OUR preferences (maybe an other prefMap.ser was committed)
		return map;
	}
	
}
