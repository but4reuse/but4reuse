package org.but4reuse.utils.ui.dialogs;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * URI selection dialog
 * 
 * @author jabier.martinez
 */

public class URISelectionDialog extends InputDialog {

	static IInputValidator validator = new IInputValidator() {

		@Override
		public String isValid(String newText) {
			try {
				new URI(newText);
			} catch (URISyntaxException e) {
				return "Invalid URI";
			}
			return null;
		}

	};

	public URISelectionDialog(Shell parentShell, String dialogTitle, String dialogMessage, String initialValue) {
		super(parentShell, dialogTitle, dialogMessage, initialValue, validator);
	}

}
