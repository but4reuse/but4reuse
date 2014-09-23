package org.but4reuse.input.views;

import java.io.File;

import org.but4reuse.variantsmodel.Variant;
import org.but4reuse.variantsmodel.VariantsModel;
import org.but4reuse.variantsmodel.VariantsModelFactory;
import org.but4reuse.variantsmodel.VariantsModelPackage;
import org.but4reuse.variantsmodel.presentation.VariantsModelEditor;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEffect;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ResourceTransfer;
import org.eclipse.ui.part.ViewPart;
import org.but4reuse.utils.workbench.WorkbenchUtils;

/**
 * Input Drop View
 * 
 * @author jabier.martinez
 */
public class InputDropView extends ViewPart {

	public static final String EDITOR_ID = VariantsModelEditor.ID;

	public Canvas canvas;
	public boolean nativelyDoubleBufferedCanvas = false;
	public boolean dragOver = false;
	private static Image addImage;

	public InputDropView() {
		ImageDescriptor addImageDescriptor = ImageDescriptor.createFromFile(InputDropView.class, "/icons/add.png");
		addImage = addImageDescriptor.createImage();
	}

	@Override
	public void dispose() {
		canvas.dispose();
		super.dispose();
	}

	@Override
	public void createPartControl(Composite parent) {
		createCanvasAndPaintControl(parent);
		createDropFunctionality();
	}

	private void createDropFunctionality() {
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
		DropTarget target = new DropTarget(canvas, operations);
		final TextTransfer textTransfer = TextTransfer.getInstance();
		final FileTransfer fileTransfer = FileTransfer.getInstance();
		final ResourceTransfer resourceTransfer = ResourceTransfer.getInstance();
		Transfer[] types = new Transfer[] { fileTransfer, textTransfer, resourceTransfer };
		target.setTransfer(types);
		DropTargetEffect drop = new DropTargetEffect(canvas) {
			@Override
			public void drop(DropTargetEvent event) {
				// We add this for not to remove the file
				event.detail = DND.DROP_NONE;

				dragOver = false;
				canvas.redraw();

				// Compound command to be able to undo and redo for a set of
				// added artefacts
				CompoundCommand compoundCommand = new CompoundCommand();

				VariantsModelEditor editor = (VariantsModelEditor) WorkbenchUtils.getActiveEditorOfAGivenId(EDITOR_ID);
				if (editor == null) {
					MessageDialog
							.openInformation(getControl().getShell(), "Info",
									"A variants model editor must be opened to add your variants. Open or create one and try again.");
					return;
				}

				// Resource transfer
				if (event.data instanceof IResource[]) {
					IResource[] res = (IResource[]) event.data;
					for (int i = 0; i < res.length; i++) {
						String uriString = "platform:/resource/"
								+ URI.encodeSegment(res[i].getProject().getName(), false) + "/"
								+ res[i].getProjectRelativePath().toOSString().replace("\\", "/");
						Command command = addVariant(editor.getEditingDomain(), uriString);
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
							Command command = addVariant(editor.getEditingDomain(), uriString);
							if (command != null) {
								compoundCommand.append(command);
							}
						}
					}
				}
				if (compoundCommand.canExecute()) {
					editor.getEditingDomain().getCommandStack().execute(compoundCommand);
					editor.setFocus();
				}
				editor.setFocus();
			}

			// This methods are for the visual effect on mouse over
			@Override
			public void dragEnter(DropTargetEvent event) {
				dragOver = true;
				canvas.redraw();
			}

			@Override
			public void dragLeave(DropTargetEvent event) {
				dragOver = false;
				canvas.redraw();
			}
		};

		target.addDropListener(drop);
	}

	@Override
	public void setFocus() {
		// do nothing
	}

	private Command addVariant(EditingDomain editingDomain, String uriString) {
		Variant a = VariantsModelFactory.eINSTANCE.createVariant();
		a.setVariantURI(uriString);
		String name = uriString;
		if(name.endsWith("/")){
			name = name.substring(0,name.length()-1);
		}
		name = name.substring(name.lastIndexOf("/")+1, name.length());
		a.setName(name);
		XMIResource amr = (XMIResource) editingDomain.getResourceSet().getResources().get(0);
		VariantsModel am = (VariantsModel) amr.getContents().get(0);
		return AddCommand
				.create(editingDomain, am, VariantsModelPackage.eINSTANCE.getVariantsModel_OwnedVariants(), a);
	}

	/**
	 * Create canvas and how it is redrawn
	 * 
	 * @param parent_original
	 */
	private void createCanvasAndPaintControl(Composite parent_original) {
		canvas = createCanvas(parent_original, new PaintListener() {
			public void paintControl(PaintEvent e) {
				GC gc;
				Image bufferImage = null;
				if (!nativelyDoubleBufferedCanvas) {
					try {
						bufferImage = new Image(Display.getCurrent(), canvas.getBounds().width,
								canvas.getBounds().height);
						gc = new GC(bufferImage);
					} catch (SWTError noMoreHandlesError) {
						// No more handles error with big images
						bufferImage = null;
						gc = e.gc;
					}
				} else {
					gc = e.gc;
				}

				Rectangle clientArea = canvas.getClientArea();
				clientArea.width = clientArea.width - 20;
				clientArea.height = clientArea.height - 20;
				clientArea.x = clientArea.x + 10;
				clientArea.y = clientArea.y + 10;
				if (dragOver) {
					gc.setLineStyle(SWT.LINE_DASH);
					gc.setLineWidth(3);
				} else {
					gc.setLineStyle(SWT.LINE_DASH);
					gc.setLineWidth(2);
				}
				gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN));
				gc.drawRectangle(clientArea);

				gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
				String message = "Drop your artefacts here";
				int messageWidth = gc.textExtent(message).x;

				gc.drawImage(addImage, clientArea.width / 2 - 8, clientArea.height / 2 - 8);
				gc.drawText(message, clientArea.width / 2 - messageWidth / 2 + 8, clientArea.height / 2 + 20);

				if (!nativelyDoubleBufferedCanvas && bufferImage != null) {
					e.gc.drawImage(bufferImage, 0, 0);
					bufferImage.dispose();
				}
			}

		});
	}

	/**
	 * Create canvas
	 * 
	 * @param parent
	 * @param paintListener
	 * @return the canvas
	 */
	private Canvas createCanvas(Composite parent, PaintListener pl) {
		final Canvas c = new Canvas(parent, SWT.NO_BACKGROUND);
		c.setLayout(new FillLayout());
		nativelyDoubleBufferedCanvas = ((c.getStyle() & SWT.DOUBLE_BUFFERED) != 0);
		if (pl != null) {
			c.addPaintListener(pl);
		}
		return c;
	}

}
