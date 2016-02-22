package org.but4reuse.utils.ui.dialogs;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ScrollableMessageDialog extends TitleAreaDialog {
	public String title;
	public String text;
	public String scrollableText;
	private Text scrollable;
	private boolean isCloseable;
	Button okButton;
	
	public ScrollableMessageDialog(Shell parentShell, String title, String text, String scrollableText) {
		super(parentShell);
		this.title = title;
		this.text = text;
		this.scrollableText = scrollableText;
		this.isCloseable = true;
		setHelpAvailable(false);
	}
	
	public ScrollableMessageDialog(Shell parentShell, String title, String text, String scrollableText, boolean isCloseable) {
		this(parentShell,title,text,scrollableText);
		this.isCloseable = isCloseable;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent); // Let the dialog create
																		  // the parent composite
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true; // Layout vertically, too!
		gridData.verticalAlignment = GridData.FILL;

		scrollable = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
		scrollable.setLayoutData(gridData);
		scrollable.setText(scrollableText);

		return composite;
	}

	@Override
	public void create() {
		super.create();

		// This is not necessary; the dialog will become bigger as the text
		// grows but at the same time,
		// the user will be able to see all (or at least more) of the error
		// message at once
		// getShell ().setSize (300, 300);
		setTitle(title);
		setMessage(text, IMessageProvider.INFORMATION);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, OK, "OK", true);
		if(!isCloseable) okButton.setEnabled(false);
		okButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isCloseable) close();
			}
		});
	}

	@Override
	protected boolean isResizable() {
		return true; // Allow the user to change the dialog size!
	}
	
	public void setScrollableText(String str) {
		this.scrollable.setText(str);
	}
	
	public String getScrollableText() {
		return this.scrollable.getText();
	}
	
	public void setCloseable(boolean isCloseable){
		this.isCloseable = isCloseable;
		if(this.okButton != null) this.okButton.setEnabled(isCloseable);
	}
	
	public boolean isDisposed(){
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
