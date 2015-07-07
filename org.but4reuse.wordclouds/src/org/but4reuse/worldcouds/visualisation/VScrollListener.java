package org.but4reuse.worldcouds.visualisation;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.ScrollBar;

public class VScrollListener implements SelectionListener {

	private Rectangle rect;

	private ScrolledComposite cmp;
	
	public VScrollListener(ScrolledComposite cmp,Rectangle rect) {
		// TODO Auto-generated constructor stub
		
		this.cmp = cmp;
		this.rect = rect;
		
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
	
		ScrollBar bar = (ScrollBar)(e.getSource());
		
		System.out.println("bar s : "+bar.getSelection());
		int hSelection = bar.getSelection ();
		
		int destY = hSelection - rect.y;
		cmp.setOrigin(0, rect.y+10);
	
		rect.y += 10 ; // -hSelection;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
