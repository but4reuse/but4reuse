package org.but4reuse.visualisation.visualiser;

import org.eclipse.contribution.visualiser.core.Stripe;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;

// TODO add IMarker as in StripeWithMarker and handle it when clicking
public class ElementStripe extends Stripe {

	private String text;
	
	public ElementStripe(IMarkupKind blockKind, int i, int j) {
		super(blockKind,i,j);
	}

	@Override
	public String getToolTip() {
		return "Blocks: " + stringifyKinds() + "\n" + text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
