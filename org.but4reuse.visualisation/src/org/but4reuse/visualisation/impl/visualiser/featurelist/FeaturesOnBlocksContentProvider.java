package org.but4reuse.visualisation.impl.visualiser.featurelist;

import java.util.List;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.visualisation.impl.visualiser.adaptedmodel.BlockElementsMarkupProvider;
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
public class FeaturesOnBlocksContentProvider extends SimpleContentProvider {

	public void reset() {
		if (this.getAllMembers() != null) {
			this.getAllMembers().clear();
		}
		if (this.getAllGroups() != null) {
			this.getAllGroups().clear();
		}
	}

	/**
	 * Add the blocks as members and add the stripes
	 * 
	 * @param adaptedModel
	 */
	public void update(FeatureList featureList, AdaptedModel adaptedModel) {

		// Update
		IGroup group = new SimpleGroup("Blocks");
		this.addGroup(group);
		// get the markup provider
		BlockElementsMarkupProvider markupProvider = (BlockElementsMarkupProvider) BlocksOnFeaturesVisualisation
				.getFeaturesOnBlocksProvider().getMarkupInstance();
		Map<Block, IMarkupKind> map = markupProvider.getBlocksAndNames();

		// Add blocks as members
		for (Feature feature : featureList.getOwnedFeatures()) {
			IMember member = new SimpleMember(feature.getName());
			// Add stripes
			int i = 0;
			for (Block block : adaptedModel.getOwnedBlocks()) {
				if (isBlockInFeature(block, feature)) {
					IMarkupKind blockKind = map.get(block);
					Stripe stripe = new Stripe(blockKind, i, block.getOwnedBlockElements().size());
					i = i + block.getOwnedBlockElements().size();
					markupProvider.addMarkup(member.getFullname(), stripe);
				}
			}
			group.add(member);
			member.setSize(i);
		}
		// This must be called to show the overlapping cases
		markupProvider.processMarkups();
	}

	public static boolean isBlockInFeature(Block block, Feature feature) {
		List<Artefact> artefacts = feature.getImplementedInArtefacts();
		List<BlockElement> blockElements = block.getOwnedBlockElements();
		for (BlockElement be : blockElements) {
			for (ElementWrapper ew : be.getElementWrappers()) {
				AdaptedArtefact aa = (AdaptedArtefact) ew.eContainer();
				for (Artefact a : artefacts) {
					if (aa.getArtefact().equals(a)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public ImageDescriptor getMemberViewIcon() {
		return FileUtils.getImageFromPlugin("org.but4reuse.featurelist.edit", "/icons/full/obj16/Feature.gif");
	}

	@Override
	public ImageDescriptor getGroupViewIcon() {
		return FileUtils.getImageFromPlugin("org.but4reuse.featurelist.edit", "/icons/full/obj16/FeatureList.gif");
	}

}
