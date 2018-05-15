package org.but4reuse.adapters.eclipse.benchmark.generator.dialogs;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Julien Margarido
 * @author Felix Lima Gorito
 */
public class SummaryDialog extends TitleAreaDialog {

	private String title;
	private String text;
	private String scrollableText;
	private Text scrollable;
	private boolean isCloseable;
	private Button okButton;

	public SummaryDialog(Shell parentShell, String title, String text, String scrollableText) {
		super(parentShell);
		this.title = title;
		this.text = text;
		this.scrollableText = scrollableText;
		this.isCloseable = true;
		setHelpAvailable(false);
	}

	public SummaryDialog(Shell parentShell, String title, String text, String scrollableText, boolean isCloseable) {
		this(parentShell, title, text, scrollableText);
		this.isCloseable = isCloseable;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(600, 650);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;

		scrollable = new Text(composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
		scrollable.setLayoutData(gridData);
		scrollable.setText(scrollableText);

		return composite;
	}

	@Override
	public void create() {
		super.create();
		setTitle(title);
		setMessage(text, IMessageProvider.INFORMATION);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, OK, "OK", true);
		if (!isCloseable)
			okButton.setEnabled(false);
		okButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isCloseable)
					close();
			}
		});
	}

	@Override
	protected boolean isResizable() {
		// Allow the user to change the dialog size!
		return true;
	}

	public void setScrollableText(String str) {
		this.scrollable.setText(str);
	}

	public String getScrollableText() {
		return this.scrollable.getText();
	}

	public void setCloseable(boolean isCloseable) {
		this.isCloseable = isCloseable;
		if (this.okButton != null)
			this.okButton.setEnabled(isCloseable);
	}

	public boolean isDisposed() {
		return this.getShell().isDisposed();
	}

	@Override
	protected ShellListener getShellListener() {
		return new ShellAdapter() {
			public void shellClosed(ShellEvent event) {
				event.doit = isCloseable;
			}
		};
	}

}
