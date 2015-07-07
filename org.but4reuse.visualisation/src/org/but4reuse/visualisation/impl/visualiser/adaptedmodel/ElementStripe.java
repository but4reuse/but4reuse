package org.but4reuse.visualisation.impl.visualiser.adaptedmodel;

import org.but4reuse.adapters.IElement;
import org.eclipse.contribution.visualiser.core.Stripe;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;

/**
 * Element Stripe
 * 
 * @author jabier.martinez
 */
public class ElementStripe extends Stripe {

	private IElement element;

	public ElementStripe(IMarkupKind blockKind, int i, int j) {
		super(blockKind, i, j);
	}

	@Override
	public String getToolTip() {
		return "Blocks: " + stringifyKinds() + "\n" + element.getText();
	}

	public IElement getElement() {
		return element;
	}

	public void setElement(IElement element) {
		this.element = element;
	}
}
