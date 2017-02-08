package org.but4reuse.feature.location.lsi.location.preferences;

import org.but4reuse.utils.ui.preferences.ScaleLabel;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;

public class ParameterLSIFieldEditor extends FieldEditor {

	private Composite parent;
	private double value;
	private boolean isFixed;
	private String valueName;
	private Scale scale;
	private Text text;
	private Button rate;
	private Button fixed;
	private ScaleLabel scaleL;

	public ParameterLSIFieldEditor(String preferenceName, String name, Composite parent, String valueName) {
		super(preferenceName, name, parent);
		this.valueName = valueName;

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
		text = null;
		scale = null;
		Group gr = new Group(parent, SWT.NORMAL);
		GridData data = new GridData();
		data.horizontalSpan = numColumns;
		data.heightHint = 40;

		gr.setData(data);
		gr.setLayout(new FillLayout());
		rate = new Button(gr, SWT.RADIO);
		rate.setText("Rate");
		rate.setToolTipText("Set how many percent of dimension that you want use");
		rate.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				isFixed = false;
				update();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		fixed = new Button(gr, SWT.RADIO);
		fixed.setText("Fixed Value");
		fixed.setToolTipText("Set How many dimensions you want (Max)");
		fixed.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				isFixed = true;
				update();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		data = new GridData();
		data.horizontalSpan = numColumns;
		data.heightHint = 40;
		scale = new Scale(parent, SWT.NORMAL);
		scale.setData(data);

		data = new GridData();
		data.horizontalSpan = 2;
		data.heightHint = 15;
		text = new Text(parent, SWT.BORDER);
		scale.setData(data);
		if (rate.getSelection())
			scaleL = new ScaleLabel(scale, text, ScaleLabel.Type.DOUBLE, 0.0, 1.0);
		else
			scaleL = new ScaleLabel(scale, text, ScaleLabel.Type.INT, 1.0, 300.0);

		if (isFixed) {
			rate.setSelection(false);
			fixed.setSelection(true);
		} else {
			rate.setSelection(false);
			fixed.setSelection(true);
		}
		update();

	}

	@Override
	protected void doLoad() {
		value = getPreferenceStore().getDouble(getPreferenceName());
		isFixed = getPreferenceStore().getBoolean(valueName);
		if (isFixed) {
			rate.setSelection(false);
			fixed.setSelection(true);
			scaleL.change(ScaleLabel.Type.INT, 1.0, 300.0);
			scale.setSelection((int) value);
			text.setText(value + "");
		} else {
			rate.setSelection(true);
			fixed.setSelection(false);
			scaleL.change(ScaleLabel.Type.DOUBLE, 0.0, 1.0);
			scale.setSelection((int) (value * 100));
			text.setText(value * 100 + "");
		}

	}

	@Override
	protected void doLoadDefault() {
		value = getPreferenceStore().getDefaultInt(getPreferenceName());
		isFixed = getPreferenceStore().getDefaultBoolean(valueName);
		if (isFixed) {
			rate.setSelection(false);
			fixed.setSelection(true);
			scaleL.change(ScaleLabel.Type.INT, 1.0, 300.0);
			scale.setSelection((int) value);
			text.setText(value + "");
		} else {
			rate.setSelection(true);
			fixed.setSelection(false);
			scaleL.change(ScaleLabel.Type.DOUBLE, 0.0, 1.0);
			scale.setSelection((int) (value * 100));
			text.setText(value * 100 + "");
		}

	}

	@Override
	protected void doStore() {
		value = scale.getSelection();
		if (!isFixed)
			value /= 100.0;
		getPreferenceStore().setValue(getPreferenceName(), value);
		getPreferenceStore().setValue(valueName, isFixed);
	}

	@Override
	public int getNumberOfControls() {
		return 4;
	}

	private void update() {

		if (rate.getSelection())
			scaleL.change(ScaleLabel.Type.DOUBLE, 0.0, 1.0);
		else
			scaleL.change(ScaleLabel.Type.INT, 1.0, 300.0);

	}
}
