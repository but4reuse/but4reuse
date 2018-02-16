package org.but4reuse.adapters.helper;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.artefactmodel.ComposedArtefact;
import org.but4reuse.utils.emf.EMFUtils;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

/**
 * Adapters Helper Useful methods for Adapters and Elements
 * 
 * @author jabier.martinez
 */
public class AdaptersHelper {

	public static final String ADAPTERS_EXTENSIONPOINT = "org.but4reuse.adapters";

	private static List<IAdapter> cache_adapters;

	/**
	 * Get all adapters
	 * 
	 * @return list of declared adapters
	 */
	public static List<IAdapter> getAllAdapters() {
		if (cache_adapters != null) {
			return cache_adapters;
		}
		List<IAdapter> adapters = new ArrayList<IAdapter>();
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(ADAPTERS_EXTENSIONPOINT);
		for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
			try {
				adapters.add((IAdapter) adapterExtensionPoint.createExecutableExtension("class"));
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			} catch (Throwable t) {
				continue;
			}
		}
		cache_adapters = adapters;
		return adapters;
	}

	/**
	 * Get adapters from artefact model
	 * 
	 * @param artefactModel
	 * @param monitor
	 * @return
	 */
	public static List<IAdapter> getAdapters(ArtefactModel artefactModel, IProgressMonitor monitor) {
		// TODO for the moment we return the super set of adapters of all
		// artefacts but this can have problems. Now we assume that all share
		// the same adapters.
		List<IAdapter> filteredAdapters = new ArrayList<IAdapter>();
		for (Artefact artefact : artefactModel.getOwnedArtefacts()) {
			List<IAdapter> artefactAdapters = getAdapters(artefact, monitor);
			for (IAdapter artefactAdapter : artefactAdapters) {
				if (!filteredAdapters.contains(artefactAdapter)) {
					filteredAdapters.add(artefactAdapter);
				}
			}
			// monitor canceled
			if (monitor.isCanceled()) {
				return filteredAdapters;
			}
		}
		return filteredAdapters;
	}

	/**
	 * Get adapters
	 * 
	 * @param artefact
	 * @return
	 */
	public static List<IAdapter> getAdapters(Artefact artefact, IProgressMonitor monitor) {
		List<IAdapter> filteredAdapters = new ArrayList<IAdapter>();

		if (!(artefact instanceof ComposedArtefact)) {
			if (artefact.getArtefactURI() == null || artefact.getArtefactURI().length() == 0) {
				WorkbenchUtils.reportError(EMFUtils.getIResource(artefact.eResource()), 0, "URI is not defined");
				return filteredAdapters;
			}
			// Check if URI exists, we use emf utils
			org.eclipse.emf.common.util.URI emfuri = org.eclipse.emf.common.util.URI
					.createURI(artefact.getArtefactURI());
			ExtensibleURIConverterImpl conv = new ExtensibleURIConverterImpl();
			if (!(conv.exists(emfuri, null))) {
				// It does not exist report error
				WorkbenchUtils.reportError(EMFUtils.getIResource(artefact.eResource()), 0, "URI not found: " + emfuri);
				return filteredAdapters;
			}
		}

		// TODO clean this method
		List<IAdapter> adapters = getAllAdapters();
		for (IAdapter adapter : adapters) {
			if (!filteredAdapters.contains(adapter)) {
				try {
					if (artefact instanceof ComposedArtefact) {
						ComposedArtefact ca = (ComposedArtefact) artefact;
						for (Artefact ar : ca.getOwnedArtefacts()) {
							List<IAdapter> lap = getAdapters(ar, new NullProgressMonitor());
							for (IAdapter adapp : lap) {
								if (!filteredAdapters.contains(adapp)) {
									filteredAdapters.add(adapp);
								}
							}
						}
					} else {
						if (artefact.getArtefactURI() != null) {
							URI uri = new URI(artefact.getArtefactURI());
							String name = artefact.getName();
							if (name == null) {
								name = uri.toString();
							}
							monitor.subTask("Checking if " + name + " is adaptable with " + getAdapterName(adapter));
							if (adapter.isAdaptable(uri, null)) {
								filteredAdapters.add(adapter);
							}
							// user cancel
							if (monitor.isCanceled()) {
								return filteredAdapters;
							}
						}
					}
				} catch (URISyntaxException e) {
					e.printStackTrace();
					return filteredAdapters;
				}
			}
		}
		return filteredAdapters;
	}

	/**
	 * Get artefacts whose active property is set to true
	 * 
	 * @param artefactModel
	 * @return list of artefacts
	 */
	public static List<Artefact> getActiveArtefacts(ArtefactModel artefactModel) {
		List<Artefact> activeArtefacts = new ArrayList<Artefact>();
		for (Artefact artefact : artefactModel.getOwnedArtefacts()) {
			if (artefact.isActive()) {
				activeArtefacts.add(artefact);
			}
		}
		return activeArtefacts;
	}

	/**
	 * Get artefact model
	 * 
	 * @param artefact
	 * @return artefactModel
	 */
	public static ArtefactModel getArtefactModel(Artefact artefact) {
		EObject container = artefact.eContainer();
		while (container != null) {
			if (container instanceof ArtefactModel) {
				return (ArtefactModel) container;
			}
			container = container.eContainer();
		}
		return null;
	}

	/**
	 * Get Elements from a given artefact with a set of adapters
	 * 
	 * @param artefact
	 * @param adapters
	 * @return list of Elements
	 */
	public static List<IElement> getElements(Artefact artefact, List<IAdapter> adapters) {
		List<IElement> list = new ArrayList<IElement>();
		if (artefact.isActive()) {
			if (artefact instanceof ComposedArtefact) {
				ComposedArtefact cArtefact = (ComposedArtefact) artefact;
				for (Artefact a : cArtefact.getOwnedArtefacts()) {
					list.addAll(getElements(a, adapters));
				}
			} else {
				for (IAdapter ada : adapters) {
					list.addAll(getElements(artefact, ada));
				}
			}
		}
		return list;
	}

	/**
	 * Get Elements of a given artefact with a given adapter
	 * 
	 * @param artefact
	 * @return list of ielements
	 */
	public static List<IElement> getElements(Artefact artefact, IAdapter adapter) {
		List<IElement> elements = new ArrayList<IElement>();
		if (artefact instanceof ComposedArtefact) {
			ComposedArtefact cArtefact = (ComposedArtefact) artefact;
			for (Artefact a : cArtefact.getOwnedArtefacts()) {
				elements.addAll(getElements(a, adapter));
			}
		} else {
			URI uri = null;
			try {
				uri = new URI(artefact.getArtefactURI());
			} catch (Exception e) {
				e.printStackTrace();
				return elements;
			}
			elements = adapter.adapt(uri, new NullProgressMonitor());
		}
		return elements;
	}

	/**
	 * Get adapter id
	 * 
	 * @param adapter
	 * @return adapter id
	 */
	public static String getAdapterId(IAdapter adapter) {
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(ADAPTERS_EXTENSIONPOINT);
		try {
			for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
				IAdapter ada = null;
				ada = (IAdapter) adapterExtensionPoint.createExecutableExtension("class");
				if (ada.getClass().equals(adapter.getClass())) {
					String id = adapterExtensionPoint.getAttribute("id");
					if (id == null || id.length() > 0) {
						return id;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get adapter name
	 * 
	 * @param adapter
	 * @return adapter name
	 */
	public static String getAdapterName(IAdapter adapter) {
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(ADAPTERS_EXTENSIONPOINT);
		for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
			IAdapter ada = null;
			try {
				ada = (IAdapter) adapterExtensionPoint.createExecutableExtension("class");
				if (ada != null && ada.getClass().equals(adapter.getClass())) {
					String name = adapterExtensionPoint.getAttribute("name");
					if (name == null || name.length() > 0) {
						return name;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			} catch (Throwable t) {
				continue;
			}
		}
		return null;
	}

	/**
	 * Get adapter icon
	 * 
	 * @param adapter
	 * @return image descriptor
	 */
	public static ImageDescriptor getAdapterIcon(IAdapter adapter) {
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(ADAPTERS_EXTENSIONPOINT);
		for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
			IAdapter ada = null;
			try {
				ada = (IAdapter) adapterExtensionPoint.createExecutableExtension("class");
				if (ada != null && ada.getClass().equals(adapter.getClass())) {
					String path = adapterExtensionPoint.getAttribute("icon");
					// default if no icon defined
					if (path == null) {
						return PlatformUI.getWorkbench().getSharedImages()
								.getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT);
					}
					Bundle bundle = Platform.getBundle(adapterExtensionPoint.getContributor().getName());
					Path imageFilePath = new Path(path);
					URL imageFileUrl = FileLocator.find(bundle, imageFilePath, null);
					return ImageDescriptor.createFromURL(imageFileUrl);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			} catch (Throwable t) {
				continue;
			}
		}
		// return default icon but some error happened
		return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT);
	}

	/**
	 * Get associated adapter of a given element
	 * 
	 * @param element
	 * @return adapter or null if the element was not declared in any adapter
	 */
	public static IAdapter getAdapter(IElement element) {
		// loop through adapter extension points and check the declaration of
		// elements
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(ADAPTERS_EXTENSIONPOINT);
		for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
			IConfigurationElement[] a = adapterExtensionPoint.getChildren("elements");
			if (a != null && a.length > 0) {
				for (int ai = 0; ai < a.length; ai++) {
					IConfigurationElement cps = a[0];
					IConfigurationElement[] cps2 = cps.getChildren("element");
					if (cps2 != null && cps2.length > 0) {
						for (IConfigurationElement cpcon : cps2) {
							try {
								String className = cpcon.getAttribute("element");
								if (className != null) {
									// matching of element class names so return
									// the adapter
									if (className.equals(element.getClass().getName())) {
										return (IAdapter) adapterExtensionPoint.createExecutableExtension("class");
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
		return null;
	}

	/**
	 * Get the IElements of a given adapter
	 * 
	 * @param adapter
	 * @return list non-null
	 */
	public static List<String> getAdapterIElements(IAdapter adapter) {
		List<String> elements = new ArrayList<String>();
		try {
			IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry()
					.getConfigurationElementsFor(ADAPTERS_EXTENSIONPOINT);
			for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
				IAdapter ada = null;
				ada = (IAdapter) adapterExtensionPoint.createExecutableExtension("class");
				if (ada.getClass().equals(adapter.getClass())) {
					// I found the adapter, now get the elements
					IConfigurationElement[] a = adapterExtensionPoint.getChildren("elements");
					if (a != null && a.length > 0) {
						for (int ai = 0; ai < a.length; ai++) {
							IConfigurationElement cps = a[0];
							IConfigurationElement[] cps2 = cps.getChildren("element");
							if (cps2 != null && cps2.length > 0) {
								for (IConfigurationElement cpcon : cps2) {
									elements.add(cpcon.getAttribute("element"));
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return elements;
	}

	/**
	 * Get adapter by ids
	 * 
	 * @param adapter
	 *            ids as a comma separated list
	 * @return a non null list
	 */
	public static List<IAdapter> getAdaptersByIds(String adapters) {
		List<IAdapter> ada = new ArrayList<IAdapter>();
		if (adapters != null) {
			String[] ada_s = adapters.split(",");
			for (String s : ada_s) {
				IAdapter a = getAdapterById(s);
				if (a != null) {
					ada.add(a);
				}
			}
		}
		return ada;
	}

	/**
	 * Get adapter by Id
	 * 
	 * @param id
	 * @return the adapter or null
	 */
	public static IAdapter getAdapterById(String id) {
		for (IAdapter a : getAllAdapters()) {
			if (id.equals(getAdapterId(a))) {
				return a;
			}
		}
		return null;
	}
}
