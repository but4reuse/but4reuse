package org.but4reuse.input.views;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

/**
 * Input Drop View
 * 
 * @author jabier.martinez
 */
public class InputDropView extends ViewPart {

	public Canvas canvas;
	public boolean nativelyDoubleBufferedCanvas = false;
	private static Image addImage;
	private ArtefactsDropTarget dropTarget;

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
		dropTarget = new ArtefactsDropTarget(canvas);
	}

	@Override
	public void setFocus() {
		// do nothing
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
				if (dropTarget.isDragOver()) {
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
