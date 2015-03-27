package org.but4reuse.feature.constraints.impl;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;

/**
 * Constraints helper
 * 
 * @author jabier.martinez
 */
public class ConstraintsHelper {

	public static String getText(List<IConstraint> constraints) {
		String text = "";
		for (IConstraint constraint : constraints) {
			text += constraint.getText() + "\n";
		}
		if (text.length() != 0) {
			// remove last end of line
			text = text.substring(0, text.length() - 1);
		}
		return text;
	}

	public static String getTextWithExplanations(List<IConstraint> constraints) {
		String text = "";
		for (IConstraint constraint : constraints) {
			text += getTextWithExplanations(constraint) + "\n\n";
		}
		if (text.length() != 0) {
			// remove last end of line
			text = text.substring(0, text.length() - 1);
		}
		return text;
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
	 * @param featureList
	 * @param adaptedModel
	 * @return
	 */
	public static String[][] createMatrixOfPresenceOfBlocksInFeatures(FeatureList featureList, AdaptedModel adaptedModel) {
		String[][] matrix = null;
		if (featureList != null) {
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
		}
		return matrix;
	}
	
	/**
	 * Percentage of one block appearing in a given feature
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
}
