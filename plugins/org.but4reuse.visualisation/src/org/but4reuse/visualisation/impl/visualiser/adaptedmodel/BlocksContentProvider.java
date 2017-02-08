package org.but4reuse.visualisation.impl.visualiser.adaptedmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.contribution.visualiser.core.Stripe;
import org.eclipse.contribution.visualiser.interfaces.IGroup;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.interfaces.IMember;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleContentProvider;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleGroup;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleMember;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Block content provider
 * 
 * @author jabier.martinez
 */
public class BlocksContentProvider extends SimpleContentProvider {

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
		BlockElementsMarkupProvider markupProvider = (BlockElementsMarkupProvider) BlocksOnArtefactsVisualisation
				.getBlocksOnArtefactsProvider().getMarkupInstance();
		Map<Block, IMarkupKind> map = markupProvider.getBlocksAndNames();

		for (AdaptedArtefact adaptedArtefact : adaptedModel.getOwnedAdaptedArtefacts()) {
			IMember member = new SimpleMember(adaptedArtefact.getArtefact().getName());
			member.setSize(adaptedArtefact.getOwnedElementWrappers().size());
			// TODO Do not touch tooltip, unfortunately it is used by Visualiser
			// for the action when we right click an artefact
			// member.setTooltip(member.getFullname() + "\nElements: " +
			// adaptedArtefact.getOwnedElementWrappers().size());

			List<Block> visitedBlocks = new ArrayList<Block>();
			for (ElementWrapper elementWrapper : adaptedArtefact.getOwnedElementWrappers()) {
				List<BlockElement> blockElements = elementWrapper.getBlockElements();
				if (!blockElements.isEmpty()) {
					// TODO get the one belonging to this adapted artefact, for
					// the moment it can only be one
					BlockElement blockElement = blockElements.get(0);
					Block block = (Block) blockElement.eContainer();
					if (!visitedBlocks.contains(block)) {
						visitedBlocks.add(block);
					}
				}
			}

			int i = 0;
			for (Block block : adaptedModel.getOwnedBlocks()) {
				if (visitedBlocks.contains(block)) {
					int blockSize = block.getOwnedBlockElements().size();
					IMarkupKind blockKind = map.get(block);
					Stripe stripe = new Stripe(blockKind, i, blockSize);
					i = i + blockSize;
					markupProvider.addMarkup(member.getFullname(), stripe);
				}
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
		return new BlockElementsContentProvider().processMouseclick(member, markupWasClicked, buttonClicked);
	}

}
