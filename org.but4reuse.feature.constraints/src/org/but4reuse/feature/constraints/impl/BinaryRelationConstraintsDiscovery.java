package org.but4reuse.feature.constraints.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adapters.IElement;
import org.but4reuse.feature.constraints.IConstraintsDiscovery;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Binary relations constraints discovery
 * TODO improve this constraints discovery!
 * @author jabier.martinez
 *
 */
public class BinaryRelationConstraintsDiscovery implements IConstraintsDiscovery{

	@Override
	public String discover(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor) {
		
		monitor.beginTask("Binary Relation Constraints discovery", new Double(Math.pow(adaptedModel.getOwnedBlocks().size(),2)).intValue());
		
		String result = "";
		
		// Block Level
		// TODO Feature level
		for(Block b1 : adaptedModel.getOwnedBlocks()){
			monitor.subTask("Checking relations of " + b1.getName());
			for(Block b2 : adaptedModel.getOwnedBlocks()){
				if(b1 != b2){
					// check monitor
					if(monitor.isCanceled()){
						return result;
					}
					// here we have all binary combinations A and B, B and A etc.
					// requires b1 -> b2
					if(blockRequiresAnotherBlockB(b1,b2)){
						// TODO provide more info about the origin of the constraint, the involved elements for example
						result = result + b1.getName() + " requires " + b2.getName() + "\n";
					}
					
					// TODO mutual exclusion
				}
				monitor.worked(1);
			}
		}
		
		return result;
	}
	
	public boolean blockRequiresAnotherBlockB(Block a, Block b){
		for(BlockElement block1Element : a.getOwnedBlockElements()){
			List<Object> block1ElementDependencies = getAllDependencies(block1Element);
			for(BlockElement block2Element : b.getOwnedBlockElements()){
				for(ElementWrapper elementW2 : block2Element.getElementWrappers()){
					if(block1ElementDependencies.contains(elementW2.getElement())){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static List<Object> getAllDependencies(BlockElement blockElement){
		List<Object> result = new ArrayList<Object>();
		for(ElementWrapper elementW1 : blockElement.getElementWrappers()){
			IElement element = (IElement)elementW1.getElement();
			Map<String, List<Object>> map = element.getDependencies();
			for(String key : map.keySet()){
				List<Object> dependencies = map.get(key);
				for(Object o : dependencies){
					if(!result.contains(o)){
						result.add(o);
					}
				}
			}
		}
		return result;
	}

}
