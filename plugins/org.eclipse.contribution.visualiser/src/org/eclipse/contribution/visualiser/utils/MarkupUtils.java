/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Andy Clement - initial version
 *******************************************************************************/
package org.eclipse.contribution.visualiser.utils;

import java.util.List;

import org.eclipse.contribution.visualiser.VisualiserPlugin;
import org.eclipse.contribution.visualiser.core.Stripe;

/**
 * Utility class containing methods for processing Stripes
 */
public class MarkupUtils {

	/**
	 * Processes a list of stripes to deal with the overlapping cases
	 * 
	 * @param stripes
	 */
	public static void processStripes(List stripes) {
		if (stripes == null)
			return;

		final boolean debug = false;
		// We are looking for markups that 'clash' and we'll break them into
		// smaller pieces.

		int splits = 1;
		int loop = 0;
		while (splits > 0) {
			if (debug)
				VisualiserPlugin.log(2, "Stripe processing, iteration: " + (loop + 1)); //$NON-NLS-1$
			splits = 0;
			for (int j = 0; j < stripes.size(); j++) {
				for (int i = 0; i < stripes.size(); i++) {
					if (i != j) {
						Stripe stripe1 = (Stripe) stripes.get(i);
						Stripe stripe2 = (Stripe) stripes.get(j);
						if (debug)
							VisualiserPlugin.log(2, "Processing " + stripe1 + " and " + stripe2); //$NON-NLS-1$ //$NON-NLS-2$
						if (stripe2.getOffset() < stripe1.getOffset()
								|| (stripe2.getOffset() == stripe1.getOffset() && stripe2.getDepth() < stripe1
										.getDepth())) {
							// Changeround
							if (debug)
								VisualiserPlugin.log(2, "Stripe change round"); //$NON-NLS-1$
							stripe2 = (Stripe) stripes.get(i);
							stripe1 = (Stripe) stripes.get(j);
						}
						int stripe1start = stripe1.getOffset();
						int stripe2start = stripe2.getOffset();
						int dep1 = stripe1.getDepth();
						int dep2 = stripe2.getDepth();
						int stripe1end = stripe1start + dep1;
						int stripe2end = stripe2start + dep2;

						// What cases are there...
						if (stripe1end > stripe2start && stripe2end < stripe1end) {
							if (debug) {

								VisualiserPlugin.log(2, "Stripe split - type 1: stripe2 is entirely within stripe1"); //$NON-NLS-1$
								VisualiserPlugin.log(2, "Stripe1: " + stripe1); //$NON-NLS-1$
								VisualiserPlugin.log(2, "Stripe2: " + stripe2); //$NON-NLS-1$
								VisualiserPlugin.log(2, ""); //$NON-NLS-1$
							}
							// Stripe1 finishes after Stripe2 starts
							// Shorten stripe1 and stripe2 and build a stripe3
							// between them !
							Stripe stripe3 = new Stripe();
							stripe3.addKinds(stripe1.getKinds());
							stripe3.addKinds(stripe2.getKinds());
							stripe3.setOffset(stripe2start);
							stripe3.setDepth(dep2);
							stripes.add(i + 1, stripe3);
							stripe1.setDepth(stripe2start - stripe1start);
							stripe2.setOffset(stripe2start + dep2);
							stripe2.setDepth(dep1 - dep2 - (stripe2start - stripe1start));
							stripe2.setKinds(stripe1.getKinds());
							if (debug) {
								VisualiserPlugin.log(2, "Stripe1: " + stripe1); //$NON-NLS-1$
								VisualiserPlugin.log(2, "Stripe3: " + stripe3); //$NON-NLS-1$
								VisualiserPlugin.log(2, "Stripe2: " + stripe2); //$NON-NLS-1$
							}
							splits++;

						} else if (stripe1end > stripe2start) {
							// two cases, normal and the case when stripe1end ==
							// stripe2end !

							if (stripe1end != stripe2end) {
								if (stripe1start != stripe2start) {
									if (debug) {

										VisualiserPlugin.log(2,
												"Stripe split - type 2: stripe1 finishes after stripe2 starts"); //$NON-NLS-1$
										VisualiserPlugin.log(2, "Stripe1: " + stripe1); //$NON-NLS-1$
										VisualiserPlugin.log(2, "Stripe2: " + stripe2); //$NON-NLS-1$

										VisualiserPlugin.log(2, ""); //$NON-NLS-1$
									}
									// Stripe1 finishes after Stripe2 starts
									// Shorten stripe1 and stripe2 and build a
									// stripe3 between them !
									Stripe stripe3 = new Stripe();
									stripe3.addKinds(stripe1.getKinds());
									stripe3.addKinds(stripe2.getKinds());
									stripe3.setOffset(stripe2start);
									stripe3.setDepth((stripe1start + dep1) - stripe2start);
									stripes.add(i + 1, stripe3);
									stripe1.setDepth(stripe2start - stripe1start);

									stripe2.setOffset(stripe1start + dep1);
									stripe2.setDepth(dep2 - ((stripe1start + dep1) - stripe2start));
									if (debug) {

										VisualiserPlugin.log(2, "Stripe1: " + stripe1); //$NON-NLS-1$
										VisualiserPlugin.log(2, "Stripe3: " + stripe3); //$NON-NLS-1$
										VisualiserPlugin.log(2, "Stripe2: " + stripe2); //$NON-NLS-1$
									}
								} else {
									if (debug) {

										VisualiserPlugin.log(2,
												"Stripe split - type 4: stripe1 and stripe2 start at the same point"); //$NON-NLS-1$
										VisualiserPlugin.log(2, "Stripe1: " + stripe1); //$NON-NLS-1$
										VisualiserPlugin.log(2, "Stripe2: " + stripe2); //$NON-NLS-1$

										VisualiserPlugin.log(2, ""); //$NON-NLS-1$
									}
									if (stripe1end == stripe2end) {
										stripe2.addKinds(stripe1.getKinds());
										stripes.remove(i);
									} else if (stripe1end > stripe2end) {
										stripe2.addKinds(stripe1.getKinds());
										stripe1.setOffset(stripe2end);
										stripe1.setDepth(dep1 - dep2);
									} else {
										stripe2.setOffset(stripe1end);
										stripe2.setDepth(dep2 - dep1);
										stripe1.addKinds(stripe2.getKinds());
									}
									VisualiserPlugin.log(2, "NewStripe1: " + stripe1); //$NON-NLS-1$
									VisualiserPlugin.log(2, "NewStripe2: " + stripe2); //$NON-NLS-1$
								}
							} else {
								if (debug) {

									VisualiserPlugin
											.log(2,
													"Stripe split - type 3: stripe2 starts half way down stripe 1 - they both finish at the same point"); //$NON-NLS-1$
									VisualiserPlugin.log(2, "Stripe1: " + stripe1); //$NON-NLS-1$
									VisualiserPlugin.log(2, "Stripe2: " + stripe2); //$NON-NLS-1$

									VisualiserPlugin.log(2, ""); //$NON-NLS-1$
								}

								// Stripe1 finishes after Stripe2 starts
								stripe2.addKinds(stripe1.getKinds());
								stripe2.setDepth((stripe1start + dep1) - stripe2start);
								stripe1.setDepth(stripe2start - stripe1start);
								if (debug) {

									VisualiserPlugin.log(2, "Stripe1: " + stripe1); //$NON-NLS-1$
									VisualiserPlugin.log(2, "Stripe2: " + stripe2); //$NON-NLS-1$
								}
							}
							splits++;
						}

					}
				}
			}
			if (debug)
				VisualiserPlugin.log(2, "Splits on this iteration: " + splits); //$NON-NLS-1$
		}
	}
}
