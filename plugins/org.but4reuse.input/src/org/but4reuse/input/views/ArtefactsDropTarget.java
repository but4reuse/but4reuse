package org.but4reuse.input.views;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.ResourceTransfer;

/**
 * Artefacts Drop Target
 * 
 * @author jabier.martinez
 */
public class ArtefactsDropTarget {

	static int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
	ArtefactsDropTargetEffect dropTargetEffect;

	public ArtefactsDropTarget(Control control) {
		DropTarget dropTarget = new DropTarget(control, operations);
		final TextTransfer textTransfer = TextTransfer.getInstance();
		final FileTransfer fileTransfer = FileTransfer.getInstance();
		final ResourceTransfer resourceTransfer = ResourceTransfer.getInstance();
		Transfer[] types = new Transfer[] { fileTransfer, textTransfer, resourceTransfer };
		dropTarget.setTransfer(types);
		dropTargetEffect = new ArtefactsDropTargetEffect(control);
		dropTarget.addDropListener(dropTargetEffect);
	}

	public boolean isDragOver() {
		return dropTargetEffect.isDragOver();
	}

}
