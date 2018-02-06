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

	String id
	String name
	ArtifactKind kind
	boolean isDependency
	String sourcesName
	String sha1
	long sizeInBytes	
	Set<String> packages = [] as Set

	Artifact(String id, String name, ArtifactKind kind, boolean isDependency=true, String sourcesName=null, String sha1 = null, long sizeInBytes=0) {
		this.id = id
		this.name = name
		this.kind = kind
		this.isDependency = isDependency
		this.sourcesName = sourcesName
		this.sha1 = sha1
		this.sizeInBytes = sizeInBytes
	}
}
