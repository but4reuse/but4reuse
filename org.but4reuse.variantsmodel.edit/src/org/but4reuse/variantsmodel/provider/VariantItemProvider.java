/**
 */
package org.but4reuse.variantsmodel.provider;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.variantsmodel.Variant;
import org.but4reuse.variantsmodel.VariantsModelPackage;
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
 * This is the item provider adapter for a {@link org.but4reuse.variantsmodel.Variant} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class VariantItemProvider
	extends ItemProviderAdapter
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VariantItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addNamePropertyDescriptor(object);
			addVariantURIPropertyDescriptor(object);
			addDescriptionPropertyDescriptor(object);
			addActivePropertyDescriptor(object);
			addFeaturesPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Variant_name_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Variant_name_feature", "_UI_Variant_type"),
				 VariantsModelPackage.Literals.VARIANT__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Variant URI feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVariantURIPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Variant_variantURI_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Variant_variantURI_feature", "_UI_Variant_type"),
				 VariantsModelPackage.Literals.VARIANT__VARIANT_URI,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Description feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDescriptionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Variant_description_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Variant_description_feature", "_UI_Variant_type"),
				 VariantsModelPackage.Literals.VARIANT__DESCRIPTION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Active feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addActivePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Variant_active_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Variant_active_feature", "_UI_Variant_type"),
				 VariantsModelPackage.Literals.VARIANT__ACTIVE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Features feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFeaturesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Variant_features_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Variant_features_feature", "_UI_Variant_type"),
				 VariantsModelPackage.Literals.VARIANT__FEATURES,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns Variant.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		// use the default image for this file type if found.
		// use a disabled (grey) version of the icon if disabled
		if (object instanceof Variant) {
			Variant variant = (Variant) object;
			String uriString = variant.getVariantURI();
			if (uriString != null) {
				try {
					URI uri = new URI(uriString);
					String path = uri.getPath();
					ImageDescriptor imageDescriptor = FileUtils.getIconFromFileName(path);
					if (imageDescriptor != null) {
						if (!variant.isActive()) {
							Image image = imageDescriptor.createImage();
							return overlayImage(object, new Image(image.getDevice(), image, SWT.IMAGE_DISABLE));
						}
						return overlayImage(object, imageDescriptor.createImage());
					}
				} catch (URISyntaxException e) {
					return overlayImage(object, getResourceLocator().getImage("full/obj16/Variant"));
				}
			}
			if (!variant.isActive()) {
				return overlayImage(object, getResourceLocator().getImage("full/obj16/Variant_disabled.png"));
			}
		}
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Variant"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		// Show URI if name is empty
		Variant variant = ((Variant) object);
		String label = variant.getName();
		if (label != null && label.length() > 0) {
			return label;
		}
		label = variant.getVariantURI();
		if (label != null && label.length() > 0) {
			return label;
		}
		return getString("_UI_Variant_type");
	}


	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(Variant.class)) {
			case VariantsModelPackage.VARIANT__NAME:
			case VariantsModelPackage.VARIANT__VARIANT_URI:
			case VariantsModelPackage.VARIANT__DESCRIPTION:
			case VariantsModelPackage.VARIANT__ACTIVE:
			case VariantsModelPackage.VARIANT__FEATURES:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return VariantsmodelEditPlugin.INSTANCE;
	}

}
