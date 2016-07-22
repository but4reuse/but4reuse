package org.but4reuse.adapter.sourcecode.extension;

/**
 * 
 * @author colympio
 * Object used to store the values of one line of CSV  file
 */
public class NodeFromCSV {

	private String id;
	private String kind;
	private String name;
	private String qualifiedName;
	private String type;
	
	
	
	
	public NodeFromCSV(String id, String kind, String name, String qualifiedName, String type) {
		super();
		this.id = id;
		this.kind = kind;
		this.name = name;
		this.qualifiedName = qualifiedName;
		this.type = type;
	}
	
	public String getId() {
		return id;
	}
	public String getKind() {
		return kind;
	}
	public String getName() {
		return name;
	}
	public String getQualifiedName() {
		return qualifiedName;
	}
	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "NodeFromCSV [id=" + id + ", kind=" + kind + ", name=" + name + ", qualifiedName=" + qualifiedName
				+ ", type=" + type + "]";
	}
	
	
	
	
	
	
}
