package org.but4reuse.visualisation.impl.visualiser.adaptedmodel;

import java.util.HashMap;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.markers.IMarkerElement;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.contribution.visualiser.core.Stripe;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.interfaces.IMember;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleMarkupProvider;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * Block Elements Markup Provider
 * 
 * @author jabier.martinez
 */
public class BlockElementsMarkupProvider extends SimpleMarkupProvider {

	private Map<Block, IMarkupKind> blocksAndKinds = new HashMap<Block, IMarkupKind>();

	public void reset() {
		// Remove previous
		resetMarkupsAndKinds();
		getBlocksAndNames().clear();
	}

	public void update(AdaptedModel adaptedModel) {
		// Update
		for (Block block : adaptedModel.getOwnedBlocks()) {
			BlockMarkupKind kind = new BlockMarkupKind(block.getName());
			kind.setBlock(block);
			blocksAndKinds.put(block, kind);
			addMarkupKind(kind);
		}
	}

	public Map<Block, IMarkupKind> getBlocksAndNames() {
		return blocksAndKinds;
	}

	public void setBlocksAndNames(Map<Block, IMarkupKind> blocksAndNames) {
		this.blocksAndKinds = blocksAndNames;
	}

	@Override
	public boolean processMouseclick(IMember member, Stripe stripe, int buttonClicked) {
		String message = stripe.getToolTip();
		MessageDialog.openInformation(Display.getCurrent().getActiveShell(), member.getName(), message);
		if (stripe instanceof ElementStripe) {
			IElement element = ((ElementStripe) stripe).getElement();
			if (element instanceof IMarkerElement) {
				IMarker marker = ((IMarkerElement) element).getMarker();
				if (marker != null) {
					WorkbenchUtils.openInEditor(marker);
					// leave no trace
					try {
						marker.delete();
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}
		}
		// return false for not to recompute
		return false;
	}

}
