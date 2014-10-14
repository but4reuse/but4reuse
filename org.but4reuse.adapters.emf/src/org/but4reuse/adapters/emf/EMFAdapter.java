package org.but4reuse.adapters.emf;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;

/**
 * EMF Adapter
 * @author jabier.martinez
 */
public class EMFAdapter implements IAdapter {

	public static AdapterFactory ADAPTER_FACTORY = getAllRegisteredAdapterFactories();
	
	@Override
	public boolean isAdaptable(URI uri, IStatus status, IProgressMonitor monitor) {
		EObject eObject = getEObject(uri);
		if (eObject == null) {
			return false;
		}
		return true;
	}

	@Override
	public List<IElement> adapt(URI uri, IStatus status, IProgressMonitor monitor) {
		// TODO
		return new ArrayList<IElement>();
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IStatus status, IProgressMonitor monitor) {

	}
	
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
	
	public static ComposedAdapterFactory getAllRegisteredAdapterFactories() {
		final List<AdapterFactory> factories = new ArrayList<AdapterFactory>();
		factories.add(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return new ComposedAdapterFactory(factories);
	}
	
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

}
