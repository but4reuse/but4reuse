package org.but4reuse.adapter.sourcecode.extension;

import java.util.ArrayList;
import java.util.List;

public class EdgeFromCSV {

	private String id;
	private String type;
	private List<String> target;

	public EdgeFromCSV(String id,String type) {
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
	
	public boolean contains(String id){
		if(this.id == id)
			return true;
		else if(this.target.contains(id))
			return true;
		else
			return false;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "EdgeFromCSV [id=" + id + ", type=" + type + ", target=" + target + "]";
	}

	
}
