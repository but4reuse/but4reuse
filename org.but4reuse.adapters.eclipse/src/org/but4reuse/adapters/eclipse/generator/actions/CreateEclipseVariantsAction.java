package org.but4reuse.adapters.eclipse.generator.actions;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.but4reuse.adapters.eclipse.generator.VariantsGenerator;
import org.but4reuse.adapters.eclipse.generator.utils.IListener;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class CreateEclipseVariantsAction implements IListener, IObjectActionDelegate {

	@SuppressWarnings("unused")
	private ISelection selection;
	
	public CreateEclipseVariantsAction() {
		super();
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
	private ArrayList<String> summaryTexts;
	private JList<String> jlistRecap;
	
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
		
		summaryTexts = new ArrayList<String>();
		jlistRecap = new JList<String>(summaryTexts.toArray(new String[summaryTexts.size()]));
		final JScrollPane scrollpane = new JScrollPane(jlistRecap);
		scrollpane.getViewport().add(jlistRecap);
		JPanel panel = new JPanel(); 
		panel.add(scrollpane);
		scrollpane.setPreferredSize(new Dimension(350, 300));
		jlistRecap.addListSelectionListener(new ListSelectionListener () {
			public void valueChanged (ListSelectionEvent lse) {
				jlistRecap.clearSelection(); // Transparence de la sélection
				jlistRecap.setFocusable(false);
			}
		});
		
		new Thread(new Runnable(){
	        public void run(){
	        	JOptionPane.showMessageDialog(null, scrollpane, "Summary of variants generation", JOptionPane.PLAIN_MESSAGE);
	        }
	    }).start();
		
		VariantsGenerator varGen = new VariantsGenerator(input.getText(), output.getText(), nbVariants, valRand, onlyMetaData.isSelected());
		varGen.addListener(this);
		varGen.generate(); // Synchrone
		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}
	
	@Override
	public void receive(String... msg) {
		if(msg != null && msg.length!=0){
			for(String s : msg){
				summaryTexts.add(s);
				System.out.println(s);
			}
			jlistRecap.setListData(summaryTexts.toArray(new String[summaryTexts.size()]));
		}
	}
	
}
