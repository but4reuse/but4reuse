package org.but4reuse.utils.emf;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
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
			if (resource.getContents().isEmpty()) {
				return null;
			}
			return resource.getContents().get(0);
		} catch (Exception e) {
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
	 * 
	 * @param eResource
	 * @return the iResource
	 */
	public static IResource getIResource(Resource eResource) {
		if(eResource == null){
			return null;
		}
		org.eclipse.emf.common.util.URI eUri = eResource.getURI();
		if (eUri.isPlatformResource()) {
			String platformString = eUri.toPlatformString(true);
			return ResourcesPlugin.getWorkspace().getRoot().findMember(platformString);
		}
		return null;
	}

	/**
	 * Save resource
	 * 
	 * @param resource
	 */
	public static void saveResource(Resource resource) {
		Map<Object, Object> options = ((XMLResource) resource).getDefaultSaveOptions();
		options.put(XMLResource.OPTION_ENCODING, "UTF-8");
		options.put(XMLResource.OPTION_CONFIGURATION_CACHE, Boolean.TRUE);
		options.put(XMLResource.OPTION_USE_CACHED_LOOKUP_TABLE, new ArrayList<Object>());
		// Do not throw anything if dangling references
		// options.put(XMLResource.OPTION_PROCESS_DANGLING_HREF,
		// XMLResource.OPTION_PROCESS_DANGLING_HREF_DISCARD);
		try {
			resource.save(options);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Save resource ignoring dangling
	 * 
	 * @param resource
	 */
	public static void saveResourceIgnoringDangling(Resource resource) {
		Map<Object, Object> options = ((XMLResource) resource).getDefaultSaveOptions();
		options.put(XMLResource.OPTION_ENCODING, "UTF-8");
		options.put(XMLResource.OPTION_CONFIGURATION_CACHE, Boolean.TRUE);
		options.put(XMLResource.OPTION_USE_CACHED_LOOKUP_TABLE, new ArrayList<Object>());
		// Do not throw anything if dangling references
		options.put(XMLResource.OPTION_PROCESS_DANGLING_HREF, XMLResource.OPTION_PROCESS_DANGLING_HREF_DISCARD);
		try {
			resource.save(options);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void saveEObject(URI uri, EObject eObject) throws IOException {
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("uml", new XMIResourceFactoryImpl());

		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.createResource(uriToEMFURI(uri));
		resource.getContents().add(eObject);
		resource.save(Collections.EMPTY_MAP);
	}

	public static org.eclipse.emf.common.util.URI uriToEMFURI(URI uri) {
		return org.eclipse.emf.common.util.URI.createURI(uri.toString());
	}

	public static EFactory getEFactory(EObject eObject) {
		return getEPackage(eObject).getEFactoryInstance();
	}

	public static EPackage getEPackage(EObject eObject) {
		return eObject.eClass().getEPackage();
	}

	@SuppressWarnings("unchecked")
	/**
	 * Get the referenced EObjects. The precondition is that the reference belongs to this meta-class
	 * @param eObject
	 * @param reference
	 * @return A non null List
	 */
	public static List<EObject> getReferencedEObjects(EObject eObject, EReference reference) {
		List<EObject> refList = new ArrayList<EObject>();
		if (reference.isMany()) {
			Object o = eObject.eGet(reference);
			if (o != null && o instanceof List<?>) {
				refList = (List<EObject>) o;
			}
		} else {
			Object ob = eObject.eGet(reference);
			if (ob != null && ob instanceof EObject) {
				EObject o = (EObject) ob;
				refList.add(o);
			}
		}
		return refList;
	}
	
	/**
	 * Get model file extension
	 * TODO check the case where the eObject was not serialized yet
	 * @param eObject
	 * @return the extension or null
	 */
	public static String getModelExtension(EObject eObject){
		return FileUtils.getExtension(eObject.eResource().getURI().toString());
	}

}
