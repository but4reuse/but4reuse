package org.but4reuse.adapters.ui.actions;

import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ScrollableMessageChangeNameDialog extends ScrollableMessageDialog {

	public String name;

	public ScrollableMessageChangeNameDialog(Shell parentShell, String title, String text, String scrollableText) {
		super(parentShell, title, text, scrollableText);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("Edit name: ");
		final Text nameText = new Text(parent, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		nameText.setLayoutData(gridData);
		nameText.setText(title);
		nameText.setSelection(0, title.length());
		nameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				name = nameText.getText();
			}
		});
		Composite composite = (Composite) super.createDialogArea(parent);
		return composite;
	}

}
