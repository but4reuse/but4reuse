package org.but4reuse.adapters.eclipse.generator.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.but4reuse.adapters.eclipse.generator.VariantsGenerator;
import org.but4reuse.adapters.eclipse.generator.utils.IListener;
import org.but4reuse.adapters.eclipse.generator.utils.VariantsUtils;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.utils.emf.EMFUtils;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class CreateEclipseVariantsAction implements IListener, IObjectActionDelegate {

	private ISelection selection;
	private CreateEclipseVariantsAction context;
	
	public CreateEclipseVariantsAction() {
		super();
		this.context = this;
	}
	
	private JTextField input = new JTextField("C:\\Users\\JulienM\\Desktop\\PSTL\\input\\eclipse");
	private JTextField output = new JTextField("C:\\Users\\JulienM\\Desktop\\PSTL\\output");
	private JTextField numberVariant = new JTextField(0);
	private JTextField randomSelector = new JTextField(0);
	private JCheckBox onlyMetaData = new JCheckBox();
//	private JFileChooser fc = new JFileChooser();
	
	private boolean isAllOK = true;
	private ScrollableMessageDialog dialog;
	private String texteWaiting;
	private JLabel inputLabel;
	private JLabel variantsLabel;
	private JLabel randomLabel;
	private JButton validButton;
	
	public void run(IAction action) {
		
		if(isAllOK){
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
		
		// Start the generator process
		final int nbVariantsForThread = nbVariants;
		final int valRandForThread = valRand;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("Before generator");
				VariantsGenerator varGen = new VariantsGenerator(input.getText(), output.getText(), nbVariantsForThread, valRandForThread, onlyMetaData.isSelected());
				varGen.addListener(context);
				varGen.generate();
				
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						if(dialog != null && !dialog.isDisposed()) dialog.setCloseable(true);
					}
				});
				System.out.println("After generator");
			}
		}).start();
		
		
		// Open the Summary Dialog
		final Shell shell = Display.getCurrent().getActiveShell();
    	System.out.println("\nBefore dialog");
    	
		if (selection instanceof IStructuredSelection) {
			Object art = ((IStructuredSelection) selection).getFirstElement();
			if (art instanceof ArtefactModel) {
				try {
					// Get the artefact model
					ArtefactModel artefactModel = ((ArtefactModel) art);
					artefactModel.setAdapters(VariantsUtils.ADAPTER_NAME);
					artefactModel.eResource().save(null);
					IResource res = EMFUtils.getIResource(artefactModel.eResource());
					IContainer container = res.getParent();

					WorkbenchUtils.refreshIResource(container);
					// Show message
					dialog = new ScrollableMessageDialog(shell,
							"Summary" , null,
							"", false);
					dialog.open();
					System.out.println("After dialog");
    			} catch (Exception e) {
					MessageDialog.openError(
									shell,
									"Error in summary dialog",
									e.toString());
					e.printStackTrace();
				}
			}
		}
		System.out.println("after dialog");
		
		System.out.println("(CreateEclipseVariantsAction.run) finished");
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}
	
	@Override
	public void receive(final String msg) {
		if(msg != null && !msg.isEmpty()){
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					if(dialog==null || (dialog!=null && dialog.isDisposed())){
						if(texteWaiting==null) texteWaiting = msg;
						else texteWaiting += "\r\n\n" + msg;
					} else if (!dialog.isDisposed()){
						String scrollText = dialog.getScrollableText();
						if(scrollText==null || scrollText.isEmpty()) scrollText = "";
						else scrollText += "\r\n\n";
						
					    if(texteWaiting != null){
					    	scrollText += texteWaiting + "\r\n\n" + msg;
					    	texteWaiting = null;
					    }
						else scrollText += msg;

						dialog.setScrollableText(scrollText);
					}
				}
			});
		}
	}
	
}
