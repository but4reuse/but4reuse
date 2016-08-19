package org.but4reuse.adapter.sourcecode.extension;

/**
 * 
 * @author colympio
 * 
 *         Object used to store the values of one node line of CSV file
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeFromCSV other = (NodeFromCSV) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (kind == null) {
			if (other.kind != null)
				return false;
		} else if (!kind.equals(other.kind))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (qualifiedName == null) {
			if (other.qualifiedName != null)
				return false;
		} else if (!qualifiedName.equals(other.qualifiedName))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
