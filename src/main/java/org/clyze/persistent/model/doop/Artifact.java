package org.clyze.persistent.model.doop;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

import org.clyze.persistent.model.ItemImpl;

/**
 * A software artifact (jar, aar, etc) of a project.
 * 
 * The artifact refers either to an application or a dependency (isDependency) and has an ArtifactKind.
 */
public class Artifact extends ItemImpl {

	public static final String CHECKSUM_ALGORITHM = "SHA1";

	protected String id;
	protected String name;
	protected ArtifactKind kind;
	protected boolean isDependency = true;
	protected String sourcesName;
	protected String checksum;
	protected long sizeInBytes = 0;
	protected Set<String> packages = new HashSet<>();
	protected String parentArtifactId;

	public Artifact(String id, String name, ArtifactKind kind, boolean isDependency, String sourcesName, String checksum, long sizeInBytes) {
		this(id, name, kind, isDependency, sourcesName, checksum, sizeInBytes, new HashSet<String>(), null);
	}	

	public Artifact(String id, 
					String name, 
					ArtifactKind kind, 
					boolean isDependency, 
					String sourcesName, 
					String checksum, 
					long sizeInBytes, 
					Set<String> packages,
					String parentArtifactId) {
		this.id = id;
		this.name = name;
		this.kind = kind;
		this.isDependency = isDependency;
		this.sourcesName = sourcesName;
		this.checksum = checksum;
		this.sizeInBytes = sizeInBytes;
		this.packages = packages;
		this.parentArtifactId = parentArtifactId;
	}	

	protected void saveTo(Map<String, Object> map) {
		map.put("id", this.id);
		map.put("name",this.name);
		map.put("kind", this.kind.name());
		map.put("isDependency", this.isDependency);
		map.put("sourcesName", this.sourcesName);
		map.put("checksum", this.checksum);
		map.put("sizeInBytes", this.sizeInBytes);
		map.put("packages", this.packages);
		map.put("parentArtifactId", this.parentArtifactId);
	}

	protected void loadFrom(Map<String, Object> map){
		this.id               = (String) map.get("id");
		this.name             = (String) map.get("name");
		this.kind             = ArtifactKind.valueOf((String) map.get("kind"));
		this.isDependency     = (Boolean) map.get("isDependency");
		this.sourcesName      = (String) map.get("sourcesName");
		this.checksum         = (String) map.get("checksum");
		this.sizeInBytes      = ((Number) map.get("sizeInBytes")).longValue();
		this.packages.addAll((Collection) map.get("packages"));
		this.parentArtifactId = (String) map.get("parentArtifactId");
	}

	public String getId() {
		return id;
	}
}
