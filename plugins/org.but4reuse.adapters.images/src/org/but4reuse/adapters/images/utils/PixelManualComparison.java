package org.but4reuse.adapters.images.utils;

import java.text.DecimalFormat;

import org.but4reuse.adapters.images.PixelElement;
import org.but4reuse.adapters.impl.IManualComparison;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

/**
 * Pixels manual comparison
 * 
 * @author jabier.martinez
 * 
 */
public class PixelManualComparison implements IManualComparison {

	int result = 0;
	double calculatedSimilarity;
	PixelElement element;
	PixelElement anotherElement;

	public PixelManualComparison(double calculatedSimilarity, PixelElement element, PixelElement anotherElement) {
		this.calculatedSimilarity = calculatedSimilarity;
		this.element = element;
		this.anotherElement = anotherElement;
	}

	@Override
	public void run() {
		ComparisonDialog cd = new ComparisonDialog(Display.getCurrent().getActiveShell());
		result = cd.open();
	}

	@Override
	public int getResult() {
		return result;
	}

	public class ComparisonDialog extends Dialog {

		private int manualResult;

		public ComparisonDialog(Shell parent) {
			this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		}

		public ComparisonDialog(Shell parent, int style) {
			super(parent, style);
			setText("Calculated similarity: " + new DecimalFormat("#").format(calculatedSimilarity * 100));
		}

		public int getInput() {
			return manualResult;
		}

		public int open() {
			Shell shell = new Shell(getParent(), getStyle());
			shell.setText(getText());

			Monitor primary = shell.getDisplay().getPrimaryMonitor();
			Rectangle bounds = primary.getBounds();
			Rectangle rect = shell.getBounds();
			int x = bounds.x + (bounds.width - rect.width) / 2;
			int y = bounds.y + (bounds.height - rect.height) / 2;

			shell.setLocation(x, y);
			createContents(shell);
			shell.pack();
			shell.open();
			Display display = getParent().getDisplay();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			return manualResult;
		}

		private void createContents(final Shell shell) {
			shell.setLayout(new GridLayout(3, true));

			Label label = new Label(shell, SWT.NONE);
			label.setText("Can we consider them equal?");
			GridData data = new GridData();
			data.horizontalSpan = 3;
			label.setLayoutData(data);

			Label label1 = new Label(shell, SWT.NONE);
			label1.setText(element.getText());
			data = new GridData();
			data.horizontalSpan = 3;
			label1.setLayoutData(data);

			Label label2 = new Label(shell, SWT.NONE);
			label2.setText(anotherElement.getText());
			data = new GridData();
			data.horizontalSpan = 3;
			label2.setLayoutData(data);

			final Canvas canvas = new Canvas(shell, SWT.BORDER);
			GridData datac = new GridData(GridData.FILL_BOTH);
			datac.horizontalSpan = 3;
			canvas.setLayoutData(datac);

			canvas.addPaintListener(new PaintListener() {
				public void paintControl(PaintEvent e) {
					Color pixel1 = new Color(Display.getCurrent(), element.color);
					e.gc.setBackground(pixel1);
					e.gc.setAlpha(element.alpha);
					e.gc.fillRectangle(0, 0, canvas.getBounds().width / 2, canvas.getBounds().height);
					Color pixel2 = new Color(Display.getCurrent(), anotherElement.color);
					e.gc.setBackground(pixel2);
					e.gc.setAlpha(anotherElement.alpha);
					e.gc.fillRectangle(canvas.getBounds().width / 2, 0, canvas.getBounds().width / 2,
							canvas.getBounds().height);
				}
			});

			Button ok = new Button(shell, SWT.PUSH);
			ok.setText("Yes");
			data = new GridData(GridData.FILL_HORIZONTAL);
			ok.setLayoutData(data);
			ok.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					manualResult = 0;
					shell.close();
				}
			});

			Button no = new Button(shell, SWT.PUSH);
			no.setText("No");
			data = new GridData(GridData.FILL_HORIZONTAL);
			no.setLayoutData(data);
			no.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					manualResult = 1;
					shell.close();
				}
			});

			Button cancel = new Button(shell, SWT.PUSH);
			cancel.setText("Deactivate manual equal");
			data = new GridData(GridData.FILL_HORIZONTAL);
			cancel.setLayoutData(data);
			cancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					manualResult = 2;
					shell.close();
				}
			});

			shell.setDefaultButton(no);
		}
	}

}
