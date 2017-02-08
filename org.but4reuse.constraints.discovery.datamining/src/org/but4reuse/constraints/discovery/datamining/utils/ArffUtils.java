package org.but4reuse.constraints.discovery.datamining.utils;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;

/**
 * Arff utils
 * 
 * @author jabier.martinez
 * 
 * arff example
 * \@relation ArtefactInstances
 * \@attribute 'Block 0' {0,1}
 * \@attribute 'Block 1' {0,1}
 * \@data
 * 1,0
 * 1,1
 */
public class ArffUtils {

	/**
	 * The instances (rows) are the adapted artefacts and the attributes are the
	 * blocks
	 * 
	 * @param adaptedModel
	 * @return
	 */
	public static String createArffFileContent(AdaptedModel adaptedModel, boolean ignoreCommonBlocks) {

		StringBuilder sb = new StringBuilder();
		String relationName = AdaptedModelHelper.getName(adaptedModel);
		if (relationName == null || relationName.length() == 0) {
			relationName = "ArtefactInstances";
		} else {
			relationName = relationName.replaceAll(" ", "");
		}
		sb.append("@relation ");
		sb.append(relationName);
		sb.append("\n\n");
		
		// ignore common
		List<Block> commonBlocks = new ArrayList<Block>();
		if (ignoreCommonBlocks) {
			commonBlocks = AdaptedModelHelper.getCommonBlocks(adaptedModel);
		}

		// declare attributes (the blocks)
		for (Block block : adaptedModel.getOwnedBlocks()) {
			// ignore common
			if (!commonBlocks.contains(block)) {
				sb.append("@attribute '");
				sb.append(block.getName());
				sb.append("' {0,1}\n");
			}
		}
		
		sb.append("\n@data\n");

		// feed the instances (the artefacts)
		for (AdaptedArtefact adaptedArtefact : adaptedModel.getOwnedAdaptedArtefacts()) {
			// Create the instance
			List<Block> blocks = AdaptedModelHelper.getBlocksOfAdaptedArtefact(adaptedArtefact);
			// ignore common
			blocks.removeAll(commonBlocks);
			for (Block block : adaptedModel.getOwnedBlocks()) {
				// ignore common
				if (!commonBlocks.contains(block)) {
					if (blocks.contains(block)) {
						sb.append("1,");
					} else {
						sb.append("0,");
					}
				}
			}
			// remove last comma
			sb.setLength(sb.length() - 1);
			sb.append("\n");
		}
		
		return sb.toString();
	}

}
