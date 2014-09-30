package org.but4reuse.input.views;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DropTargetEvent;

// TODO to drop elements directly in the editor
public class ArtefactsDropAdapter extends EditingDomainViewerDropAdapter {

	public ArtefactsDropAdapter(EditingDomain domain, Viewer viewer) {
		super(domain, viewer);
	}

	public void dropAccept(DropTargetEvent event) {
		super.dropAccept(event);
		// do not remove files!
		//if (FileTransfer.getInstance().isSupportedType(event.currentDataType)){
		//	event.detail = DND.DROP_NONE;
		//}
	}

}
