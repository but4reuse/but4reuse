package org.but4reuse.adapters.eclipse.generator.dialogs;

import org.but4reuse.utils.ui.dialogs.ScrollableMessageDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

public class SummaryDialog extends ScrollableMessageDialog{

	public SummaryDialog(Shell parentShell, String title, String text, String scrollableText) {
		super(parentShell, title, text, scrollableText);
	}
	
	public SummaryDialog(Shell parentShell, String title, String text, String scrollableText, boolean isCloseable) {
		super(parentShell, title, text, scrollableText, isCloseable);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(600, 650);
	}

}
