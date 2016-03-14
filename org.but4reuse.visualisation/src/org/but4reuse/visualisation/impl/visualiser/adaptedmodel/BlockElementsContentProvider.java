package org.but4reuse.visualisation.impl.visualiser.adaptedmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adapters.IElement;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.ui.dialogs.ScrollableMessageLineColorsDialog;
import org.eclipse.contribution.visualiser.core.ProviderDefinition;
import org.eclipse.contribution.visualiser.interfaces.IGroup;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.interfaces.IMember;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleContentProvider;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleGroup;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleMember;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
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
			// TODO Do not touch tooltip, unfortunately it is used by Visualiser
			// for the action when we right click an artefact
			// member.setTooltip(member.getFullname() + "\nElements: " +
			// adaptedArtefact.getOwnedElementWrappers().size());
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
					stripe.setElement((IElement) (elementWrapper.getElement()));
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
	// show selected variant and block elements relation
	public boolean processMouseclick(IMember member, boolean markupWasClicked, int buttonClicked) {
		if (!markupWasClicked) {
			// when names are empty and uri is used we get the last part
			String memberName = member.getName();
			if (memberName.contains("/")) {
				memberName = memberName.substring(memberName.lastIndexOf("/") + 1, memberName.length());
			}

			ProviderDefinition definition = BlockElementsOnArtefactsVisualisation.getBlockElementsOnVariantsProvider();
			BlockElementsMarkupProvider markupProvider = (BlockElementsMarkupProvider) definition.getMarkupInstance();

			// Get colors and initialize lines
			List<Color> colors = new ArrayList<Color>();
			List<List<Integer>> lines = new ArrayList<List<Integer>>();
			Map<Object, Integer> kindIndexMap = new HashMap<Object, Integer>();
			Object[] markups2 = markupProvider.getAllMarkupKinds().toArray();
			for (int i = 0; i < markups2.length; i++) {
				IMarkupKind mk = (IMarkupKind) markups2[i];
				kindIndexMap.put(mk, i);
				colors.add(markupProvider.getColorFor(mk));
				lines.add(new ArrayList<Integer>());
			}

			List<?> stripes = markupProvider.getMemberMarkups(member);
			StringBuffer sText = new StringBuffer("");
			for (int i = 0; i < stripes.size(); i++) {
				ElementStripe stripe = (ElementStripe) stripes.get(i);
				lines.get(kindIndexMap.get(stripe.getKinds().get(0))).add(i);
				String elementText = ((ElementStripe) stripe).getElement().getText().replaceAll("\n", " ").replaceAll("\t", " ").replaceAll("\r", " ");
				sText.append(elementText);
				sText.append("\n");
			}

			// Remove the last \n
			if (sText.length() > 0) {
				sText.setLength(sText.length() - 1);
			}

			ScrollableMessageLineColorsDialog dialog = new ScrollableMessageLineColorsDialog(Display.getCurrent()
					.getActiveShell(), memberName, stripes.size() + " Elements", sText.toString(), lines, colors);
			dialog.open();
		}
		return true;
	}

}
