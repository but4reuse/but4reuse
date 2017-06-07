package org.but4reuse.feature.constraints;

import java.util.List;

import org.but4reuse.adaptedmodel.Block;

/**
 * TODO This is a provisional API of binary operation
 * 
 * @author jabier.martinez
 */
public interface IConstraint {
	String REQUIRES = "REQUIRES";
	String MUTUALLY_EXCLUDES = "MUTUALLY EXCLUDES";
	String FREETEXT = "constraint";

	public Block getBlock1();

	public Block getBlock2();

	public void setBlock1(Block block);

	public void setBlock2(Block block);

	public String getType();

	public void setType(String type);

	public List<String> getExplanations();

	public void setExplanations(List<String> explanations);

	public int getNumberOfReasons();

	public void setNumberOfReasons(int numberOfReasons);

	public String getText();

	public void setText(String text);
}
