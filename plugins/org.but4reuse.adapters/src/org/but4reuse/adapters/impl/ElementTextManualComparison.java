package org.but4reuse.adapters.impl;

import java.text.DecimalFormat;

import org.eclipse.jface.dialogs.MessageDialog;

/**
 * The default manual comparison
 * 
 * @author jabier.martinez
 */
public class ElementTextManualComparison implements IManualComparison {
	String elementText1;
	String elementText2;
	double calculatedSimilarity;
	int result;

	public ElementTextManualComparison(double calculatedSimilarity, String elementText1, String elementText2) {
		this.elementText1 = elementText1;
		this.elementText2 = elementText2;
		this.calculatedSimilarity = calculatedSimilarity;
	}

	@Override
	public void run() {
		// TODO implement "Always" and "Never" buttons
		// Default is No
		MessageDialog dialog = new MessageDialog(null,
				"Manual decision for equal. Automatic said " + new DecimalFormat("#").format(calculatedSimilarity * 100)
						+ "%",
				null, elementText1 + "\n\n is equal to \n\n" + elementText2, MessageDialog.QUESTION,
				new String[] { "Yes", "No", "Deactivate manual equal" }, 1);
		result = dialog.open();
	}

	@Override
	public int getResult() {
		return result;
	}

}
