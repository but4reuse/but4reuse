package org.but4reuse.utils.emf;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;

/**
 * EMF Utils
 * 
 * @author jabier.martinez
 */
public class EMFUtils {

	/**
	 * Get EObject from URI
	 * 
	 * @param uri
	 * @return the EObject behind this resource uri
	 */
	public static EObject getEObject(URI uri) {
		try {
			AdapterFactoryEditingDomain editingDomain = new AdapterFactoryEditingDomain(new ComposedAdapterFactory(
					ComposedAdapterFactory.Descriptor.Registry.INSTANCE), new BasicCommandStack());
			Resource resource = editingDomain.createResource(uri.toString());
			resource.load(null);
			return resource.getContents().get(0);
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Get the emf composedAdapterFactory
	 * 
	 * @return the emf composedAdapterFactory
	 */
	public static ComposedAdapterFactory getAllRegisteredAdapterFactories() {
		final List<AdapterFactory> factories = new ArrayList<AdapterFactory>();
		factories.add(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return new ComposedAdapterFactory(factories);
	}

	/**
	 * Try to find a human redable name for an EObject
	 * 
	 * @param eObject
	 * @return a name
	 */
	public static String getName(EObject eObject) {
		// TODO get item provider and then getText
		String name = eObject.eClass().getName();
		for (EAttribute attr : eObject.eClass().getEAllAttributes()) {
			if (attr.getName().equalsIgnoreCase("name")) {
				Object o = eObject.eGet(attr);
				if (o != null) {
					name = name + "_" + eObject.eGet(attr);
					break;
				}
			}
			if (attr.getName().equalsIgnoreCase("title")) {
				Object o = eObject.eGet(attr);
				if (o != null) {
					name = name + "_" + eObject.eGet(attr);
					break;
				}
			}
		}
		return name;
	}

	/**
	 * Get an IResource from an EMF resource
	 * @param eResource
	 * @return the iResource
	 */
	public static IResource getIResource(Resource eResource) {
		org.eclipse.emf.common.util.URI eUri = eResource.getURI();
		if (eUri.isPlatformResource()) {
			String platformString = eUri.toPlatformString(true);
			return ResourcesPlugin.getWorkspace().getRoot().findMember(platformString);
		}
		return null;
	}

}
