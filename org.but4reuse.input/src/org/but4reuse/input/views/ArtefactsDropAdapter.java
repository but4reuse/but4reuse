package org.but4reuse.input.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DropTargetEvent;

/**
 * To be used in the EMF editor
 * 
 * @author jabier.martinez
 */
// TODO to drop elements from the file system (desktop) directly in the editor
public class ArtefactsDropAdapter extends EditingDomainViewerDropAdapter {

	public ArtefactsDropAdapter(EditingDomain domain, Viewer viewer) {
		super(domain, viewer);
	}

	public void dropAccept(DropTargetEvent event) {
		if (source == null) {
			source = getDragSource(event);
		}
		// check if files from workspace were dropped
		if (source != null) {
			List<IResource> iResources = new ArrayList<IResource>();
			for (Object o : source.toArray()) {
				if (o instanceof URI) {
					URI uri = (URI) o;
					if (uri.isPlatformResource()) {
						String platformString = uri.toPlatformString(true);
						IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(platformString);
						iResources.add(res);
					}
				}
			}
			if (!iResources.isEmpty()) {
				// this will modify event.detail for not to remove the files
				event.data = iResources.toArray(new IResource[iResources.size()]);
				ArtefactsDropTargetEffect effect = new ArtefactsDropTargetEffect(viewer.getControl());
				effect.drop(event);
			} else {
				super.dropAccept(event);
			}
		}
	}
}
