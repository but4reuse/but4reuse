package org.but4reuse.visualisation.visualiser;

import java.util.HashMap;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.eclipse.contribution.visualiser.core.Stripe;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.interfaces.IMember;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleMarkupProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * Block Elements Markup Provider
 * @author jabier.martinez
 */
public class BlockElementsMarkupProvider extends SimpleMarkupProvider {

	private Map<Block, IMarkupKind> blocksAndNames = new HashMap<Block, IMarkupKind>();

	public void reset() {
		// Remove previous
		resetMarkupsAndKinds();
		getBlocksAndNames().clear();
	}

	public void update(AdaptedModel adaptedModel) {
		// Update
		int i = 0;
		for (Block block : adaptedModel.getOwnedBlocks()) {
			String blockName = "Block " + getNumberWithZeros(i, adaptedModel.getOwnedBlocks().size() - 1);
			BlockMarkupKind kind = new BlockMarkupKind(blockName);
			kind.setBlock(block);
			blocksAndNames.put(block, kind);
			addMarkupKind(kind);
			i++;
		}
	}

	public static String getNumberWithZeros(int number, int maxNumber) {
		String _return = String.valueOf(number);
		for (int zeros = _return.length(); zeros < String.valueOf(maxNumber).length(); zeros++) {
			_return = "0" + _return;
		}
		return _return;
	}

	public Map<Block, IMarkupKind> getBlocksAndNames() {
		return blocksAndNames;
	}

	public void setBlocksAndNames(Map<Block, IMarkupKind> blocksAndNames) {
		this.blocksAndNames = blocksAndNames;
	}

	@Override
	public boolean processMouseclick(IMember member, Stripe stripe, int buttonClicked) {
		String message = stripe.getToolTip();
		MessageDialog.openInformation(Display.getCurrent().getActiveShell(), member.getName(), message);
		// return false for not to recompute
		return false;
	}

	@Override
	public void resetColours() {
		super.resetColours();
		if (this.getAllMarkupKinds() != null && !this.getAllMarkupKinds().isEmpty()) {
			// Block 0 is usually the common, force it to be white
			IMarkupKind firstKind = (IMarkupKind) this.getAllMarkupKinds().first();
			setColorFor(firstKind, new Color(Display.getCurrent(), 255, 255, 255));
		}
	}

}
