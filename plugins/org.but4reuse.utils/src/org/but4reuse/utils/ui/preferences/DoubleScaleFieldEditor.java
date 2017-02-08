package org.but4reuse.utils.ui.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author Arthur
 * 
 *         This is a double editor which give a give a double between 0 and 1.
 *         There is a scale control and a text control for choosing the number.
 */
public class DoubleScaleFieldEditor extends FieldEditor {

	private double value = 0.5;
	private Composite parent;
	private Scale scale;
	private Text text;

	/**
	 * Constructor
	 * 
	 * @param valueName
	 *            Preference name
	 * @param name
	 *            label name
	 * @param parent
	 *            parent composite
	 */
	public DoubleScaleFieldEditor(String valueName, String name, Composite parent) {
		super(valueName, name, parent);
	}

	@Override
	protected void adjustForNumColumns(int numColumns) {
		if (parent.getLayoutData() != null) {
			((GridData) parent.getLayoutData()).horizontalSpan = numColumns;
		}
	}

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {

		this.parent = parent;

		/*
		 * Here we create the label
		 */
		Label label = getLabelControl(parent);
		GridData data = new GridData();
		data.horizontalSpan = 1;
		data.heightHint = 25;
		label.setLayoutData(data);

		data = new GridData();
		data.horizontalSpan = numColumns - 2;
		data.heightHint = 40;

		/*
		 * Here we create the scale
		 */
		scale = new Scale(parent, SWT.HORIZONTAL);
		scale.setMaximum(100);
		scale.setMinimum(0);
		scale.setPageIncrement(10);
		scale.setIncrement(1);
		scale.setLayoutData(data);
		scale.setSelection((int) (value * 100));

		data = new GridData();
		data.horizontalSpan = 1;
		data.heightHint = 15;

		text = new Text(parent, SWT.BORDER);
		text.setMessage("00.00");
		text.setLayoutData(data);
		text.setText("" + value);

		scale.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				/*
				 * when the selection scale change we update the value in the
				 * text control
				 */
				Scale s = (Scale) e.getSource();
				value = s.getSelection() / 100.0;
				String st = String.format("%.2f", value);
				st = st.replace(",", ".");
				text.setText(st);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		text.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {
				e.doit = false;
				String value_t = text.getText();
				/*
				 * Here we check the text in the text control
				 */

				if ((int) e.character == 8) // It means backspace char so we
											// remove the last char
				{
					int ind = text.getCaretPosition();

					if (ind == 0)
						return;
					if (ind == value_t.length()) {
						value_t = value_t.substring(0, value_t.length() - 1);
					} else {
						String tmp = value_t.substring(0, ind - 1);
						value_t = tmp + value_t.substring(ind, value_t.length());
					}
					text.setText(value_t);
					return;
				} else if (text.getSelectionCount() > 0) // If the user has
															// selected some
															// chars we remove
															// if
				{
					String tmp = text.getSelectionText();
					value_t = value_t.replace(tmp, "");
				}

				if ((int) e.character != 0) // If the user add a char in the
											// control
				{

					if (Character.isDigit(e.character) || e.character == '.') {
						if (value_t.contains(".") && e.character == '.')
							return;

						/*
						 * Here we check if the user has gave a number and if
						 * it's between 0 and 1
						 */
						try {
							double d = Double.parseDouble(value_t + e.character);
							if (d < 0) {
								text.setText("0.00");
								return;
							} else if (d > 1) {
								text.setText("1.00");
								return;
							}
							value = d;

							String st = String.format("%.2f", value);
							st = st.replace(",", ".");

							scale.setSelection((int) (value * 100));
							e.doit = true;
						} catch (NumberFormatException ex) {
							MessageBox mb = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);
							System.out.println(ex.getMessage());
							ex.printStackTrace();
							mb.setText("Error value");
							mb.setMessage("You must select a number");
							mb.open();
							text.setText("0.5");
						}
					}

				} else {
					try {
						/*
						 * Here it's for when we update the Text control with
						 * setText method we check if the number is between 0
						 * and 1
						 */
						double d = Double.parseDouble(e.text);
						if (d < 0) {
							d = 0;
							e.text = "0.00";
						} else if (d > 1) {
							d = 1;
							e.text = "1.00";
						}
						value = d;
						scale.setSelection((int) (value * 100));
						e.doit = true;
					} catch (NumberFormatException ex) {
						MessageBox mb = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);
						System.out.println(ex.getMessage());
						ex.printStackTrace();
						mb.setText("Error value");
						mb.setMessage("You must select a number");
						mb.open();
						text.setText("0.5");
					}
				}
			}
		});

	}

	@Override
	protected void doLoad() {
		value = getPreferenceStore().getDouble(getPreferenceName());
		scale.setSelection((int) (value * 100));
		String st = String.format("%.2f", value);
		st = st.replace(",", ".");
		text.setText(st);
	}

	@Override
	protected void doLoadDefault() {
		value = getPreferenceStore().getDefaultDouble(getPreferenceName());
		scale.setSelection((int) (value * 100));
		String st = String.format("%.2f", value);
		st = st.replace(",", ".");
		text.setText(st);
	}

	@Override
	protected void doStore() {
		value = scale.getSelection() / 100.0;
		getPreferenceStore().setValue(getPreferenceName(), value);
	}

	@Override
	public int getNumberOfControls() {
		return 3;
	}

}
