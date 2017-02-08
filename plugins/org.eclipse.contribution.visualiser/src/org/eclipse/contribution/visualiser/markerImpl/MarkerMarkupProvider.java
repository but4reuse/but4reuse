/**********************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: Sian January - initial version
 * ...
 **********************************************************************/

package org.eclipse.contribution.visualiser.markerImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.contribution.visualiser.core.Stripe;
import org.eclipse.contribution.visualiser.interfaces.IGroup;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.interfaces.IMember;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleMarkupKind;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleMarkupProvider;
import org.eclipse.contribution.visualiser.utils.JDTUtils;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.texteditor.AnnotationPreference;
import org.eclipse.ui.texteditor.AnnotationPreferenceLookup;
import org.eclipse.ui.texteditor.MarkerAnnotation;

/**
 * Markup provider that accompanies the ResourceContentProvider and shows any
 * markers attached to the files being displayed.
 */
public class MarkerMarkupProvider extends SimpleMarkupProvider {

	Map namesToKinds = new HashMap();
	List images = new ArrayList();

	/**
	 * Update the set of markups stored by this provider.
	 * 
	 * @param groups
	 */
	protected void updateMarkups(List groups) {
		namesToKinds = new HashMap();
		resetColours();
		resetMarkupsAndKinds();
		if (groups != null) {
			for (Iterator iter = groups.iterator(); iter.hasNext();) {
				IGroup group = (IGroup) iter.next();
				for (Iterator iter2 = group.getMembers().iterator(); iter2.hasNext();) {
					IMember member = (IMember) iter2.next();
					if (member instanceof ResourceMember) {
						IResource res = ((ResourceMember) member).getResource();
						try {
							IMarker[] markers = res.findMarkers(null, true, IResource.DEPTH_INFINITE);
							for (int i = 0; i < markers.length; i++) {
								IMarker marker = markers[i];
								Integer lineNum = (Integer) marker.getAttribute(IMarker.LINE_NUMBER);
								if (lineNum != null) {
									int lineNumber = lineNum.intValue();
									String name = getLabel(marker);
									if (name == null) {
										name = marker.getType();
									}
									IMarkupKind kind;
									if (namesToKinds.get(name) instanceof IMarkupKind) {
										kind = (IMarkupKind) namesToKinds.get(name);
									} else {
										Image image = getImage(marker);
										images.add(image);
										kind = new SimpleMarkupKind(name, image);
										namesToKinds.put(name, kind);
										addMarkupKind(kind);
										Color color = getColor(marker);
										if (color != null) {
											setColorFor(kind, color);
										}
									}
									boolean stripeOnLineAlready = false;
									List stripes = getMemberMarkups(member);
									if (stripes != null) {
										for (Iterator iter3 = stripes.iterator(); iter3.hasNext();) {
											Stripe stripe = (Stripe) iter3.next();
											if (stripe.getOffset() == lineNumber) {
												List kindList = Arrays.asList(new Object[] { kind });
												stripe.addKinds(kindList);
												stripeOnLineAlready = true;
											}
										}
									}
									if (!stripeOnLineAlready) {
										Stripe stripe = new StripeWithMarker(kind, lineNumber, marker);
										addMarkup(member.getFullname(), stripe);
									}
								}
							}
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * Get the Image for a marker, or null if none is defined
	 * 
	 * @param marker
	 * @return the Image found
	 */
	private Image getImage(IMarker marker) {
		IWorkbenchAdapter adapter = (IWorkbenchAdapter) marker.getAdapter(IWorkbenchAdapter.class);
		if (adapter != null) {
			ImageDescriptor descriptor = adapter.getImageDescriptor(marker);
			if (descriptor != null) {
				return descriptor.createImage();
			}
		}
		Annotation annotation = new MarkerAnnotation(marker);
		if (annotation != null) {
			AnnotationPreferenceLookup lookup = EditorsPlugin.getDefault().getAnnotationPreferenceLookup();
			AnnotationPreference preference = lookup.getAnnotationPreference(annotation);
			if (preference != null) {
				ImageDescriptor id = preference.getImageDescriptor();
				if (id != null) {
					return id.createImage();
				} else if (preference.getSymbolicImageName().equals("bookmark")) { //$NON-NLS-1$
					return PlatformUI.getWorkbench().getSharedImages()
							.getImageDescriptor(IDE.SharedImages.IMG_OBJS_BKMRK_TSK).createImage();
				}
			}
		}
		return null;
	}

	/**
	 * Get the label for a marker, or null if a label can not be found
	 * 
	 * @param marker
	 * @return the String found, or null if none is found
	 */
	private String getLabel(IMarker marker) {
		Annotation annotation = new MarkerAnnotation(marker);
		if (annotation != null) {
			AnnotationPreferenceLookup lookup = EditorsPlugin.getDefault().getAnnotationPreferenceLookup();
			AnnotationPreference preference = lookup.getAnnotationPreference(annotation);
			if (preference != null) {
				String id = preference.getPreferenceLabel();
				if (id != null && !(id.trim().equals(""))) { //$NON-NLS-1$
					return id;
				}
			}
		}
		return null;
	}

	/**
	 * Get the Color for a marker, or null if none is defined
	 * 
	 * @param marker
	 * @return the Color found, or null
	 */
	private Color getColor(IMarker marker) {
		Annotation annotation = new MarkerAnnotation(marker);
		if (annotation != null) {
			AnnotationPreferenceLookup lookup = EditorsPlugin.getDefault().getAnnotationPreferenceLookup();
			AnnotationPreference preference = lookup.getAnnotationPreference(annotation);
			if (preference != null) {
				RGB rgb = preference.getColorPreferenceValue();
				if (rgb != null) {
					return new Color(null, rgb);
				}
			}
		}
		return null;
	}

	/**
	 * Process a mouse click on a stripe. This implementation opens the editor
	 * at the location of the marker
	 * 
	 * @see org.eclipse.contribution.visualiser.interfaces.IMarkupProvider#processMouseclick(IMember,
	 *      Stripe, int)
	 */
	public boolean processMouseclick(IMember member, Stripe stripe, int buttonClicked) {
		if (stripe instanceof StripeWithMarker) {
			IMarker marker = ((StripeWithMarker) stripe).getMarker();
			if (marker != null) {
				JDTUtils.openInEditor(marker);
			}
		}

		return false;
	}

	/**
	 * Deactivate this provider - dispose of system resources
	 */
	public void deactivate() {
		super.deactivate();
		for (Iterator iter = images.iterator(); iter.hasNext();) {
			Image element = (Image) iter.next();
			if (element != null && !element.isDisposed()) {
				element.dispose();
			}
			iter.remove();
		}
		namesToKinds = new HashMap();
		resetMarkupsAndKinds();
	}

	public class StripeWithMarker extends Stripe {

		IMarker marker;

		/**
		 * Stripe constructor when the stripe is of the minimum depth (1).
		 * 
		 * @param k
		 *            The kind of stripe
		 * @param i
		 *            The offset down the bar where the stripe starts
		 */
		public StripeWithMarker(IMarkupKind k, int i, IMarker marker) {
			super(k, i);
			this.marker = marker;
		}

		public IMarker getMarker() {
			return marker;
		}

	}

}
