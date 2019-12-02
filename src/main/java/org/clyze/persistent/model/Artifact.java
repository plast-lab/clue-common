package org.clyze.persistent.model;

import java.util.Map;
import java.util.Collection;
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
	protected boolean isDependency;
	protected String sourcesName;
	protected String checksum;
	protected long sizeInBytes;
	protected final Set<String> packages;
	protected String parentArtifactId;

	public Artifact(String id, String name, ArtifactKind kind, boolean isDependency, String sourcesName, String checksum, long sizeInBytes) {
		this(id, name, kind, isDependency, sourcesName, checksum, sizeInBytes, new HashSet<>(), null);
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

	public void fromMap(Map<String, Object> map){
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

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public ArtifactKind getKind() {
		return kind;
	}

	public boolean isDependency() {
		return isDependency;
	}

	public Set<String> getPackages() {
		return packages;
	}

	public void setParentArtifactId(String parentArtifactId) {
		this.parentArtifactId = parentArtifactId;
	}

	public String getParentArtifactId() {
		return parentArtifactId;
	}
}
