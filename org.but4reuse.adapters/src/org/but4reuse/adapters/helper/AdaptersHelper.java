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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;


/**
 * Adapters Helper
 * Useful methods for Adapters and Elements
 * @author jabier.martinez
 */
public class AdaptersHelper {

	public static final String ADAPTERS_EXTENSIONPOINT = "org.but4reuse.adapters";

	/**
	 * 
	 * @return
	 */
	public static List<IAdapter> getAllAdapters() {
		List<IAdapter> adapters = new ArrayList<IAdapter>();
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry().getConfigurationElementsFor(
				ADAPTERS_EXTENSIONPOINT);
		for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
			try {
				adapters.add((IAdapter) adapterExtensionPoint.createExecutableExtension("class"));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return adapters;
	}

	/**
	 * 
	 * @param variantsModel
	 * @return
	 */
	public static List<IAdapter> getAdapters(ArtefactModel variantsModel) {
		List<IAdapter> filteredAdapters = new ArrayList<IAdapter>();
		for (Artefact artefact : variantsModel.getOwnedArtefacts()) {
			List<IAdapter> artefactAdapters = getAdapters(artefact);
			return artefactAdapters;
		}
		return filteredAdapters;
	}

	/**
	 * TODO clean this method
	 * 
	 * @param artefact
	 * @return
	 */
	public static List<IAdapter> getAdapters(Artefact artefact) {
		List<IAdapter> adapters = getAllAdapters();
		List<IAdapter> filteredAdapters = new ArrayList<IAdapter>();
		for (IAdapter adapter : adapters) {
			if (!filteredAdapters.contains(adapter)) {
				try {
					if (artefact instanceof ComposedArtefact) {
						ComposedArtefact ca = (ComposedArtefact) artefact;
						for (Artefact ar : ca.getOwnedArtefacts()) {
							List<IAdapter> lap = getAdapters(ar);
							for (IAdapter adapp : lap) {
								if (!filteredAdapters.contains(adapp)) {
									filteredAdapters.add(adapp);
								}
							}
						}
					} else {
						if (artefact.getArtefactURI() != null) {
							URI uri = new URI(artefact.getArtefactURI());
							if (adapter.isAdaptable(uri, null, null)) {
								filteredAdapters.add(adapter);
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
	

	public static List<Artefact> getActiveArtefacts(ArtefactModel artefactModel){
		List<Artefact> activeArtefacts = new ArrayList<Artefact>();
		for(Artefact artefact : artefactModel.getOwnedArtefacts()){
			if(artefact.isActive()){
				activeArtefacts .add(artefact);
			}
		}
		return activeArtefacts;
	}
	
	public static List<IElement> getElements(Artefact artefact,
			List<IAdapter> adapters) {
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
	 * 
	 * @param artefact
	 * @return
	 */
	public static List<IElement> getElements(Artefact artefact,
			IAdapter adapter) {
		List<IElement> elements = new ArrayList<IElement>();
		try {
			elements = adapter.adapt(new URI(artefact.getArtefactURI()), null, null);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return elements;
		}
		return elements;
	}

	public static String getAdapterName(IAdapter adapter) {
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry().getConfigurationElementsFor(
				ADAPTERS_EXTENSIONPOINT);
		for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
			IAdapter ada = null;
			try {
				ada = (IAdapter) adapterExtensionPoint.createExecutableExtension("class");
			} catch (CoreException e) {
				e.printStackTrace();
			}
			if (ada.getClass().equals(adapter.getClass())) {
				String name = adapterExtensionPoint.getAttribute("name");
				if (name == null || name.length() > 0) {
					return name;
				}
			}
		}
		return null;
	}

	public static ImageDescriptor getAdapterIcon(IAdapter adapter) {
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry().getConfigurationElementsFor(
				ADAPTERS_EXTENSIONPOINT);
		for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
			IAdapter ada = null;
			try {
				ada = (IAdapter) adapterExtensionPoint.createExecutableExtension("class");
			} catch (CoreException e) {
				e.printStackTrace();
			}
			if (ada.getClass().equals(adapter.getClass())) {
				String path = adapterExtensionPoint.getAttribute("icon");
				Bundle bundle = Platform.getBundle(adapterExtensionPoint.getContributor().getName());
				Path imageFilePath = new Path(path);
				URL imageFileUrl = FileLocator.find(bundle, imageFilePath, null);
				return ImageDescriptor.createFromURL(imageFileUrl);
			}
		}
		return null;
	}

	public static IAdapter getAdapter(IElement element) {
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry().getConfigurationElementsFor(
				ADAPTERS_EXTENSIONPOINT);
		for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
			IConfigurationElement[] a = adapterExtensionPoint.getChildren("elements");
			if (a != null && a.length > 0) {
				// only one
				IConfigurationElement cps = a[0];
				IConfigurationElement[] cps2 = cps.getChildren("element");
				if (cps2 != null && cps2.length > 0) {
					for (IConfigurationElement cpcon : cps2) {
						try {
							String className = cpcon.getAttribute("element");
							if (className.equals(element.getClass().getName())) {
								return (IAdapter) adapterExtensionPoint
										.createExecutableExtension("class");
							}
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}
}
