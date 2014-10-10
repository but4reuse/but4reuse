package org.but4reuse.visualisation.visualiser;

import java.util.HashMap;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleMarkupKind;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleMarkupProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class BlockMarkupProvider extends SimpleMarkupProvider {

	private Map<Block,IMarkupKind> blocksAndNames = new HashMap<Block,IMarkupKind>();
	
	public void update(AdaptedModel adaptedModel) {
		// Remove previous
		resetMarkupsAndKinds();
		getBlocksAndNames().clear();
		
		// Update
		int i = 0;
		for(Block block : adaptedModel.getOwnedBlocks()){
			String blockName = "Block "+getNumberWithZeros(i,adaptedModel.getOwnedBlocks().size()-1);
			IMarkupKind kind = new SimpleMarkupKind(blockName);
			addMarkupKind(kind);
			if(i==0){
				// Block 0 is usually the common, force it to be black
				setColorFor(kind, new Color(Display.getCurrent(),255,255,255));
			}
			i++;
			blocksAndNames.put(block, kind);
		}
	}
	
	public String getNumberWithZeros(int number, int maxNumber) {
		String _return = String.valueOf(number);
		for (int zeros = _return.length(); zeros < String.valueOf(maxNumber).length(); zeros++) {
			_return = "0" + _return;
		}
		return _return;
	}

	public Map<Block,IMarkupKind> getBlocksAndNames() {
		return blocksAndNames;
	}

	public void setBlocksAndNames(Map<Block,IMarkupKind> blocksAndNames) {
		this.blocksAndNames = blocksAndNames;
	}

}
