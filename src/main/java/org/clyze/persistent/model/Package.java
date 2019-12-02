package org.clyze.persistent.model;

import java.util.Map;
import java.util.List;

/**
 * A package of a project.
 */
public class Package extends ItemImpl {

	private String id;
	private String name;
	private String fullyQualifiedName;
	private String parentPackageId;
	//A package can be defined in multiple artifacts
	private List<String> artifactIds;

	public Package() {}

	public Package(String id, String name, String fullyQualifiedName, String parentPackageId, List<String> artifactIds) {
		this.id = id;
		this.name = name;
		this.fullyQualifiedName = fullyQualifiedName;
		this.parentPackageId = parentPackageId;
		this.artifactIds = artifactIds;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	protected void saveTo(Map<String, Object> map) {		
		map.put("id", this.id);
		map.put("name", this.name);
		map.put("fullyQualifiedName", this.fullyQualifiedName);
		map.put("parentPackageId", this.parentPackageId);
		map.put("artifactIds", this.artifactIds);
	}

	public void fromMap(Map<String, Object> map){
		this.id                 = (String) map.get("id");
		this.name               = (String) map.get("name");
		this.fullyQualifiedName = (String) map.get("fullyQualifiedName");
		this.parentPackageId    = (String) map.get("parentPackageId");
		this.artifactIds        = (List<String>) map.get("artifactIds");
	}
}
