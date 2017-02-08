package org.but4reuse.adapter.sourcecode.extension;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author colympio
 * 
 *         Object used to store the values of one edge file line
 */
public class EdgeFromCSV {

	private String id;
	private String type;
	private List<String> target;

	public EdgeFromCSV(String id, String type) {
		super();
		this.id = id;
		this.type = type;
		this.target = new ArrayList<String>();
	}

	public String getId() {
		return id;
	}

	public List<String> getTarget() {
		return target;
	}

	public void addTarget(String t) {
		this.target.add(t);
	}

	public boolean contains(String id) {
		if (this.id == id)
			return true;
		else if (this.target.contains(id))
			return true;
		else
			return false;
	}

	public String getType() {
		return type;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EdgeFromCSV other = (EdgeFromCSV) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EdgeFromCSV [id=" + id + ", type=" + type + ", target=" + target + "]";
	}

}
