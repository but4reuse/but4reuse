package org.but4reuse.adapters.eclipse.generator.dialogs;

import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SummaryDialog extends ScrollableMessageDialog {

	private Text scrollable;
	private boolean isCloseable;
	Button okButton;
	
	public SummaryDialog(Shell parentShell, String title, String text, String scrollableText) {
		super(parentShell, title, text, scrollableText);
		this.isCloseable = true;
	}
	
	public SummaryDialog(Shell parentShell, String title, String text, String scrollableText, boolean isCloseable) {
		this(parentShell,title,text,scrollableText);
		this.isCloseable = isCloseable;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(600, 650);
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
