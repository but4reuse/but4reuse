package org.but4reuse.input.views;

import java.io.File;
import java.util.Date;

import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.artefactmodel.ArtefactModelFactory;
import org.but4reuse.artefactmodel.ArtefactModelPackage;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEffect;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Artefacts Drop Target Effect
 * @author jabier.martinez
 */
public class ArtefactsDropTargetEffect extends DropTargetEffect {
	
	public static final String EDITOR_ID = "org.but4reuse.artefactmodel.presentation.ArtefactModelEditorID";
	
	private boolean dragOver = false;
	
	public ArtefactsDropTargetEffect(Control control) {
		super(control);
	}

	@Override
	public void drop(DropTargetEvent event) {
		// We add this for not to remove the file
		event.detail = DND.DROP_NONE;

		setDragOver(false);
		getControl().redraw();

		// Compound command to be able to undo and redo for a set of
		// added artefacts
		CompoundCommand compoundCommand = new CompoundCommand();

		IEditingDomainProvider editor = (IEditingDomainProvider) WorkbenchUtils.getActiveEditorOfAGivenId(EDITOR_ID);
		if (editor == null) {
			MessageDialog
					.openInformation(getControl().getShell(), "Info",
							"An artefacts model editor must be opened to add your artefacts. Open or create one and try again.");
			return;
		}

		// Resource transfer
		if (event.data instanceof IResource[]) {
			IResource[] res = (IResource[]) event.data;
			for (int i = 0; i < res.length; i++) {
				String uriString = "platform:/resource/"
						+ URI.encodeSegment(res[i].getProject().getName(), false) + "/"
						+ res[i].getProjectRelativePath().toOSString().replace("\\", "/");
				Date creationDate = FileUtils.getCreationDate(WorkbenchUtils.getFileFromIResource(res[i]));
				Command command = addArtefact(editor.getEditingDomain(), uriString, creationDate);
				if (command != null) {
					compoundCommand.append(command);
				}
			}
		}
		// File transfer
		if (event.data instanceof String[]) {
			String[] da = (String[]) event.data;
			for (int i = 0; i < da.length; i++) {
				File file = new File(da[i]);
				if (file.exists()) {
					String uriString = file.toURI().toString();
					Date creationDate = FileUtils.getCreationDate(file);
					Command command = addArtefact(editor.getEditingDomain(), uriString, creationDate);
					if (command != null) {
						compoundCommand.append(command);
					}
				}
			}
		}
		if (compoundCommand.canExecute()) {
			editor.getEditingDomain().getCommandStack().execute(compoundCommand);
//			if (editor instanceof IWorkbenchPart){
//				((IWorkbenchPart)editor).setFocus();
//			}
		}
		if (editor instanceof IWorkbenchPart){
			((IWorkbenchPart)editor).setFocus();
		}
	}

	// This methods are for the visual effect on mouse over
	@Override
	public void dragEnter(DropTargetEvent event) {
		setDragOver(true);
		getControl().redraw();
	}

	@Override
	public void dragLeave(DropTargetEvent event) {
		setDragOver(false);
		getControl().redraw();
	}
	
	private Command addArtefact(EditingDomain editingDomain, String uriString, Date date) {
		Artefact a = ArtefactModelFactory.eINSTANCE.createArtefact();
		a.setArtefactURI(uriString);
		a.setDate(date);
		String name = uriString;
		if(name.endsWith("/")){
			name = name.substring(0,name.length()-1);
		}
		name = name.substring(name.lastIndexOf("/")+1, name.length());
		a.setName(name);
		XMIResource amr = (XMIResource) editingDomain.getResourceSet().getResources().get(0);
		ArtefactModel am = (ArtefactModel) amr.getContents().get(0);
		return AddCommand
				.create(editingDomain, am, ArtefactModelPackage.eINSTANCE.getArtefactModel_OwnedArtefacts(), a);
	}

	public boolean isDragOver() {
		return dragOver;
	}

	public void setDragOver(boolean dragOver) {
		this.dragOver = dragOver;
	}
}
