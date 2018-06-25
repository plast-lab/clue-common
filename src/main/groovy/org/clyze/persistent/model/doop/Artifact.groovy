package org.clyze.persistent.model.doop

import groovy.transform.EqualsAndHashCode

import org.clyze.persistent.model.ItemImpl

/**
 * A software artifact (jar, aar, etc) of a project.
 * 
 * The artifact refers either to an application or a dependency (isDependency) and has an ArtifactKind.
 */
@EqualsAndHashCode
class Artifact extends ItemImpl {

	public static final String CHECKSUM_ALGORITHM = "SHA1"

	String id
	String name
	ArtifactKind kind
	boolean isDependency
	String sourcesName
	String checksum
	long sizeInBytes	
	Set<String> packages = [] as Set
	Artifact parent

	Artifact(String id, 
			 String name, 
			 ArtifactKind kind, 
			 boolean isDependency=true, 
			 String sourcesName=null, 
			 String checksum = null, 
			 long sizeInBytes=0,
			 Artifact parent=null) {
		this.id           = id
		this.name         = name
		this.kind         = kind
		this.isDependency = isDependency
		this.sourcesName  = sourcesName
		this.checksum     = checksum
		this.sizeInBytes  = sizeInBytes
		this.parent       = parent
	}
}
