package org.but4reuse.utils.ui.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;

public class ScaleLabel {

	public enum Type {
		INT, DOUBLE;
	}

	private Scale scaleC;
	private Text textC;
	private double value;
	private double minVal;
	private double maxVal;
	private Type typeC;

	public ScaleLabel(Scale scale, Text text, Type type, double min, double max) {

		scaleC = scale;
		textC = text;
		minVal = min;
		maxVal = max;
		typeC = type;
		scaleC.setIncrement(1);
		if (typeC == Type.INT) {
			scale.setMinimum((int) min);
			scale.setMaximum((int) max);
			scaleC.setPageIncrement(50);
		} else if (typeC == Type.DOUBLE) {
			scale.setMinimum((int) min * 100);
			scale.setMaximum((int) max * 100);
			scaleC.setPageIncrement((int) (min + max) / 10);
		}
		scaleC.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				/*
				 * when the selection scale change we update the value in the
				 * text control
				 */
				Scale s = (Scale) e.getSource();
				value = s.getSelection();
				if (typeC == Type.INT)
					textC.setText(value + "");
				else
					textC.setText(String.format("%.2f", value));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		textC.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {
				e.doit = false;
				String value_t = textC.getText();
				/*
				 * Here we check the text in the text control
				 */

				if ((int) e.character == 8) // It means backspace char so we
											// remove the last char
				{
					int ind = textC.getCaretPosition();

					if (ind == 0)
						return;
					if (ind == value_t.length()) {
						value_t = value_t.substring(0, value_t.length() - 1);
					} else {
						String tmp = value_t.substring(0, ind - 1);
						value_t = tmp + value_t.substring(ind, value_t.length());
					}
					textC.setText(value_t);
					return;
				} else if (textC.getSelectionCount() > 0) // If the user has
															// selected some
															// chars we remove
															// if
				{
					String tmp = textC.getSelectionText();
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
							if (d < minVal) {
								textC.setText(String.format("%.2f", minVal));
								return;
							} else if (d > maxVal) {
								textC.setText(String.format("%.2f", maxVal));
								return;
							}
							value = d;

							String st = String.format("%.2f", value);
							st = st.replace(",", ".");

							if (typeC == Type.DOUBLE)
								scaleC.setSelection((int) (value * 100));
							if (typeC == Type.INT)
								scaleC.setSelection((int) value);

							e.doit = true;
						} catch (NumberFormatException ex) {
							MessageBox mb = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);
							System.out.println(ex.getMessage());
							ex.printStackTrace();
							mb.setText("Error value");
							mb.setMessage("You must select a number");
							mb.open();
							textC.setText((minVal + maxVal) / 2 + "");
						}
					}

				} else {
					try {
						/*
						 * Here it's for when we update the Text control with
						 * setText method we check if the number is between 0
						 * and 1
						 */

						e.text = e.text.replace(',', '.');
						double d = Double.parseDouble(e.text);

						if (d < minVal)
							d = minVal;
						else if (d > maxVal)
							d = maxVal;

						if (typeC == Type.DOUBLE)
							d /= 100;
						e.text = String.format("%.2f", d);

						e.doit = true;
					} catch (NumberFormatException ex) {
						MessageBox mb = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);
						System.out.println(ex.getMessage());
						ex.printStackTrace();
						mb.setText("Error value");
						mb.setMessage("You must select a number");
						mb.open();
						textC.setText((minVal + maxVal) / 2 + "");
					}
				}
			}
		});

	}

	public void change(Type type, double min, double max) {
		minVal = min;
		maxVal = max;
		typeC = type;

		if (typeC == Type.INT) {
			scaleC.setPageIncrement(50);
			value = (int) ((minVal + maxVal) / 2);
		} else if (typeC == Type.DOUBLE) {
			minVal *= 100;
			maxVal *= 100;
			value = (minVal + maxVal) / 2;
			scaleC.setPageIncrement((int) (minVal + maxVal) / 10);
		}

		scaleC.setMinimum((int) minVal);
		scaleC.setMaximum((int) maxVal);
		scaleC.setSelection((int) (minVal + maxVal) / 2);
		scaleC.setSelection((int) value);

		textC.setText(value + "");

	}

}
