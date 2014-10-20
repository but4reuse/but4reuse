package org.but4reuse.visualisation.visualiser.featurelist;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.but4reuse.visualisation.visualiser.adaptedmodel.BlockElementsMarkupProvider;
import org.eclipse.contribution.visualiser.core.Stripe;
import org.eclipse.contribution.visualiser.interfaces.IGroup;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.interfaces.IMember;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleContentProvider;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleGroup;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleMember;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;

/**
 * Block content provider
 * 
 * @author jabier.martinez
 */
public class FeaturesOnBlocksContentProvider extends SimpleContentProvider {

	Map<String, String> messagePerMember = new HashMap<String, String>();
	
	public void reset() {
		if (this.getAllMembers() != null) {
			this.getAllMembers().clear();
		}
		if (this.getAllGroups() != null) {
			this.getAllGroups().clear();
		}
		messagePerMember.clear();
	}


	/**
	 * Add the blocks as members and add the stripes
	 * 
	 * @param adaptedModel
	 */
	public void update(FeatureList featureList, AdaptedModel adaptedModel) {
		// Update
		IGroup group = new SimpleGroup("Artefacts");
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
			String memberMessage = "Blocks, and percentage of the existance of the block in the artefacts that implemented this feature\n";
			for (Block block : adaptedModel.getOwnedBlocks()) {
				double percentage = percentageOfBlockInFeature(block, feature);
				if (percentage > 0) {
					List<Feature> featuresOfThisBlock = getFeaturesOfThisBlock(featureList, block);
					String messageExtra = "";
					for(Feature f:featuresOfThisBlock){
						if(!f.equals(feature)){
							messageExtra = messageExtra  + f.getName() + ", ";
						}
					}
					if(messageExtra.length()>0){
						messageExtra = " also found in " + messageExtra.substring(0,messageExtra.length()-2);
					}
					memberMessage = memberMessage + block.getName() + " -> " + new Double(percentage * 100).intValue() + "% " + messageExtra + "\n";
					IMarkupKind blockKind = map.get(block);
					Stripe stripe = new Stripe(blockKind, i, block.getOwnedBlockElements().size());
					i = i + block.getOwnedBlockElements().size();
					markupProvider.addMarkup(member.getFullname(), stripe);
				}
			}
			messagePerMember.put(member.getFullname(), memberMessage);
			group.add(member);
			member.setSize(i);
		}
		// This must be called to show the overlapping cases
		markupProvider.processMarkups();
	}

	private List<Feature> getFeaturesOfThisBlock(FeatureList featureList, Block block) {
		List<Feature> fs = new ArrayList<Feature>();
		for(Feature f : featureList.getOwnedFeatures()){
			if(percentageOfBlockInFeature(block, f)>0){
				fs.add(f);
			}
		}
		return fs;
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
	
	@Override
	// show selected variant and block elements relation
	public boolean processMouseclick(IMember member, boolean markupWasClicked, int buttonClicked) {
		if (!markupWasClicked) {
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), member.getFullname(), messagePerMember.get(member.getFullname()));
		}
		return true;
	}

}
