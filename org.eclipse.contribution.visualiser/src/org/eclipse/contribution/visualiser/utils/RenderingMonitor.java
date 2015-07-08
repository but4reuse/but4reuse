/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Matt Chapman - initial version
 *******************************************************************************/
package org.eclipse.contribution.visualiser.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

/**
 * Debug/profiling view, requires RenderingMonitorInfo aspect
 * 
 * @author matt
 */
public class RenderingMonitor extends ViewPart {

	private static RenderingMonitor instance;

	private Text fpsText;

	private Text msText;

	private Text ipText;

	private Text colsText;

	private Text provText;

	private Text geomText;

	private Text dsText;

	private static int runningTotal;

	private static int numValues;

	private static long lastTime;

	private static long provTime;

	private static long geomTime;

	private static int dataSize;

	private static int numCols;

	private static int imageKB;

	private static int widthHint = 80;

	public RenderingMonitor() {
		super();
		instance = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createPartControl(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayout(new GridLayout(3, true));
		group.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_FILL));
		group.setText("Visualiser rendering information"); //$NON-NLS-1$

		// row 1
		Label label = new Label(group, SWT.NONE);
		label.setText("Average framerate:"); //$NON-NLS-1$

		label = new Label(group, SWT.NONE);
		label.setText("Allocated Image mem:"); //$NON-NLS-1$

		label = new Label(group, SWT.NONE);
		label.setText("Time to get data:"); //$NON-NLS-1$

		// row 2
		fpsText = new Text(group, SWT.SINGLE);
		GridData gd = new GridData();
		gd.widthHint = widthHint;
		fpsText.setLayoutData(gd);
		if (numValues == 0) {
			fpsText.setText("0 fps"); //$NON-NLS-1$
		} else {
			setAverage((float) runningTotal / numValues);
		}

		ipText = new Text(group, SWT.SINGLE);
		gd = new GridData();
		gd.widthHint = widthHint;
		ipText.setLayoutData(gd);
		ipText.setText(imageKB + " KB"); //$NON-NLS-1$

		provText = new Text(group, SWT.SINGLE);
		gd = new GridData();
		gd.widthHint = widthHint;
		provText.setLayoutData(gd);
		provText.setText(provTime + " ms"); //$NON-NLS-1$

		// row 3
		label = new Label(group, SWT.NONE);
		label.setText("Time of last paint:"); //$NON-NLS-1$

		label = new Label(group, SWT.NONE);
		label.setText("Allocated Colors:"); //$NON-NLS-1$

		label = new Label(group, SWT.NONE);
		label.setText("Data size:"); //$NON-NLS-1$

		// row 4
		msText = new Text(group, SWT.SINGLE);
		gd = new GridData();
		gd.widthHint = widthHint;
		msText.setLayoutData(gd);
		msText.setText(lastTime + " ms"); //$NON-NLS-1$

		colsText = new Text(group, SWT.SINGLE);
		gd = new GridData();
		gd.widthHint = widthHint;
		colsText.setLayoutData(gd);
		colsText.setText("" + numCols); //$NON-NLS-1$

		dsText = new Text(group, SWT.SINGLE);
		gd = new GridData();
		gd.widthHint = widthHint;
		dsText.setLayoutData(gd);
		dsText.setText(dataSize + " bars"); //$NON-NLS-1$

		// row 5
		label = new Label(group, SWT.NONE);
		label.setText("Time of geometry calc:"); //$NON-NLS-1$

		label = new Label(group, SWT.NONE);

		label = new Label(group, SWT.NONE);

		// row 6
		geomText = new Text(group, SWT.SINGLE);
		gd = new GridData();
		gd.widthHint = widthHint;
		geomText.setLayoutData(gd);
		geomText.setText(geomTime + " ms"); //$NON-NLS-1$
	}

	private void setAverage(float av) {
		// convert to frames per second
		float fps = 1000 / av;
		// round to one decimal place
		float fpsi = Math.round(fps * 10) / 10f;
		fpsText.setText(fpsi + " fps"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		// fpsText.setFocus();
	}

	public static void logTime(long time) {
		lastTime = time;
		runningTotal += time;
		numValues++;

		if (instance != null) {
			if (instance.msText == null || instance.msText.isDisposed()) {
				instance = null;
				return;
			}
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					instance.setAverage((float) runningTotal / numValues);
					instance.msText.setText(lastTime + " ms"); //$NON-NLS-1$
				}
			});
		}
	}

	public static void logProvTime(long time) {
		provTime = time;
		if (instance != null) {
			if (instance.provText == null || instance.provText.isDisposed()) {
				instance = null;
				return;
			}
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					instance.provText.setText(provTime + " ms"); //$NON-NLS-1$
				}
			});
		}
	}

	public static void logGeomTime(long time) {
		geomTime = time;
		if (instance != null) {
			if (instance.geomText == null || instance.geomText.isDisposed()) {
				instance = null;
				return;
			}
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					instance.geomText.setText(geomTime + " ms"); //$NON-NLS-1$
				}
			});
		}
	}

	public static void logDataSize(int size) {
		dataSize = size;
		if (instance != null) {
			if (instance.dsText == null || instance.dsText.isDisposed()) {
				instance = null;
				return;
			}
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					instance.dsText.setText(dataSize + " bars"); //$NON-NLS-1$
				}
			});
		}
	}

	public static void logImagePixels(final long ip) {
		// convert num pixels to KB
		imageKB = (int) ((ip * Display.getDefault().getDepth()) / (8 * 1024));
		if (instance != null) {
			if (instance.ipText == null || instance.ipText.isDisposed()) {
				instance = null;
				return;
			}
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					instance.ipText.setText(imageKB + " KB"); //$NON-NLS-1$
				}
			});
		}
	}

	public static void logNumCols(int cols) {
		numCols = cols;
		if (instance != null) {
			if (instance.colsText == null || instance.colsText.isDisposed()) {
				instance = null;
				return;
			}
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					instance.colsText.setText("" + numCols); //$NON-NLS-1$
				}
			});
		}
	}

	public static void resetAverage() {
		numValues = 0;
		runningTotal = 0;
	}
}