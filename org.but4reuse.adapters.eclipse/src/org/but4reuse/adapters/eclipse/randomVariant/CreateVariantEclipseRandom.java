package org.but4reuse.adapters.eclipse.randomVariant;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class CreateVariantEclipseRandom implements IObjectActionDelegate {

	private ISelection selection;

	
	public CreateVariantEclipseRandom() {
		super();
	}
	
	
	private String InputMessageText = "Input pathname to eclipse package";
	private String OutputMessageText = "Output where do you want stock the variant random ";

	private String numberOfVariantText = "Number of Variant to create";
	private String SelectRandomText = "Value of Random selector";
	
	private String OnlyFeaturesText = "Only save features and plugins metadata";

	JTextField Input = new JTextField("c:/...");
	JTextField Output = new JTextField("C:/...");
	JTextField numberVariant = new JTextField(0);
	JTextField randomSelector = new JTextField(0);
	JCheckBox invar = new JCheckBox();
	JFileChooser fc = new JFileChooser();
	
	public void run(IAction action) {
		// InputDialog dlg = new
		// InputDialog(Display.getCurrent().getActiveShell(), "Test", "Please
		// input text.",
		// "Test-Text", null) {};
		// InputDialog dlg2 = new InputDialog(dlg.getShell(),"Test2", "Please
		// input text2.",
		// "Test-Text", null);
		// dlg.open();
		// dlg2.open();

		// MessageBox msg = new
		// MessageBox(Display.getCurrent().getActiveShell());
		//
		// msg.setMessage("Entree votre texte");
		//
		// msg.open();

		
		final JComponent[] inputs = new JComponent[] {
				new JLabel(InputMessageText),
				Input,
				//fc,
				new JLabel(OutputMessageText),
				Output,
				new JLabel(numberOfVariantText),
				numberVariant,
				new JLabel(SelectRandomText),
				randomSelector,
				new JLabel(OnlyFeaturesText),
				invar
		};
		
		JOptionPane.showMessageDialog(null, inputs, "Generation Random", JOptionPane.OK_CANCEL_OPTION);
		
		System.out.println("You entered " +
				Input.getText() + ", " +
				Output.getText() + ", " +
				numberVariant.getText() + ", " +
				randomSelector.getText() + ", " +
				invar.isSelected());
		
		try{
			if(Integer.parseInt(randomSelector.getText())<=0 || Integer.parseInt(randomSelector.getText())>=100){
				SelectRandomText="Value of Random selector need to be between 0 and 100";
				if(Integer.parseInt(numberVariant.getText())<0 ){
					numberOfVariantText="Number of Variant to create need to be superior of 0";
				}
				this.run(action);
				return;
			}
			if(Integer.parseInt(numberVariant.getText())<=0 ){
				numberOfVariantText="Number of Variant to create need to be superior of 0";
				this.run(action);
				return;
			}
		}catch(NumberFormatException e){
			this.run(action);
			return;
		}
		final JComponent[] input = new JComponent[] {
				new JLabel(methodeJulien(Input.getText(), Output.getText(), Integer.parseInt(numberVariant.getText()),
						Integer.parseInt(randomSelector.getText()),invar.isSelected()))
				
		};
		JOptionPane.showMessageDialog(null, input, "Generation Random", JOptionPane.OK_CANCEL_OPTION);
		
		System.out.println(" fin methode run ");
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}
	
	public String methodeJulien(String in, String out, int number, int rand, boolean check){
		return "input = "+in+" output = "+out+" nbVar ="+number+" random Select = "+rand+" check = "+check;
	}

}
