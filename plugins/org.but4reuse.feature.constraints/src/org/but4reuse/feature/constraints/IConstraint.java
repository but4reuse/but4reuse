package org.but4reuse.feature.constraints;

import java.util.List;

/**
 * IConstraint
 * 
 * @author jabier.martinez
 */
public interface IConstraint {

	public List<String> getExplanations();

	public void setExplanations(List<String> explanations);

	public int getNumberOfReasons();

	public void setNumberOfReasons(int numberOfReasons);
	
	public String getText();

}
