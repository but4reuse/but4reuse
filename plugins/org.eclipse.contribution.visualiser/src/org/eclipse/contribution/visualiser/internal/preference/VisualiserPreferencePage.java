/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Adrian Colyer - initial version
 *     Sian Whiting - added tabbed layout and drawing options
 *     Matt Chapman - changes to support new rendering options
 *******************************************************************************/
package org.eclipse.contribution.visualiser.internal.preference;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.contribution.visualiser.VisualiserPlugin;
import org.eclipse.contribution.visualiser.core.PaletteDefinition;
import org.eclipse.contribution.visualiser.core.PaletteManager;
import org.eclipse.contribution.visualiser.core.ProviderDefinition;
import org.eclipse.contribution.visualiser.core.ProviderManager;
import org.eclipse.contribution.visualiser.core.RendererDefinition;
import org.eclipse.contribution.visualiser.core.RendererManager;
import org.eclipse.contribution.visualiser.interfaces.IMarkupProvider;
import org.eclipse.contribution.visualiser.interfaces.IVisualiserPalette;
import org.eclipse.contribution.visualiser.interfaces.IVisualiserRenderer;
import org.eclipse.contribution.visualiser.palettes.PatternVisualiserPalette;
import org.eclipse.contribution.visualiser.renderers.PatternVisualiserRenderer;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleMarkupProvider;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleMember;
import org.eclipse.contribution.visualiser.text.VisualiserMessages;
import org.eclipse.contribution.visualiser.utils.ColorConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The preference page for the Visualiser plugin
 */
public class VisualiserPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private Text descriptionText;

	private CheckboxTableViewer checkboxViewer;

	private List styleList;

	private List colourList;

	private Scale prefWidth;

	private Scale stripeHeight;

	private VisualiserPreview preview;

	/**
	 * Create the contents of the page
	 * 
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);

		TabFolder folder = new TabFolder(composite, SWT.NONE);
		folder.setLayout(new TabFolderLayout());
		folder.setLayoutData(new GridData(GridData.FILL_BOTH));

		Control providerControl = createProviderControl(folder);
		Control drawingOptionsControl = createDrawingOptionsControl(folder);

		TabItem item = new TabItem(folder, SWT.NONE);
		item.setText(VisualiserMessages.VisualiserPreferencePage_providers);
		item.setControl(providerControl);

		item = new TabItem(folder, SWT.NONE);
		item.setText(VisualiserMessages.VisualiserPreferencePage_drawingOptions);
		item.setControl(drawingOptionsControl);

		populateProviders();

		return composite;
	}

	/**
	 * Subsidiary method for createContents(). Creates the contents of the
	 * drawing options tab.
	 * 
	 * @param parent
	 * @return the created control
	 */
	private Control createDrawingOptionsControl(TabFolder parent) {
		GridLayout layout = new GridLayout();
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(layout);
		createDrawingStyleArea(composite);
		return composite;
	}

	/**
	 * Subsidiary method for createContents(). Creates the contents of the
	 * providers tab.
	 * 
	 * @param parent
	 * @return the created control
	 */
	private Control createProviderControl(TabFolder parent) {
		GridLayout layout = new GridLayout();
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(layout);
		createProvidersArea(composite);
		createDescriptionArea(composite);
		return composite;
	}

	private void createDrawingStyleArea(Composite mainComposite) {
		java.util.List renderers = RendererManager.getAllRendererDefinitions();
		java.util.List rnames = new ArrayList();
		for (Iterator iter = renderers.iterator(); iter.hasNext();) {
			RendererDefinition rd = (RendererDefinition) iter.next();
			rnames.add(rd.getName());
		}
		java.util.List palettes = PaletteManager.getAllPaletteDefinitions();
		java.util.List pnames = new ArrayList();
		for (Iterator iter = palettes.iterator(); iter.hasNext();) {
			PaletteDefinition rd = (PaletteDefinition) iter.next();
			pnames.add(rd.getName());
		}

		Composite drawingComposite = new Composite(mainComposite, SWT.NONE);
		drawingComposite.setLayout((new GridLayout(2, true)));
		GridData gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_FILL);
		drawingComposite.setLayoutData(gd);

		Group styleGroup = new Group(drawingComposite, SWT.NONE);
		styleGroup.setLayout(new GridLayout());
		styleGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_FILL));
		styleGroup.setText(VisualiserMessages.VisualiserPreferencePage_drawingStyle);

		styleList = new List(styleGroup, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
		gd.heightHint = 65;
		styleList.setLayoutData(gd);
		styleList.setItems((String[]) rnames.toArray(new String[] {}));
		String rname = VisualiserPreferences.getRendererName();
		if ((rname == null) || (rname.length() == 0)) {
			// hasn't been set, use the default
			rname = RendererManager.getDefaultRenderer().getName();
		}

		Group colourGroup = new Group(drawingComposite, SWT.NONE);
		colourGroup.setLayout(new GridLayout());
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
		colourGroup.setLayoutData(gd);
		colourGroup.setText(VisualiserMessages.VisualiserPreferencePage_colorSet);

		colourList = new List(colourGroup, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
		gd.heightHint = 65;
		colourList.setLayoutData(gd);
		colourList.setItems((String[]) pnames.toArray(new String[] {}));
		String pname = PaletteManager.getCurrentPalette().getName();

		Group stripeHGroup = new Group(drawingComposite, SWT.NONE);
		stripeHGroup.setLayout(new GridLayout());
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
		stripeHGroup.setLayoutData(gd);
		stripeHGroup.setText(VisualiserMessages.VisualiserPreferencePage_stripeHeight);

		stripeHeight = new Scale(stripeHGroup, SWT.HORIZONTAL);
		stripeHeight.setMinimum(1);
		stripeHeight.setMaximum(20);
		stripeHeight.setIncrement(1);
		stripeHeight.setSelection(VisualiserPreferences.getStripeHeight());
		gd = new GridData(GridData.FILL_HORIZONTAL);
		stripeHeight.setLayoutData(gd);

		Group prefWidthGroup = new Group(drawingComposite, SWT.NONE);
		prefWidthGroup.setLayout(new GridLayout());
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
		prefWidthGroup.setLayoutData(gd);
		prefWidthGroup.setText(VisualiserMessages.VisualiserPreferencePage_colWidth);

		prefWidth = new Scale(prefWidthGroup, SWT.HORIZONTAL);
		prefWidth.setMinimum(VisualiserPreferences.getMinBarSize());
		prefWidth.setMaximum(120);
		prefWidth.setIncrement(5);
		prefWidth.setSelection(VisualiserPreferences.getBarWidth());
		gd = new GridData(GridData.FILL_HORIZONTAL);
		prefWidth.setLayoutData(gd);

		Group canvasGroup = new Group(drawingComposite, SWT.NONE);
		canvasGroup.setLayout(new GridLayout());
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
		gd.grabExcessVerticalSpace = true;
		gd.horizontalSpan = 2;
		canvasGroup.setLayoutData(gd);
		canvasGroup.setText(VisualiserMessages.VisualiserPreferencePage_preview);

		preview = new VisualiserPreview(canvasGroup);
		gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 300;
		gd.heightHint = 100;
		preview.setLayoutData(gd);

		styleList.setSelection(new String[] { rname });
		styleList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				preview.redraw();
			}
		});

		colourList.setSelection(new String[] { pname });
		colourList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String pname = colourList.getSelection()[0];
				if (PaletteManager.getPaletteByName(pname).getPalette() instanceof PatternVisualiserPalette) {
					preview.localUsePatterns = true;
				} else {
					preview.localUsePatterns = false;
				}
				preview.redraw();
			}
		});

		stripeHeight.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				preview.redraw();
			}
		});

		prefWidth.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				preview.redraw();
			}
		});

	}

	class VisualiserPreview extends Canvas {
		private SimpleMember m = new SimpleMember(VisualiserMessages.VisualiserPreferencePage_preview_col1);

		private SimpleMember m2 = new SimpleMember(VisualiserMessages.VisualiserPreferencePage_preview_col2);

		private IVisualiserPalette ivp;

		private Color[] cols;

		private RGB[] colsForPatterns;

		private boolean localUsePatterns = VisualiserPreferences.getUsePatterns();

		VisualiserPreview(Composite parent) {
			super(parent, SWT.NO_BACKGROUND);
			addPaintListener(new PaintListener() {
				public void paintControl(PaintEvent e) {
					paint(e.gc);
				}
			});
			// accessibility: add listeners so we can receive focus
			addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					redraw();
				}

				public void focusLost(FocusEvent e) {
					redraw();
				}
			});
			addKeyListener(new KeyAdapter() {
			});
			addTraverseListener(new TraverseListener() {
				public void keyTraversed(TraverseEvent e) {
					switch (e.detail) {
					/* Do tab group traversal */
					case SWT.TRAVERSE_ESCAPE:
					case SWT.TRAVERSE_RETURN:
					case SWT.TRAVERSE_TAB_NEXT:
					case SWT.TRAVERSE_TAB_PREVIOUS:
					case SWT.TRAVERSE_PAGE_NEXT:
					case SWT.TRAVERSE_PAGE_PREVIOUS:
						e.doit = true;
						break;
					}
				}
			});
			setToolTipText(VisualiserMessages.VisualiserPreferencePage_preview);
		}

		private void paint(GC gc) {
			String rname = styleList.getSelection()[0];
			RendererDefinition rd = RendererManager.getRendererByName(rname);
			String pname = colourList.getSelection()[0];
			PaletteDefinition pd = PaletteManager.getPaletteByName(pname);
			if ((rd != null) && (pd != null)) {
				Rectangle clientRect = getClientArea();
				Image buffer = new Image(getDisplay(), clientRect.width, clientRect.height);
				GC sgc = new GC(buffer);
				sgc.setBackground(ColorConstants.menuBackground);
				sgc.fillRectangle(clientRect);

				IVisualiserRenderer r = rd.getRenderer();
				int x = r.getMarginSize();
				int y = r.getColumnHeaderHeight() + r.getMarginSize();
				int width = prefWidth.getSelection();
				int height = 70;
				r.paintColumnHeader(sgc, m, x, width);
				r.paintColumn(sgc, m, x, y, width, height, true);

				// paint a couple of stripes
				if (pd.getPalette() != ivp) {
					disposeCols();
				}
				if (cols == null) {
					RGB[] rgb = pd.getPalette().getRGBValues();
					colsForPatterns = new RGB[] { rgb[0], rgb[1], rgb[2], rgb[3], };
					cols = new Color[] { new Color(Display.getDefault(), rgb[0]),
							new Color(Display.getDefault(), rgb[1]), new Color(Display.getDefault(), rgb[2]),
							new Color(Display.getDefault(), rgb[3]) };
				}

				int h = stripeHeight.getSelection();
				if (localUsePatterns) {
					PatternVisualiserRenderer.getPatternRenderer().setDitherPattern(sgc, colsForPatterns[0]);
				} else {
					sgc.setBackground(cols[0]);
				}
				sgc.fillRectangle(x + 1, y + 10, width - 1, h);

				if (localUsePatterns) {
					PatternVisualiserRenderer.getPatternRenderer().setDitherPattern(sgc, colsForPatterns[1]);
				} else {
					sgc.setBackground(cols[1]);
				}
				sgc.fillRectangle(x + 1, y + 32, width / 2 - 1, h);

				if (localUsePatterns) {
					PatternVisualiserRenderer.getPatternRenderer().setDitherPattern(sgc, colsForPatterns[2]);
				} else {
					sgc.setBackground(cols[2]);
				}
				sgc.fillRectangle(x + 1 + width / 2, y + 32, width / 2 - 1, h);

				if (localUsePatterns) {
					PatternVisualiserRenderer.getPatternRenderer().setDitherPattern(sgc, colsForPatterns[3]);
				} else {
					sgc.setBackground(cols[3]);
				}
				int sy = y + 54;
				if (h > y + height - sy) {
					h = y + height - sy;
				}
				sgc.fillRectangle(x + 1, sy, width - 1, h);

				x += r.getSpacing() + width;
				r.paintColumnHeader(sgc, m2, x, width);
				r.paintColumn(sgc, m2, x, y, width, height, false);

				// need to indicate focus for accessibility
				if (isFocusControl()) {
					sgc.setForeground(ColorConstants.menuForeground);
					sgc.setBackground(ColorConstants.menuBackground);
					sgc.drawFocus(clientRect.x, clientRect.y, clientRect.width, clientRect.height);
				}

				gc.drawImage(buffer, 0, 0);
				sgc.dispose();
				buffer.dispose();
			}
		}

		private void disposeCols() {
			if (cols != null) {
				for (int i = 0; i < cols.length; i++) {
					cols[i].dispose();
				}
				cols = null;
			}
		}

		/**
		 * Dispose the resources allocated by the preview
		 */
		public void dispose() {
			super.dispose();
			disposeCols();
		}

		public boolean getLocalUsePatterns() {
			return localUsePatterns;
		}
	}

	/**
	 * Disposes the visualiser preview canvas
	 */
	public void dispose() {
		super.dispose();
		if (preview != null) {
			preview.dispose();
		}
	}

	/**
	 * Creates the widgets for the list of providers.
	 */
	private void createProvidersArea(Composite mainComposite) {

		Font mainFont = mainComposite.getFont();
		Composite providersComposite = new Composite(mainComposite, SWT.NONE);
		providersComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridLayout providersLayout = new GridLayout();
		providersLayout.marginWidth = 0;
		providersLayout.marginHeight = 0;
		providersComposite.setLayout(providersLayout);
		providersComposite.setFont(mainFont);

		Label providersLabel = new Label(providersComposite, SWT.NONE);
		providersLabel.setText(VisualiserMessages.VisualiserPreferencePage_providersLabel);
		providersLabel.setFont(mainFont);

		// Checkbox table viewer of decorators
		checkboxViewer = CheckboxTableViewer.newCheckList(providersComposite, SWT.SINGLE | SWT.TOP | SWT.BORDER);
		checkboxViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		checkboxViewer.getTable().setFont(providersComposite.getFont());
		checkboxViewer.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				return ((ProviderDefinition) element).getName();
			}
		});
		checkboxViewer.getTable().setFont(mainFont);

		checkboxViewer.setContentProvider(new IStructuredContentProvider() {

			public void dispose() {
				// Nothing to do on dispose
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

			public Object[] getElements(Object inputElement) {
				// Make an entry for each decorator definition
				// return sorter.sort((Object[]) inputElement);
				return (Object[]) inputElement;
			}

		});

		checkboxViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection sel = (IStructuredSelection) event.getSelection();
					ProviderDefinition definition = (ProviderDefinition) sel.getFirstElement();
					if (definition == null)
						clearDescription();
					else
						showDescription(definition);
				}
			}
		});

		checkboxViewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				checkboxViewer.setAllChecked(false);
				checkboxViewer.setChecked(event.getElement(), true);
				checkboxViewer.setSelection(new StructuredSelection(event.getElement()), true);

				// reset palette choice in drawing options tab
				ProviderDefinition definition = (ProviderDefinition) event.getElement();
				String pid = VisualiserPreferences.getPaletteIDForProvider(definition.getID());
				String pname = null;
				if ((pid != null) && (pid.length() > 0)) {
					pname = PaletteManager.getPaletteByID(pid).getName();
				}
				if ((pname == null) || (pname.length() == 0)) {
					pname = PaletteManager.getDefaultForProvider(definition).getName();
				}
				colourList.setSelection(new String[] { pname });
			}
		});
	}

	/**
	 * Creates the widgets for the provider description.
	 */
	private void createDescriptionArea(Composite mainComposite) {

		Font mainFont = mainComposite.getFont();
		Composite textComposite = new Composite(mainComposite, SWT.NONE);
		textComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout textLayout = new GridLayout();
		textLayout.marginWidth = 0;
		textLayout.marginHeight = 0;
		textComposite.setLayout(textLayout);
		textComposite.setFont(mainFont);

		Label descriptionLabel = new Label(textComposite, SWT.NONE);
		descriptionLabel.setText(VisualiserMessages.VisualiserPreferencePage_description);
		descriptionLabel.setFont(mainFont);

		descriptionText = new Text(textComposite, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY | SWT.BORDER | SWT.H_SCROLL);
		descriptionText.setLayoutData(new GridData(GridData.FILL_BOTH));
		descriptionText.setFont(mainFont);
	}

	/**
	 * Populates the list of providers.
	 */
	private void populateProviders() {
		ProviderDefinition[] definitions = getAllDefinitions();
		checkboxViewer.setInput(definitions);
		for (int i = 0; i < definitions.length; i++) {
			checkboxViewer.setChecked(definitions[i], definitions[i].isEnabled());
			if (definitions[i].isEnabled()) {
				showDescription(definitions[i]);
			}
		}
	}

	/**
	 * Show the selected description in the text.
	 */
	private void showDescription(ProviderDefinition definition) {
		if (descriptionText == null || descriptionText.isDisposed()) {
			return;
		}
		String text = definition.getDescription();
		if (text == null || text.length() == 0)
			descriptionText.setText(VisualiserMessages.VisualiserPreferencePage_noDescription);
		else
			descriptionText.setText(text);
	}

	/**
	 * Clear the selected description in the text.
	 */
	private void clearDescription() {
		if (descriptionText == null || descriptionText.isDisposed()) {
			return;
		}
		descriptionText.setText(""); //$NON-NLS-1$
	}

	/**
	 * Restore defaults
	 * 
	 * @see PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		super.performDefaults();
		stripeHeight.setSelection(VisualiserPreferences.getDefaultStripeHeight());
		prefWidth.setSelection(VisualiserPreferences.getDefaultBarWidth());
		String rname = RendererManager.getDefaultRenderer().getName();
		styleList.setSelection(styleList.indexOf(rname));
		ProviderDefinition def = (ProviderDefinition) checkboxViewer.getCheckedElements()[0];
		String pname = PaletteManager.getDefaultForProvider(def).getName();
		colourList.setSelection(colourList.indexOf(pname));
		preview.redraw();
	}

	/**
	 * Called when the user presses OK. Updates the Visualiser with the
	 * selections chosen.
	 * 
	 * @see PreferencePage#performOk()
	 */
	public boolean performOk() {
		if (super.performOk()) {
			ProviderDefinition[] definitions = ProviderManager.getAllProviderDefinitions();
			for (int i = 0; i < definitions.length; i++) {
				boolean checked = checkboxViewer.getChecked(definitions[i]);
				if (definitions[i].isEnabled() != checked) {
					definitions[i].setEnabled(checked);
				}
			}

			String rname = styleList.getSelection()[0];
			VisualiserPreferences.setRendererName(rname);

			String pname = colourList.getSelection()[0];
			ProviderDefinition def = ProviderManager.getCurrent();
			String defp = PaletteManager.getDefaultForProvider(def).getName();
			if (PaletteManager.getPaletteByName(pname).getPalette() instanceof PatternVisualiserPalette) {
				// Using Patterns
				if (stripeHeight.getSelection() < VisualiserPreferences.getDefaultPatternStripeHeight()
						&& !VisualiserPreferences.getUsePatterns()
						&& !VisualiserPreferences.getDontAutoIncreaseStripeHeight()) {
					if (VisualiserPreferences.getDoAutoIncreaseStripeHeight()) {
						VisualiserPreferences.setStripeHeight(VisualiserPreferences.getDefaultPatternStripeHeight());
					} else {
						MessageDialogWithToggle toggleDialog = new MessageDialogWithToggle(null,
								VisualiserMessages.VisualiserPreferencePage_stripeSizeDialog_title, null,
								VisualiserMessages.VisualiserPreferencePage_stripeSizeDialog_message,
								MessageDialog.QUESTION, new String[] { IDialogConstants.YES_LABEL,
										IDialogConstants.NO_LABEL }, 0,
								VisualiserMessages.VisualiserPreferencePage_stripeSizeDialog_togglemessage,
								VisualiserPreferences.getDoAutoIncreaseStripeHeight());
						if (toggleDialog.getReturnCode() == 0) { // Yes pressed
							VisualiserPreferences.setDoIncreaseStripeHeight(toggleDialog.getToggleState());
							VisualiserPreferences
									.setStripeHeight(VisualiserPreferences.getDefaultPatternStripeHeight());
						} else
							// No pressed
							VisualiserPreferences.setDontIncreaseStripeHeight(toggleDialog.getToggleState());
					}
				} else
					VisualiserPreferences.setStripeHeight(stripeHeight.getSelection());

				VisualiserPreferences.setBarWidth(prefWidth.getSelection());
				VisualiserPreferences.setUsePatterns(true);
				String pid = PaletteManager.getPaletteByName(pname).getID();
				VisualiserPreferences.setPaletteIDForProvider(def, pid);
			} else {
				// Not using patterns
				VisualiserPreferences.setStripeHeight(stripeHeight.getSelection());

				VisualiserPreferences.setBarWidth(prefWidth.getSelection());
				VisualiserPreferences.setUsePatterns(false);
				if (defp.equals(pname)) {
					// going with provider defintion, clear preference setting
					VisualiserPreferences.setPaletteIDForProvider(def, ""); //$NON-NLS-1$
				} else {
					// update preference setting for this provider
					String pid = PaletteManager.getPaletteByName(pname).getID();
					VisualiserPreferences.setPaletteIDForProvider(def, pid);
				}
			}
			PaletteManager.resetCurrent();

			IMarkupProvider markupP = ProviderManager.getMarkupProvider();
			if (markupP instanceof SimpleMarkupProvider) {
				((SimpleMarkupProvider) markupP).resetColours();
			}

			// if the Visualiser is showing, update to use the new settings
			if (VisualiserPlugin.visualiser != null) {
				if (VisualiserPlugin.menu != null) {
					VisualiserPlugin.menu.setVisMarkupProvider(markupP);
				}
				VisualiserPlugin.visualiser.updateDisplay(true);
			}
			return true;
		}
		return false;
	}

	/**
	 * @see IWorkbenchPreferencePage#init(IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	/**
	 * Get the Provider definitions for the workbench.
	 */
	private ProviderDefinition[] getAllDefinitions() {
		return ProviderManager.getAllProviderDefinitions();
	}
}