package org.but4reuse.visualisation.impl.visualiser.featurelist;

import java.util.ArrayList;
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
public class BlocksOnFeaturesContentProvider extends SimpleContentProvider {

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
		IGroup group = new SimpleGroup("Features");
		this.addGroup(group);
		// get the markup provider
		FeaturesMarkupProvider markupProvider = (FeaturesMarkupProvider) FeaturesOnBlocksVisualisation
				.getFeaturesOnBlocksProvider().getMarkupInstance();
		Map<Feature, IMarkupKind> map = markupProvider.getFeaturesAndNames();

		// Add blocks as members
		for (Block block : adaptedModel.getOwnedBlocks()) {
			IMember member = new SimpleMember(block.getName());
			member.setSize(block.getOwnedBlockElements().size());
			// TODO Do not touch tooltip, unfortunately it is used by Visualiser
			// for the action when we right click an artefact
			// member.setTooltip(member.getFullname() + "\nElements: " +
			// block.getOwnedBlockElements().size());

			// Add stripes
			for (Feature feature : featureList.getOwnedFeatures()) {

				if (percentageOfBlockInFeature(block, feature) > 0) {
					IMarkupKind featureKind = map.get(feature);
					Stripe stripe = new Stripe(featureKind, 0, member.getSize());
					markupProvider.addMarkup(member.getFullname(), stripe);
				}
			}

			group.add(member);
		}
		// This must be called to show the overlapping cases
		markupProvider.processMarkups();
	}

	private double percentageOfBlockInFeature(Block block, Feature feature) {
		List<Artefact> artefacts = feature.getImplementedInArtefacts();
		List<Artefact> foundArtefacts = new ArrayList<Artefact>();
		List<BlockElement> blockElements = block.getOwnedBlockElements();
		for (BlockElement be : blockElements) {
			for (ElementWrapper ew : be.getElementWrappers()) {
				AdaptedArtefact aa = (AdaptedArtefact) ew.eContainer();
				for (Artefact a : artefacts) {
					if (aa.getArtefact().equals(a)) {
						if (!foundArtefacts.contains(a)) {
							foundArtefacts.add(a);
						}
					}
				}
			}
		}
		return new Double(foundArtefacts.size()) / new Double(artefacts.size());
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
