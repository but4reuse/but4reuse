package org.but4reuse.adapters.eclipse.benchmark.generator.dialogs;

import org.but4reuse.adapters.eclipse.benchmark.generator.utils.VariantsUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Julien Margarido
 * @author Felix Lima Gorito
 */
public class RandomAndDissimilarDialog extends Dialog {

	private Text input;
	private Text output;
	private Text generator;
	private Text variantsNumber;
	private Text time;
	private Button keepOnlyMetadataButton;
	private Button noOutputButton;

	private Label lblInput;
	private Label lblOutput;
	private Label lblVariantsNumber;
	private Label lblTime;

	private Boolean isInputOK;
	private Boolean isOutputOK;
	private Boolean isVariantsNumberOK;
	private Boolean isTimeOK;

	private String inputContent = "C:\\eclipse";
	private String outputContent = "C:\\outputFolder";
	private String generatorLocationContent = "C:\\dist\\PLEDGE.jar";
	private String variantsNumberContent = "3";
	private String timeContent = "0";
	private Boolean keepOnlyMetadata = true;
	private Boolean noOutputOnlyStatistics = false;

	private Color red;

	public RandomAndDissimilarDialog(Shell parentShell) {
		super(parentShell);
		red = parentShell.getDisplay().getSystemColor(SWT.COLOR_RED);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		// Is call during the open method
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(3, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		container.setLayout(layout);

		addInput(container);
		addOutput(container);
		addGenerator(container);
		addTime(container);
		addVariantsNumber(container);
		addKeepOnlyMetadataOption(container);
		addNoOutputOption(container);

		return container;
	}

	private void addKeepOnlyMetadataOption(Composite container) {
		new Label(container, SWT.WRAP).setText("Keep only metadata");
		keepOnlyMetadataButton = new Button(container, SWT.CHECK);
		keepOnlyMetadataButton.setSelection(true);
		keepOnlyMetadataButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				keepOnlyMetadata = keepOnlyMetadataButton.getSelection();
			}
		});
		new Label(container, SWT.WRAP);
	}

	private void addNoOutputOption(Composite container) {
		new Label(container, SWT.WRAP).setText("Only statistics, no output");
		noOutputButton = new Button(container, SWT.CHECK);
		noOutputButton.setSelection(false);
		noOutputButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				noOutputOnlyStatistics = noOutputButton.getSelection();
			}
		});
	}

	private void addInput(final Composite container) {
		lblInput = new Label(container, SWT.WRAP);
		lblInput.setText(VariantsUtils.INPUT_TEXT);
		if (isInputOK != null && !isInputOK)
			lblInput.setForeground(red);

		input = new Text(container, SWT.BORDER);
		input.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		input.setText(inputContent);

		Button button = new Button(container, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				DirectoryDialog dlg = new DirectoryDialog(container.getShell());
				// Initial path, with the Text contains
				dlg.setFilterPath(inputContent);
				// Title
				dlg.setText("Select the eclipse directory");
				String dir = dlg.open();
				if (dir != null) {
					input.setText(dir);
				}
			}
		});
	}

	private void addOutput(final Composite container) {
		lblOutput = new Label(container, SWT.WRAP);
		lblOutput.setText(VariantsUtils.OUTPUT_TEXT);
		if (isOutputOK != null && !isOutputOK) {
			lblOutput.setForeground(red);
		}
		output = new Text(container, SWT.BORDER);
		output.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		output.setText(outputContent);

		Button button = new Button(container, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				DirectoryDialog dlg = new DirectoryDialog(container.getShell());
				// Initial path, with the Text contains
				dlg.setFilterPath(outputContent);
				// Title
				dlg.setText("Select the output");
				String dir = dlg.open();
				if (dir != null) {
					output.setText(dir);
				}
			}
		});
	}
	
	private void addGenerator(final Composite container) {
		Label gen = new Label(container, SWT.WRAP);
		gen.setText(VariantsUtils.GENERATOR_TEXT);
		generator = new Text(container, SWT.BORDER);
		generator.setText(generatorLocationContent);
		generator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		new Label(container, SWT.NONE).setVisible(false);
	}

	private void addVariantsNumber(Composite container) {
		lblVariantsNumber = new Label(container, SWT.WRAP);
		lblVariantsNumber.setText(VariantsUtils.VARIANTS_NUMBER_TEXT);
		if (isVariantsNumberOK != null && !isVariantsNumberOK) {
			lblVariantsNumber.setForeground(red);
		} else {
			lblVariantsNumber.setForeground(null);
		}
		variantsNumber = new Text(container, SWT.BORDER);
		variantsNumber.setText(variantsNumberContent);
		variantsNumber.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		variantsNumber.setTextLimit(10);

		new Label(container, SWT.NONE).setVisible(false);
		// Invisible element, because there are 3 columns
	}

	private void addTime(Composite container) {
		lblTime = new Label(container, SWT.WRAP);
		lblTime.setText(VariantsUtils.TIME_NUMBER_TEXT);
		if (isTimeOK != null && !isTimeOK)
			lblTime.setForeground(red);

		time = new Text(container, SWT.BORDER);
		time.setText(timeContent);
		time.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		// Invisible element, because there are 3 columns
		new Label(container, SWT.NONE).setVisible(false);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(700, 300);
	}

	@Override
	protected boolean isResizable() {
		// Allow the user to change the dialog size!
		return true;
	}

	@Override
	protected void okPressed() {
		inputContent = input.getText();
		outputContent = output.getText();
		timeContent = time.getText();
		variantsNumberContent = variantsNumber.getText();
		generatorLocationContent = generator.getText();
		super.okPressed();
	}

	public String getInputPath() {
		return inputContent;
	}

	public String getOutputPath() {
		return outputContent;
	}
	
	public String getGeneratorPath() {
		return generatorLocationContent;
	}

	public String getVariantsNumber() {
		return variantsNumberContent;
	}

	public String getTime() {
		return timeContent;
	}

	public void setInputState(boolean isGood) {
		isInputOK = isGood;
	}

	public void setOutputState(boolean isGood) {
		isOutputOK = isGood;
	}

	public void setVariantsNumberState(boolean isGood) {
		isVariantsNumberOK = isGood;
	}

	public void setTimeState(boolean isGood) {
		isTimeOK = isGood;
	}

	public boolean isKeepOnlyMetadata() {
		return keepOnlyMetadata;
	}

	public boolean isNoOutputOnlyStatistics() {
		return noOutputOnlyStatistics;
	}

}