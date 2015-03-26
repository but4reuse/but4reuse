package org.but4reuse.constraints.discovery.weka.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 * Weka utils
 * 
 * @author jabier.martinez
 */
public class WekaUtils {

	/**
	 * The instances (rows) are the adapted artefacts and the attributes are the
	 * blocks
	 * 
	 * @param adaptedModel
	 * @return
	 */
	public static Instances createInstances(AdaptedModel adaptedModel) {
		// declare attributes (the blocks)
		FastVector attributes = new FastVector(adaptedModel.getOwnedBlocks().size());
		Map<Block, Attribute> map = new HashMap<Block, Attribute>();
		FastVector values = new FastVector(2);
        values.addElement("1");
        values.addElement("0");
		for (Block block : adaptedModel.getOwnedBlocks()) {
			Attribute a = new Attribute(block.getName(), values);
			attributes.addElement(a);
			map.put(block, a);
		}
		// feed the instances (the artefacts)
		String relationName = AdaptedModelHelper.getName(adaptedModel);
		if (relationName == null || relationName.length() == 0) {
			relationName = "ArtefactInstances";
		} else {
			relationName = relationName.replaceAll(" ", "");
		}
		Instances instances = new Instances(relationName, attributes, adaptedModel.getOwnedAdaptedArtefacts().size());
		for (AdaptedArtefact adaptedArtefact : adaptedModel.getOwnedAdaptedArtefacts()) {
			// Create the instance
			List<Block> blocks = AdaptedModelHelper.getBlocksOfAdaptedArtefact(adaptedArtefact);
			Instance instance = new Instance(adaptedModel.getOwnedBlocks().size());
			for (Block block : adaptedModel.getOwnedBlocks()) {
				if (blocks.contains(block)) {
					instance.setValue(map.get(block), "1");
				} else {
					instance.setValue(map.get(block), "0");
				}
			}
			instances.add(instance);
		}
		return instances;
	}

	/**
	 * Save arff file
	 * @param file
	 * @param instances
	 * @throws IOException
	 */
	public static void save(File file, Instances instances) throws IOException {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(instances);
		saver.setFile(file);
		saver.writeBatch();
	}

}
