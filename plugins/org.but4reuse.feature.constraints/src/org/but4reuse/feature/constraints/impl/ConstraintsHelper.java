package org.but4reuse.feature.constraints.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.feature.constraints.BasicExcludesConstraint;
import org.but4reuse.feature.constraints.BasicRequiresConstraint;
import org.but4reuse.feature.constraints.Constraint;
import org.but4reuse.feature.constraints.FeatureConstraintPart;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.ImpliesConstraintPart;
import org.but4reuse.feature.constraints.NotConstraintPart;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;

/**
 * Constraints helper
 * 
 * @author jabier.martinez
 */
public class ConstraintsHelper {

	public static String getText(List<IConstraint> constraints) {
		StringBuilder text = new StringBuilder();
		for (IConstraint constraint : constraints) {
			text.append(constraint.getText() + "\n");
		}
		if (text.length() > 0) {
			text.setLength(text.length() - 1);
		}
		return text.toString();
	}

	public static String getTextWithExplanations(List<IConstraint> constraints) {
		StringBuilder text = new StringBuilder();
		for (IConstraint constraint : constraints) {
			text.append(getTextWithExplanations(constraint) + "\n\n");
		}
		if (text.length() > 0) {
			// remove last end of line
			text.setLength(text.length() - 1);
		}
		return text.toString();
	}

	public static String getTextWithExplanations(IConstraint constraint) {
		return constraint.getText() + " (" + constraint.getNumberOfReasons() + " reasons) "
				+ constraint.getExplanations();
	}

	/**
	 * Provisional method to perform the casting
	 * 
	 * @param adaptedModel
	 * @return
	 */
	public static List<IConstraint> getCalculatedConstraints(AdaptedModel adaptedModel) {
		List<IConstraint> constraints = new ArrayList<IConstraint>();
		Object o = adaptedModel.getConstraints();
		if (o != null && o instanceof List<?>) {
			List<?> list = (List<?>) o;
			for (Object e : list) {
				if (e instanceof IConstraint) {
					constraints.add((IConstraint) e);
				}
			}
		}
		return constraints;
	}

	/**
	 * Create matrix
	 * 
	 * @param featureList
	 * @param adaptedModel
	 * @return
	 */
	public static String[][] createMatrixOfPresenceOfBlocksInFeatures(FeatureList featureList, AdaptedModel adaptedModel) {
		String[][] matrix = null;
		int featuresSize = featureList.getOwnedFeatures().size() + 1;
		int blocksSize = adaptedModel.getOwnedBlocks().size() + 1;
		if (featuresSize > 1 && blocksSize > 1) {
			// initialize matrix sizes
			matrix = new String[featuresSize][];
			for (int i = 0; i < featuresSize; i++) {
				matrix[i] = new String[blocksSize];
			}
			// first row with block names
			for (int i = 1; i < blocksSize; i++) {
				matrix[0][i] = adaptedModel.getOwnedBlocks().get(i - 1).getName();
			}
			// first column with feature names
			for (int i = 1; i < featuresSize; i++) {
				matrix[i][0] = featureList.getOwnedFeatures().get(i - 1).getName();
			}
			// calculate feature-specific heuristic
			for (int r = 1; r < featuresSize; r++) {
				for (int c = 1; c < blocksSize; c++) {
					matrix[r][c] = new Double(percentageOfBlockInFeature(adaptedModel.getOwnedBlocks().get(c - 1),
							featureList.getOwnedFeatures().get(r - 1))).toString();
				}
			}
		}
		return matrix;
	}

	/**
	 * Percentage of one block appearing in a given feature
	 * 
	 * @param block
	 * @param feature
	 * @return
	 */
	public static double percentageOfBlockInFeature(Block block, Feature feature) {
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

	/**
	 * Calculate feature constraints based on block constraints
	 * 
	 * @param featureList
	 * @param adaptedModel
	 * @return a non null list of constraints
	 */
	public static List<IConstraint> getFeatureConstraints(FeatureList featureList, AdaptedModel adaptedModel) {
		List<IConstraint> featureConstraints = new ArrayList<IConstraint>();
		List<IConstraint> blockConstraints = ConstraintsHelper.getCalculatedConstraints(adaptedModel);
		// loop all the constraints
		Set<String> alreadyAdded = new HashSet<String>();
		for (IConstraint constraint : blockConstraints) {
			if (constraint instanceof BasicRequiresConstraint
					|| constraint instanceof BasicExcludesConstraint) {
				Block block1 = null;
				Block block2 = null;
				if(constraint instanceof BasicRequiresConstraint){
					block1 = ((BasicRequiresConstraint)constraint).getBlock1();
					block2 = ((BasicRequiresConstraint)constraint).getBlock2();
				} else {
					block1 = ((BasicExcludesConstraint)constraint).getBlock1();
					block2 = ((BasicExcludesConstraint)constraint).getBlock2();
				}
				List<Feature> block1Features = block1.getCorrespondingFeatures();
				List<Feature> block2Features = block2.getCorrespondingFeatures();
				// check if they share the same feature
				boolean sameFeatureFound = false;
				for (Feature f1 : block1Features) {
					for (Feature f2 : block2Features) {
						if (f1 == f2) {
							sameFeatureFound = true;
							break;
						}
					}
					if (sameFeatureFound) {
						break;
					}
				}
				// the blocks were in different features... constraint!
				if (!sameFeatureFound) {
					for (Feature f1 : block1Features) {
						for (Feature f2 : block2Features) {
							// constraint (for the moment only requires and excludes)
							Constraint cons = new Constraint();
							cons.addConstraintPart(new FeatureConstraintPart(f1));
							cons.addConstraintPart(new ImpliesConstraintPart());
							if(constraint instanceof BasicExcludesConstraint){
								cons.addConstraintPart(new NotConstraintPart());
							}
							cons.addConstraintPart(new FeatureConstraintPart(f2));
							
							// check if new
							String text = cons.getText();
							if (!alreadyAdded.contains(text)) {
								alreadyAdded.add(text);
								cons.addExplanation("Maybe more, but at least:" + constraint.getText());
								featureConstraints.add(cons);
							}
						}
					}
				}
			}

		}
		return featureConstraints;
	}

	/**
	 * TODO Move this to an appropriate helper. Here now to avoid dependency
	 * cycles
	 * 
	 * @param feature
	 * @return
	 */
	public static List<Block> getCorrespondingBlocks(AdaptedModel adaptedModel, Feature feature) {
		List<Block> correspondingBlocks = new ArrayList<Block>();
		for (Block block : adaptedModel.getOwnedBlocks()) {
			List<Feature> features = block.getCorrespondingFeatures();
			if (features != null && features.contains(feature) && !correspondingBlocks.contains(block)) {
				correspondingBlocks.add(block);
			}
		}
		return correspondingBlocks;
	}

	/**
	 * TODO another way for this
	 * 
	 * @param c1
	 * @param c2
	 * @return whether a constraint is equal to another
	 */
	public static boolean equalsConstraint(IConstraint c1, IConstraint c2) {
		// TODO check more complicated things like mutually exclude, or set of ands
		return c1.getText().equals(c2.getText());
	}

	/**
	 * TODO move Get the blocks that corresponds to a feature
	 * 
	 * @param feature
	 * @param adaptedModel
	 * @return a non null list of blocks
	 */
	public static List<Block> getBlocksOfFeature(Feature feature, AdaptedModel adaptedModel) {
		List<Block> blocks = new ArrayList<Block>();
		for (Block block : adaptedModel.getOwnedBlocks()) {
			if (block.getCorrespondingFeatures().contains(feature)) {
				blocks.add(block);
			}
		}
		return blocks;
	}
}
