package org.butreuse.adapters.eclipse.randomVariant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;


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
				fc,
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
		System.out.println(" fin methode run ");
	}

	public static Graph createActualFeaturesGraph(List<ActualFeature> features) {
		Graph graph = new TinkerGraph();
		// Add nodes
		Integer id = 0;
		Map<ActualFeature, Vertex> fMap = new HashMap<ActualFeature, Vertex>();
		Map<String, Vertex> idMap = new HashMap<String, Vertex>();
		for (ActualFeature f : features) {
			Vertex v = graph.addVertex(id);
			v.setProperty("Label", f.getId());
			v.setProperty("Name", f.getName());
			fMap.put(f, v);
			idMap.put(f.getId(), v);
			id++;
		}
		// Add edges
		for (ActualFeature f : features) {
			Vertex source = fMap.get(f);
			for (String requi : f.getRequiredFeatures()) {
				Vertex target = idMap.get(requi);
				if (source == null || target == null) {
					System.out.println(f.getId() + " requires " + requi + " but it was not found");
				} else {
					Edge e = source.addEdge("required", target);
					e.setProperty("Label", "required");
				}
			}
			for (String requi : f.getIncludedFeatures()) {
				Vertex target = idMap.get(requi);
				Edge e = source.addEdge("included", target);
				e.setProperty("Label", "included");
			}
		}
		return graph;
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

}
