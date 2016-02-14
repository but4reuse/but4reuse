package org.but4reuse.adapters.eclipse.generator.actions;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.but4reuse.adapters.eclipse.generator.VariantsGenerator;
import org.but4reuse.adapters.eclipse.generator.utils.IListener;
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
	
	private String inputMessageText;
	private String outputMessageText;
	private String numberOfVariantText;
	private String selectRandomText;
	private String onlyFeaturesText;

	private JTextField input = new JTextField("C:\\Users\\JulienM\\Desktop\\PSTL\\input\\eclipse.");
	private JTextField output = new JTextField("C:\\Users\\JulienM\\Desktop\\PSTL\\output");
	private JTextField numberVariant = new JTextField(0);
	private JTextField randomSelector = new JTextField(0);
	private JCheckBox onlyMetaData = new JCheckBox();
//	private JFileChooser fc = new JFileChooser();
	
	private boolean isAllOK = true;
	private ScrollableMessageDialog dialog;
	private String texteWaiting;
	
	public void run(IAction action) {
		
		// TODO: Retirer ces lignes
		input = new JTextField("C:\\Users\\JulienM\\Desktop\\PSTL\\input\\eclipse.");
		output = new JTextField("C:\\Users\\JulienM\\Desktop\\PSTL\\output");
		numberVariant.setText("2");
		randomSelector.setText("50");
		onlyMetaData.setSelected(true);
		
		if(isAllOK){
			inputMessageText = "Input path of eclipse package";
			outputMessageText = "Output path for variant";
			numberOfVariantText = "Number of variants";
			selectRandomText = "Value of random selector";
			onlyFeaturesText = "Only save features and plugins metadata";
		}
		onlyMetaData.setText(onlyFeaturesText);
		
		final JComponent[] inputs = new JComponent[] {
				new JLabel(inputMessageText),
				input,
				//valid,
				new JLabel(outputMessageText),
				output,
				new JLabel(numberOfVariantText),
				numberVariant,
				new JLabel(selectRandomText),
				randomSelector,
				onlyMetaData
		};
		
		int result = JOptionPane.showConfirmDialog(null, inputs, "Generate Eclipse variants", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		switch (result){
			case JOptionPane.CANCEL_OPTION: return;
			case JOptionPane.CLOSED_OPTION: return;
		}
		
		int valRand=0;
		int nbVariants=0;
		isAllOK=true;

		try{
			valRand=Integer.parseInt(randomSelector.getText());
			if((valRand<=0 || valRand>100)){
				selectRandomText="Value of Random selector need to be between 0 and 100";
				isAllOK=false;
			}
		}catch(NumberFormatException e){
			selectRandomText="Value of Random selector need to be integer";
			isAllOK=false;
		}
		
		try{
			nbVariants=Integer.parseInt(numberVariant.getText());
			if(nbVariants<=0 ){
				numberOfVariantText="Number of Variant to create need to be superior of 0";
				isAllOK=false;
			}
		}catch(NumberFormatException e){
			numberOfVariantText="Variant number to create need to be integer";
			isAllOK=false;
		}
		
		if(!isAllOK){
			this.run(action);
			return;
		}
		
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
		
		final Object objSync = new Object();
		
		final Shell shell = Display.getCurrent().getActiveShell();
    	System.out.println("\nBefore dialog");
    	
		if (selection instanceof IStructuredSelection) {
			Object art = ((IStructuredSelection) selection).getFirstElement();
			if (art instanceof ArtefactModel) {
				try {
					// Get the artefact model
					ArtefactModel artefactModel = ((ArtefactModel) art);
					artefactModel.setAdapters("eclipse4generator");
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
					synchronized (objSync) {
						objSync.notify();
					}
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
