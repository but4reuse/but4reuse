package org.but4reuse.visualisation.visualiser;

import java.util.List;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.contribution.visualiser.VisualiserPlugin;
import org.eclipse.contribution.visualiser.core.Stripe;
import org.eclipse.contribution.visualiser.interfaces.IGroup;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.interfaces.IMarkupProvider;
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
public class BlockContentProvider extends SimpleContentProvider {

	public void reset(){
		if (this.getAllMembers() != null) {
		this.getAllMembers().clear();
	}
	if (this.getAllGroups() != null) {
		this.getAllGroups().clear();
	}
	}
	
	public void update(AdaptedModel adaptedModel) {
		// Update
		IGroup group = new SimpleGroup("Artefacts");
		this.addGroup(group);
		BlockMarkupProvider markupProvider = (BlockMarkupProvider) VisualiserPlugin.getProviderManager()
				.getMarkupProvider();

		for (AdaptedArtefact adaptedArtefact : adaptedModel.getOwnedAdaptedArtefacts()) {
			IMember member = new SimpleMember(adaptedArtefact.getArtefact().getName());
			member.setSize(adaptedArtefact.getOwnedElementWrappers().size());
			group.add(member);
			int i = 0;
			for (ElementWrapper elementWrapper : adaptedArtefact.getOwnedElementWrappers()) {
				List<BlockElement> blockElements = elementWrapper.getBlockElements();
				// TODO we get only one for the moment
				if (!blockElements.isEmpty()) {
					BlockElement blockElement = blockElements.get(0);
					Block block = (Block) blockElement.eContainer();
					Map<Block, IMarkupKind> map = markupProvider.getBlocksAndNames();
					IMarkupKind blockKind = map.get(block);
					Stripe stripe = new Stripe(blockKind, i, 1);
					markupProvider.addMarkup(member.getName(), stripe);
				}
				i++;
			}
		}
	}

	public ImageDescriptor getMemberViewIcon() {
		return FileUtils.getImageFromPlugin("org.but4reuse.artefactmodel.edit", "/icons/full/obj16/Artefact.gif");
	}

	public ImageDescriptor getGroupViewIcon() {
		return FileUtils.getImageFromPlugin("org.but4reuse.artefactmodel.edit", "/icons/full/obj16/ArtefactModel.gif");
	}
}
