/*******************************************************************************
 * Copyright (c) 2002 - 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Sian Whiting - initial version
 *     Andy Clement - refactored for stand-alone visualiser
 *******************************************************************************/
package org.eclipse.contribution.visualiser.views;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.contribution.visualiser.VisualiserPlugin;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.interfaces.IMarkupProvider;
import org.eclipse.contribution.visualiser.internal.help.IVisualiserHelpContextIds;
import org.eclipse.contribution.visualiser.internal.help.VisualiserHelp;
import org.eclipse.contribution.visualiser.internal.preference.VisualiserPreferences;
import org.eclipse.contribution.visualiser.renderers.PatternVisualiserRenderer;
import org.eclipse.contribution.visualiser.text.VisualiserMessages;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.UIJob;

/**
 * The Menu part of the Visualiser. Displays Markup kinds, colour selection
 * buttons and checkboxes.
 */
public class Menu extends ViewPart {

	private IAction selectNoneAction;
	private IAction selectAllAction;
	private Button[] buttons;
	private Button[] checkboxes;
	private Label[] labels;
	private Label[] icons;
	private Shell[] shells;
	private ColorDialog[] colorDialogs;
	private Image[] colorSquares;
	private Color[] colors;
	private SelectionListener selectionListener;
	private SelectionListener checkboxListener;
	private Composite canvas;
	private ScrolledComposite scrollpane;
	private GridLayout layout = new GridLayout(4, false);
	private static IMarkupProvider vmp;
	private static Hashtable kindActive = null;
	private boolean uptodate = false;
	private Map kinds;
	private static Job updateJob;

	/**
	 * The constructor.
	 */
	public Menu() {
		reset();
	}

	/**
	 * Set the current IMarkupProvider
	 * 
	 * @param vmp
	 */
	public void setVisMarkupProvider(IMarkupProvider vmp) {
		if (Menu.vmp != null) {
			kinds = new HashMap();
		}
		Menu.vmp = vmp;
	}

	/**
	 * Private function used to create square images on colour chooser buttons.
	 */
	private void drawImage(Image image, Color color) {
		GC gc = new GC(image);
		if (VisualiserPreferences.getUsePatterns()) {
			PatternVisualiserRenderer.getPatternRenderer().setDitherPattern(gc, color.getRGB());
		} else {
			gc.setBackground(color);
		}
		Rectangle bounds = image.getBounds();
		gc.fillRectangle(0, 0, bounds.width, bounds.height);
		gc.drawRectangle(0, 0, bounds.width - 1, bounds.height - 1);
		gc.dispose();
	}

	/**
	 * This is a callback that allows us to create the composite and initialize
	 * it. It also creates listeners for the colour buttons and the checkboxes.
	 */
	public void createPartControl(Composite parent) {
		reset();
		scrollpane = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		canvas = new Composite(scrollpane, SWT.NONE);
		scrollpane.setContent(canvas);
		canvas.setLayout(layout);

		/*
		 * Listener for colour buttons - if clicked produces a ColorDialog then
		 * redraws the square image with the chosen colour.
		 */
		selectionListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (e.getSource() instanceof Button) {
					Button button = (Button) e.getSource();
					int location = 0;
					for (int j = 0; j < buttons.length; j++) {
						if ((buttons[j]).equals(button)) {
							location = j;
						}
					}
					RGB rgb = colorDialogs[location].open();
					if (rgb == null) {
						return;
					}
					colors[location] = new Color(buttons[location].getDisplay(), rgb);
					Image image = buttons[location].getImage();
					drawImage(image, colors[location]);
					buttons[location].setImage(image);
					if (!(VisualiserPlugin.visualiser == null)) {
						vmp.setColorFor((IMarkupKind) labels[location].getData(), colors[location]);
						VisualiserPlugin.visualiser.draw();
					}
				}
			}
		};

		// Listener for checkboxes
		checkboxListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (!(VisualiserPlugin.visualiser == null)) {
					for (int i = 0; i < colors.length; i++) {
						kindActive.put(labels[i].getData(), new Boolean(checkboxes[i].getSelection()));
					}
					VisualiserPlugin.visualiser.updateDisplay(false);
				}
			}
		};
		makePullDownActions();
		contributeToActionBars();
		VisualiserPlugin.getDefault().setMenu(this);

		// Add an empty ISelectionProvider so that this view works with dynamic
		// help (bug 104331)
		getSite().setSelectionProvider(new ISelectionProvider() {
			public void addSelectionChangedListener(ISelectionChangedListener listener) {
			}

			public ISelection getSelection() {
				return null;
			}

			public void removeSelectionChangedListener(ISelectionChangedListener listener) {
			}

			public void setSelection(ISelection selection) {
			}
		});

	}

	/**
	 * Create the actions for the view's pull down menu.
	 */
	private void makePullDownActions() {
		selectAllAction = new Action() {
			public int getStyle() {
				return IAction.AS_PUSH_BUTTON;
			}

			public void run() {
				showAll();
			}
		};
		selectAllAction.setText(VisualiserMessages.Select_All_20);
		selectAllAction.setToolTipText(VisualiserMessages.Select_All_20);
		selectNoneAction = new Action() {
			public int getStyle() {
				return IAction.AS_PUSH_BUTTON;
			}

			public void run() {
				showNone();
			}
		};
		selectNoneAction.setText(VisualiserMessages.Select_None_21);
		selectNoneAction.setToolTipText(VisualiserMessages.Select_None_21);
	}

	/**
	 * Adds actions to the action bars.
	 */
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
	}

	/**
	 * Adds actions to local pull down menu.
	 */
	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(selectAllAction);
		manager.add(selectNoneAction);
	}

	/**
	 * Select all the checkboxes in the menu and update the visualiser
	 */
	private void showAll() {
		if (checkboxes != null) {
			for (int i = 0; i < checkboxes.length; i++) {
				checkboxes[i].setSelection(true);
				kindActive.put(labels[i].getData(), new Boolean(checkboxes[i].getSelection()));
			}
			VisualiserPlugin.visualiser.draw();
		}
	}

	/**
	 * Deselect all of the checkboxes in the menu and update the visualiser
	 */
	private void showNone() {
		if (checkboxes != null) {
			for (int i = 0; i < checkboxes.length; i++) {
				checkboxes[i].setSelection(false);
				kindActive.put(labels[i].getData(), new Boolean(false));
			}
			VisualiserPlugin.visualiser.draw();
		}
	}

	/**
	 * Select the checkboxes whose names are in the given List then update the
	 * visualiser
	 */
	protected void onlyShow(List names) {
		if (names == null) {
			showNone();
		} else if (names.size() == 0) {
			showNone();
		} else if (names.size() == checkboxes.length) {
			showAll();
		} else {
			for (int i = 0; i < labels.length; i++) {
				if (names.contains(labels[i].getText())) {
					checkboxes[i].setSelection(true);
					kindActive.put(labels[i].getData(), new Boolean(true));
				} else {
					checkboxes[i].setSelection(false);
					kindActive.put(labels[i].getData(), new Boolean(false));
				}
			}
			VisualiserPlugin.visualiser.draw();
		}
	}

	/**
	 * Get the active state of a kind (IE is the checkbox checked).
	 * 
	 * @param kind
	 *            - the kind
	 * @return true if the kind is active
	 */
	public boolean getActive(IMarkupKind kind) {
		if (kindActive == null)
			return true;
		if (kindActive.get(kind) == null)
			return true;
		return ((Boolean) kindActive.get(kind)).booleanValue();
	}

	/**
	 * Get the active state of a kind by name(IE is the checkbox checked).
	 * 
	 * @param kindName
	 *            - the kind name
	 * @return true if the kind with the given name is active
	 */
	public boolean getActive(String kindName) {
		if (kinds == null) {
			return true;
		}
		IMarkupKind kind = (IMarkupKind) kinds.get(kindName);
		if (kind == null) {
			return true;
		}
		if (kindActive.get(kind) == null) {
			return true;
		}
		return ((Boolean) kindActive.get(kind)).booleanValue();
	}

	/**
	 * Reset the up-to-date state of this view
	 */
	public void reset() {
		uptodate = false;
	}

	/**
	 * The main method - adds aspect names to the menu.
	 */
	public void ensureUptodate() {
		if (uptodate)
			return;
		// ClassCastException is thrown if provider has not returned a set
		// of IMarkupKinds from getAllMarkupKinds().
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (getSite().getPage().isPartVisible(VisualiserPlugin.menu)) {
					getUpdateJob().schedule();
				} else {
					update();
				}
			}
		});

	}

	private synchronized Job getUpdateJob() {
		if (updateJob == null) {
			updateJob = new UIJob(VisualiserMessages.Jobs_VisualiserMenuUpdate) {
				public IStatus runInUIThread(IProgressMonitor monitor) {
					if ((canvas == null) || canvas.isDisposed()) {
						return Status.OK_STATUS;
					}
					update();
					return Status.OK_STATUS;
				}
			};
		}
		return updateJob;
	}

	/**
	 * @param markupCategories
	 * @return the number of markup kinds that are active
	 */
	private int getNumberToShow(Set markupCategories) {
		int num = 0;
		if (markupCategories == null)
			return 0;
		for (Iterator iter = markupCategories.iterator(); iter.hasNext();) {
			IMarkupKind element = (IMarkupKind) iter.next();
			if (element.showInMenu()) {
				num++;
			}
		}
		return num;
	}

	/**
	 * Private method to clear the menu.
	 */
	private void clear() {
		if (canvas != null) {
			Control[] children = canvas.getChildren();
			if (children.length > 0) {
				for (int i = 0; i < children.length; i++) {
					children[i].dispose();
				}
			}
		}
	}

	/**
	 * Update the menu
	 */
	private void update() {
		clear();
		Set markupKinds = vmp.getAllMarkupKinds();
		int numKindsToShow = getNumberToShow(markupKinds);
		if (markupKinds == null)
			return;
		buttons = new Button[numKindsToShow];
		checkboxes = new Button[numKindsToShow];
		labels = new Label[numKindsToShow];
		icons = new Label[numKindsToShow];
		shells = new Shell[numKindsToShow];
		colorSquares = new Image[numKindsToShow];
		colorDialogs = new ColorDialog[numKindsToShow];
		colors = new Color[numKindsToShow];

		kindActive = new Hashtable();
		kinds = new HashMap();

		int i = 0;
		for (Iterator iter = markupKinds.iterator(); iter.hasNext();) {
			IMarkupKind element = (IMarkupKind) iter.next();
			kinds.put(element.getName(), element);
			if (element.showInMenu()) {
				int imageSize = 12;
				colors[i] = vmp.getColorFor(element);
				if (colors[i] == null) {
					throw new NullPointerException(VisualiserMessages.getColorForError);
				}
				if (!VisualiserPreferences.getUsePatterns()) {
					buttons[i] = new Button(canvas, SWT.PUSH);
					buttons[i].setToolTipText(VisualiserMessages.Change_color_for + " " + element.getName()); //$NON-NLS-1$		 		 		 
					shells[i] = buttons[i].getShell();
					colorDialogs[i] = new ColorDialog(shells[i]);
					Display display = shells[i].getDisplay();
					colorSquares[i] = new Image(display, imageSize, imageSize);
					buttons[i].setImage(colorSquares[i]);
					buttons[i].addSelectionListener(selectionListener);
					Image image = buttons[i].getImage();
					drawImage(image, colors[i]);
					buttons[i].setImage(image);
				} else {
					// We're using patterns so make a plain label rather than a
					// button
					Label l = new Label(canvas, SWT.NONE);
					colorSquares[i] = new Image(canvas.getDisplay(), imageSize, imageSize);
					l.setImage(colorSquares[i]);
					l.setToolTipText(VisualiserMessages.Pattern_for + " " + element.getName()); //$NON-NLS-1$
					Image image = l.getImage();
					drawImage(image, colors[i]);
					l.setImage(image);
				}
				checkboxes[i] = new Button(canvas, SWT.CHECK);
				checkboxes[i].addSelectionListener(checkboxListener);
				checkboxes[i].setSelection(true);

				icons[i] = new Label(canvas, SWT.NONE);
				icons[i].setImage(element.getIcon());
				labels[i] = new Label(canvas, SWT.NONE);
				labels[i].setText(element.getName());
				labels[i].setData(element);
				labels[i].setToolTipText(element.getFullName());
				i++;
			}
			kindActive.put(element, new Boolean(true));
		}
		canvas.layout();
		canvas.setSize(canvas.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));
		uptodate = true;
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		if (scrollpane != null) {
			scrollpane.setFocus();
		}
	}

	/**
	 * Dispose of the menu when closed.
	 */
	public void dispose() {
		canvas = null;
		updateJob = null;
		VisualiserPlugin.getDefault().removeMenu();
	}

	public Object getAdapter(Class key) {
		if (key.equals(IContextProvider.class)) {
			return VisualiserHelp.getHelpContextProvider(this, IVisualiserHelpContextIds.VISUALISER_MENU_VIEW);
		}
		return super.getAdapter(key);
	}

}
