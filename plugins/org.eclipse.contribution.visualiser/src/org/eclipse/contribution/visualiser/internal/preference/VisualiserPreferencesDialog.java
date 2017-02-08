/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Sian Whiting - initial version
 *******************************************************************************/
package org.eclipse.contribution.visualiser.internal.preference;

import org.eclipse.contribution.visualiser.VisualiserPlugin;
import org.eclipse.contribution.visualiser.text.VisualiserMessages;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog containing Visualiser preferences Mostly copied from
 * org.eclipse.jface.preference.PreferenceDialog
 */
public class VisualiserPreferencesDialog extends Dialog implements IPreferencePageContainer {

	public static final String PREF_DLG_TITLE_IMG = "visualiser_preference_dialog_title_image"; //$NON-NLS-1$
	public static final String PREF_DLG_IMG_TITLE_ERROR = DLG_IMG_MESSAGE_ERROR; //$NON-NLS-1$

	static {
		ImageRegistry reg = JFaceResources.getImageRegistry();
		reg.put(PREF_DLG_TITLE_IMG,
				ImageDescriptor.createFromFile(PreferenceDialog.class, "images/pref_dialog_title.gif")); //$NON-NLS-1$
	}
	private Label titleImage;
	private CLabel messageLabel;
	protected Color titleAreaColor;
	private String message = ""; //$NON-NLS-1$
	private Composite titleArea;
	private Color normalMsgAreaBackground;
	private Color errorMsgAreaBackground;
	private Image messageImage;
	private Image errorMsgImage;
	private boolean showingError = false;
	private VisualiserPreferencePage visPage;
	private String errorMessage;

	/**
	 * Default constructor
	 * 
	 * @param parentShell
	 */
	public VisualiserPreferencesDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Override to set the title
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(VisualiserMessages.VisualiserPreferencePage_title);
	}

	/**
	 * Create the contents of the dialog
	 */
	protected Control createDialogArea(Composite parent) {
		GridData gd;
		Composite composite = (Composite) super.createDialogArea(parent);

		// Build the title area and separator line
		Composite titleComposite = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		titleComposite.setLayout(layout);
		titleComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createTitleArea(titleComposite);

		// Build the Page container
		Composite pageContainer = createPageContainer(composite);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 400;
		gd.widthHint = 450;
		pageContainer.setLayoutData(gd);
		pageContainer.setFont(parent.getFont());

		// Build the separator line
		Label separator = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		separator.setLayoutData(gd);

		return composite;
	}

	/**
	 * Creates the dialog's title area.
	 * 
	 * @param parent
	 *            the SWT parent for the title area composite
	 * @return the created title area composite
	 */
	private Composite createTitleArea(Composite parent) {
		// Create the title area which will contain
		// a title, message, and image.
		titleArea = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 2;
		layout.marginWidth = 2;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		layout.numColumns = 2;

		// Get the background color for the title area
		Display display = parent.getDisplay();
		Color background = JFaceColors.getBannerBackground(display);
		final Color foreground = JFaceColors.getBannerForeground(display);

		GridData layoutData = new GridData(GridData.FILL_BOTH);
		titleArea.setLayout(layout);
		titleArea.setLayoutData(layoutData);
		titleArea.setBackground(background);

		RGB rgb = new RGB(171, 168, 165);
		final Color borderColor = new Color(titleArea.getDisplay(), rgb);

		titleArea.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.setForeground(borderColor);
				Rectangle bounds = titleArea.getClientArea();
				bounds.height = bounds.height - 2;
				bounds.width = bounds.width - 1;
				e.gc.drawRectangle(bounds);
			}
		});

		// Add a dispose listener
		titleArea.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				if (titleAreaColor != null)
					titleAreaColor.dispose();
				if (errorMsgAreaBackground != null)
					errorMsgAreaBackground.dispose();
				borderColor.dispose();
			}
		});

		// Message label
		messageLabel = new CLabel(titleArea, SWT.LEFT);
		JFaceColors.setColors(messageLabel, foreground, background);
		messageLabel.setText(" "); //$NON-NLS-1$
		messageLabel.setFont(JFaceResources.getBannerFont());
		GridData gd = new GridData(GridData.FILL_BOTH);
		messageLabel.setLayoutData(gd);

		// Title image
		titleImage = new Label(titleArea, SWT.LEFT);
		titleImage.setBackground(background);
		titleImage.setImage(JFaceResources.getImage(PREF_DLG_TITLE_IMG));
		gd = new GridData();
		gd.horizontalAlignment = GridData.END;
		titleImage.setLayoutData(gd);

		return titleArea;
	}

	/**
	 * Display the given error message. The currently displayed message is saved
	 * and will be redisplayed when the error message is set to
	 * <code>null</code>.
	 * 
	 * @param newErrorMessage
	 *            the errorMessage to display or <code>null</code>
	 */
	public void setErrorMessage(String newErrorMessage) {
		// Any change?
		if (errorMessage == null ? newErrorMessage == null : errorMessage.equals(newErrorMessage))
			return;

		errorMessage = newErrorMessage;
		if (errorMessage == null) {
			if (showingError) {
				// we were previously showing an error
				showingError = false;
				messageLabel.setBackground(normalMsgAreaBackground);
				messageLabel.setImage(null);
				titleImage.setImage(JFaceResources.getImage(PREF_DLG_TITLE_IMG));
			}

			// avoid calling setMessage in case it is overridden to call
			// setErrorMessage,
			// which would result in a recursive infinite loop
			if (message == null)
				// this should probably never happen since setMessage does this
				// conversion....
				message = ""; //$NON-NLS-1$
			messageLabel.setText(message);
			messageLabel.setImage(messageImage);
			messageLabel.setToolTipText(message);
		} else {
			messageLabel.setText(errorMessage);
			messageLabel.setToolTipText(errorMessage);
			if (!showingError) {
				// we were not previously showing an error
				showingError = true;

				// lazy initialize the error background color and image
				if (errorMsgAreaBackground == null) {
					errorMsgAreaBackground = JFaceColors.getErrorBackground(messageLabel.getDisplay());
					errorMsgImage = JFaceResources.getImage(PREF_DLG_IMG_TITLE_ERROR);
				}

				// show the error
				normalMsgAreaBackground = messageLabel.getBackground();
				messageLabel.setBackground(errorMsgAreaBackground);
				messageLabel.setImage(errorMsgImage);
				titleImage.setImage(null);
			}
		}
		titleArea.layout(true);
	}

	/**
	 * Creates the inner page container.
	 */
	private Composite createPageContainer(Composite parent) {
		Composite result = new Composite(parent, SWT.NULL);
		result.setLayout(new FillLayout());
		visPage = new VisualiserPreferencePage();
		visPage.setContainer(this);
		visPage.setTitle(VisualiserMessages.VisualiserPreferencePage_title);
		if (visPage.getControl() == null) {
			visPage.createControl(result);
		}
		return result;
	}

	/**
	 * Get the preference store
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#getPreferenceStore()
	 */
	public IPreferenceStore getPreferenceStore() {
		return VisualiserPlugin.getDefault().getPreferenceStore();
	}

	/**
	 * Update the enabled state of buttons in the page
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateButtons()
	 */
	public void updateButtons() {
	}

	/**
	 * Set the message text. If the message line currently displays an error,
	 * the message is stored and will be shown after a call to clearErrorMessage
	 * <p>
	 * Shortcut for <code>setMessage(newMessage, NONE)</code>
	 * </p>
	 * 
	 * @param newMessage
	 *            the message, or <code>null</code> to clear the message
	 */
	public void setMessage(String newMessage) {
		setMessage(newMessage, IMessageProvider.NONE);
	}

	/**
	 * Sets the message for this dialog with an indication of what type of
	 * message it is.
	 * <p>
	 * The valid message types are one of <code>NONE</code>,
	 * <code>INFORMATION</code>, <code>WARNING</code>, or <code>ERROR</code>.
	 * </p>
	 * <p>
	 * Note that for backward compatibility, a message of type
	 * <code>ERROR</code> is different than an error message (set using
	 * <code>setErrorMessage</code>). An error message overrides the current
	 * message until the error message is cleared. This method replaces the
	 * current message and does not affect the error message.
	 * </p>
	 * 
	 * @param newMessage
	 *            the message, or <code>null</code> to clear the message
	 * @param newType
	 *            the message type
	 */
	public void setMessage(String newMessage, int newType) {
		Image newImage = null;

		if (newMessage != null) {
			switch (newType) {
			case IMessageProvider.NONE:
				break;
			case IMessageProvider.INFORMATION:
				newImage = JFaceResources.getImage(DLG_IMG_MESSAGE_INFO);
				break;
			case IMessageProvider.WARNING:
				newImage = JFaceResources.getImage(DLG_IMG_MESSAGE_WARNING);
				break;
			case IMessageProvider.ERROR:
				newImage = JFaceResources.getImage(DLG_IMG_MESSAGE_ERROR);
				break;
			}
		}

		showMessage(newMessage, newImage);
	}

	/**
	 * Show the new message
	 */
	private void showMessage(String newMessage, Image newImage) {
		// Any change?
		if (message.equals(newMessage) && messageImage == newImage)
			return;

		message = newMessage;
		if (message == null)
			message = ""; //$NON-NLS-1$
		messageImage = newImage;

		if (!showingError) {
			// we are not showing an error
			messageLabel.setText(message);
			messageLabel.setImage(messageImage);
			messageLabel.setToolTipText(message);
		}
	}

	/**
	 * Update the message
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateMessage()
	 */
	public void updateMessage() {
		String pageMessage = visPage.getMessage();
		int pageMessageType = IMessageProvider.NONE;
		if (pageMessage != null)
			pageMessageType = ((IMessageProvider) visPage).getMessageType();

		String pageErrorMessage = visPage.getErrorMessage();

		// Adjust the font
		if (pageMessage == null && pageErrorMessage == null)
			messageLabel.setFont(JFaceResources.getBannerFont());
		else
			messageLabel.setFont(JFaceResources.getDialogFont());

		// Set the message and error message
		if (pageMessage == null) {
			setMessage(visPage.getTitle());
		} else {
			setMessage(pageMessage, pageMessageType);
		}
		setErrorMessage(pageErrorMessage);
	}

	/**
	 * Update the title
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateTitle()
	 */
	public void updateTitle() {
	}

	/**
	 * Disposes the preference page
	 */
	public boolean close() {
		if (visPage != null) {
			visPage.dispose();
		}
		return super.close();
	}

	/**
	 * Called when OK is pressed in the dialog. Send OK to the visualiser
	 * prefernce page contained in this dialog
	 */
	protected void okPressed() {
		visPage.performOk();
		super.okPressed();
	}

}
