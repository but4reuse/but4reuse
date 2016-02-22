package org.but4reuse.artefactmodel.editor.actions;

import java.util.Iterator;

import org.but4reuse.artefactmodel.Artefact;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Activate / deactivate action
 * 
 * @author jabier.martinez
 */
public class SwitchActivateAction implements IObjectActionDelegate {

	ISelection selection;

	@Override
	public void run(IAction action) {
		if (selection instanceof IStructuredSelection) {
			Iterator<?> i = ((IStructuredSelection) selection).iterator();
			while (i.hasNext()) {
				Object o = i.next();
				if (o instanceof Artefact) {
					Artefact a = (Artefact) o;
					// Switch
					a.setActive(!a.isActive());
				}
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

}
