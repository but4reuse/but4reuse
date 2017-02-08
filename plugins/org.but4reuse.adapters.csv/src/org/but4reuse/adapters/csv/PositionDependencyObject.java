package org.but4reuse.adapters.csv;

import org.but4reuse.adapters.IDependencyObject;
import org.eclipse.swt.graphics.Point;

/**
 * Position dependency object
 * 
 * @author jabier.martinez
 */
public class PositionDependencyObject implements IDependencyObject {

	Point position;

	public PositionDependencyObject(Point position) {
		this.position = position;
	}

	@Override
	public int getMinDependencies(String dependencyID) {
		return 0;
	}

	@Override
	public int getMaxDependencies(String dependencyID) {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PositionDependencyObject) {
			return position.equals(((PositionDependencyObject) obj).position);
		}
		return super.equals(obj);
	}

	@Override
	public String getDependencyObjectText() {
		return position.toString();
	}

}
