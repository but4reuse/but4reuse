package org.but4reuse.visualisation.visualiser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adapters.IElement;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageLineColorsDialog;
import org.eclipse.contribution.visualiser.core.ProviderManager;
import org.eclipse.contribution.visualiser.interfaces.IGroup;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.interfaces.IMarkupProvider;
import org.eclipse.contribution.visualiser.interfaces.IMember;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleContentProvider;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleGroup;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleMember;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;

/**
 * Block content provider
 * 
 * @author jabier.martinez
 */
public class BlockElementsContentProvider extends SimpleContentProvider {

	public void reset() {
		if (this.getAllMembers() != null) {
			this.getAllMembers().clear();
		}
		if (this.getAllGroups() != null) {
			this.getAllGroups().clear();
		}
	}

	/**
	 * Add the artefacts as members and add the stripes of each of its elements
	 * based on the blocks (kinds)
	 * 
	 * @param adaptedModel
	 */
	public void update(AdaptedModel adaptedModel) {
		// Update
		IGroup group = new SimpleGroup("Artefacts");
		this.addGroup(group);
		// get the markup provider
		BlockElementsMarkupProvider markupProvider = (BlockElementsMarkupProvider) BlockElementsOnArtefactsVisualisation
				.getBlockElementsOnVariantsProvider().getMarkupInstance();
		Map<Block, IMarkupKind> map = markupProvider.getBlocksAndNames();

		for (AdaptedArtefact adaptedArtefact : adaptedModel.getOwnedAdaptedArtefacts()) {
			IMember member = new SimpleMember(adaptedArtefact.getArtefact().getName());
			member.setSize(adaptedArtefact.getOwnedElementWrappers().size());
			member.setTooltip("Elements: " + adaptedArtefact.getOwnedElementWrappers().size());

			int i = 0;
			for (ElementWrapper elementWrapper : adaptedArtefact.getOwnedElementWrappers()) {
				List<BlockElement> blockElements = elementWrapper.getBlockElements();
				if (!blockElements.isEmpty()) {
					// TODO get the one belonging to this adapted artefact, for
					// the moment it can only be one
					BlockElement blockElement = blockElements.get(0);
					Block block = (Block) blockElement.eContainer();
					IMarkupKind blockKind = map.get(block);
					ElementStripe stripe = new ElementStripe(blockKind, i, 1);
					stripe.setText(((IElement) (elementWrapper.getElement())).getText());
					markupProvider.addMarkup(member.getFullname(), stripe);
				}
				i++;
			}
			group.add(member);
		}
	}

	@Override
	public ImageDescriptor getMemberViewIcon() {
		return FileUtils.getImageFromPlugin("org.but4reuse.artefactmodel.edit", "/icons/full/obj16/Artefact.gif");
	}

	@Override
	public ImageDescriptor getGroupViewIcon() {
		return FileUtils.getImageFromPlugin("org.but4reuse.artefactmodel.edit", "/icons/full/obj16/ArtefactModel.gif");
	}

	@Override
	// TODO implement show selected variant and block elements relation
	public boolean processMouseclick(IMember member, boolean markupWasClicked, int buttonClicked) {
		if (!markupWasClicked) {
			// when names are empty and uri is used we get the last part
			String memberName = member.getName();
			if (memberName.contains("/")) {
				memberName = memberName.substring(memberName.lastIndexOf("/") + 1, memberName.length());
			}
			// TODO get the elements
			List<IElement> elements = new ArrayList<IElement>();
			String sText = "";
			for (IElement cp : elements) {
				sText = sText + cp.getText().replaceAll("\n", " ").replaceAll("\r", "") + "\n";
			}
			if (sText.length() > 0) {
				sText = sText.substring(0, sText.length() - 1);
			}

			// TODO get the line numbers for each of blocks
			List<List<Integer>> lines = new ArrayList<List<Integer>>();
			lines.add(new ArrayList<Integer>());

			List colors = new ArrayList();
			IMarkupProvider markupProvider = ProviderManager.getMarkupProvider();
			Set markups2 = markupProvider.getAllMarkupKinds();
			for (Object o : markups2) {
				IMarkupKind mk = (IMarkupKind) o;
				colors.add(markupProvider.getColorFor(mk));
			}

			ScrollableMessageLineColorsDialog dialog = new ScrollableMessageLineColorsDialog(Display.getCurrent()
					.getActiveShell(), memberName, elements.size() + " Elements", sText, lines, colors);
			dialog.open();
		}
		return true;
	}

}
