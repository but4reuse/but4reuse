package org.but4reuse.utils.emf;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.swt.graphics.Image;

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
	 * Get a human readable name for an EObject
	 * 
	 * @param eObject
	 * @return a name
	 */
	public static String getText(EObject eObject) {
		EcoreItemProviderAdapterFactory factory = new EcoreItemProviderAdapterFactory();
		AdapterFactoryLabelProvider aflp = new AdapterFactoryLabelProvider(factory);
		return aflp.getText(eObject);
	}
	
	/**
	 * Get the icon of an EObject
	 * 
	 * @param eObject
	 * @return an image
	 */
	public static Image getImage(EObject eObject) {
		EcoreItemProviderAdapterFactory factory = new EcoreItemProviderAdapterFactory();
		AdapterFactoryLabelProvider aflp = new AdapterFactoryLabelProvider(factory);
		return aflp.getImage(eObject);
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
	
	/**
	 * Save resource
	 * @param resource
	 */
	public static void saveResource(Resource resource) {
		Map<Object, Object> saveOptions = ((XMLResource) resource).getDefaultSaveOptions();
		saveOptions.put(XMLResource.OPTION_CONFIGURATION_CACHE, Boolean.TRUE);
		saveOptions.put(XMLResource.OPTION_USE_CACHED_LOOKUP_TABLE, new ArrayList<Object>());
		try {
			resource.save(saveOptions);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
