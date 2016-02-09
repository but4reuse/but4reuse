package org.but4reuse.adapters.eclipse.generator;

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
	
	private String inputMessageText;
	private String outputMessageText;
	private String numberOfVariantText;
	private String selectRandomText;
	private String onlyFeaturesText;

	JTextField input = new JTextField("C:/..");
	JTextField output = new JTextField("C:/..");
	JTextField numberVariant = new JTextField(0);
	JTextField randomSelector = new JTextField(0);
	JCheckBox onlyMetaData = new JCheckBox();
	JFileChooser fc = new JFileChooser();
	
	public boolean isAllOK = true;
	
	public void run(IAction action) {
		
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
		
		int result = JOptionPane.showConfirmDialog(null, inputs, "Generate Eclipse variantes", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		switch (result){
			case JOptionPane.CANCEL_OPTION: return;
			case JOptionPane.CLOSED_OPTION: return;
		}
		
		System.out.println("You entered " +
				input.getText() + ", " +
				output.getText() + ", " +
				numberVariant.getText() + ", " +
				randomSelector.getText() + ", " +
				onlyMetaData.isSelected());
		
		int valRand=0;
		int nbVariantes=0;
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
			nbVariantes=Integer.parseInt(numberVariant.getText());
			if(nbVariantes<=0 ){
				numberOfVariantText="Number of Variant to create need to be superior of 0";
				isAllOK=false;
			}
		}catch(NumberFormatException e){
			numberOfVariantText="Number of Variant to create need to be integer";
			isAllOK=false;
		}
		
		if(!isAllOK){
			this.run(action);
			return;
		}
		
		final JComponent[] affiche = new JComponent[] {
				new JLabel(methodeJulien(input.getText(), output.getText(), Integer.parseInt(numberVariant.getText()),
						Integer.parseInt(randomSelector.getText()),onlyMetaData.isSelected()))
				
		};
		JOptionPane.showConfirmDialog(null, affiche, "Result of generation random", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			
		VariantsGenerator varGen = new VariantsGenerator(input.getText(), output.getText(), nbVariantes, valRand, onlyMetaData.isSelected());
		varGen.generate();
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
