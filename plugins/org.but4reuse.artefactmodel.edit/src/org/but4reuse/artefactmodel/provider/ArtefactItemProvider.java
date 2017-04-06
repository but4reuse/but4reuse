/**
 */
package org.but4reuse.artefactmodel.provider;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModelPackage;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

/**
 * This is the item provider adapter for a
 * {@link org.but4reuse.artefactmodel.Artefact} object. <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 
 * @generated
 */
public class ArtefactItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider,
		IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ArtefactItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addNamePropertyDescriptor(object);
			addArtefactURIPropertyDescriptor(object);
			addDescriptionPropertyDescriptor(object);
			addActivePropertyDescriptor(object);
			addDatePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Name feature. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Artefact_name_feature"),
				getString("_UI_PropertyDescriptor_description", "_UI_Artefact_name_feature", "_UI_Artefact_type"),
				ArtefactModelPackage.Literals.ARTEFACT__NAME, true, false, false,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Artefact URI feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addArtefactURIPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_Artefact_artefactURI_feature"),
						getString("_UI_PropertyDescriptor_description", "_UI_Artefact_artefactURI_feature",
								"_UI_Artefact_type"), ArtefactModelPackage.Literals.ARTEFACT__ARTEFACT_URI, true,
						false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Description feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addDescriptionPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_Artefact_description_feature"),
						getString("_UI_PropertyDescriptor_description", "_UI_Artefact_description_feature",
								"_UI_Artefact_type"), ArtefactModelPackage.Literals.ARTEFACT__DESCRIPTION, true, true,
						false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Active feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addActivePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Artefact_active_feature"),
				getString("_UI_PropertyDescriptor_description", "_UI_Artefact_active_feature", "_UI_Artefact_type"),
				ArtefactModelPackage.Literals.ARTEFACT__ACTIVE, true, false, false,
				ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Date feature. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addDatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Artefact_date_feature"),
				getString("_UI_PropertyDescriptor_description", "_UI_Artefact_date_feature", "_UI_Artefact_type"),
				ArtefactModelPackage.Literals.ARTEFACT__DATE, true, false, false,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This returns Artefact.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		// use the default image for this file type if found.
		// use a disabled (grey) version of the icon if disabled
		if (object instanceof Artefact) {
			Artefact artefact = (Artefact) object;
			String uriString = artefact.getArtefactURI();
			if (uriString != null) {
				try {
					URI uri = new URI(uriString);
					String path = uri.getPath();
					ImageDescriptor imageDescriptor = FileUtils.getIconFromFileName(path);
					if (imageDescriptor != null) {
						if (!artefact.isActive()) {
							Image image = imageDescriptor.createImage();
							return overlayImage(object, new Image(image.getDevice(), image, SWT.IMAGE_DISABLE));
						}
						return overlayImage(object, imageDescriptor.createImage());
					}
				} catch (URISyntaxException e) {
					return overlayImage(object, getResourceLocator().getImage("full/obj16/Artefact"));
				}
			}
			if (!artefact.isActive()) {
				return overlayImage(object, getResourceLocator().getImage("full/obj16/Artefact_disabled.png"));
			}
		}
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Artefact"));
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		// Show URI if name is empty
		Artefact artefact = ((Artefact) object);
		String label = artefact.getName();
		if (label != null && label.length() > 0) {
			return label;
		}
		label = artefact.getArtefactURI();
		if (label != null && label.length() > 0) {
			return label;
		}
		return getString("_UI_Artefact_type");
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to
	 * update any cached children and by creating a viewer notification, which
	 * it passes to {@link #fireNotifyChanged}. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(Artefact.class)) {
		case ArtefactModelPackage.ARTEFACT__NAME:
		case ArtefactModelPackage.ARTEFACT__ARTEFACT_URI:
		case ArtefactModelPackage.ARTEFACT__DESCRIPTION:
		case ArtefactModelPackage.ARTEFACT__ACTIVE:
		case ArtefactModelPackage.ARTEFACT__DATE:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
			return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s
	 * describing the children that can be created under this object. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

	/**
	 * Return the resource locator for this item provider's resources. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return ArtefactModelEditPlugin.INSTANCE;
	}

}
