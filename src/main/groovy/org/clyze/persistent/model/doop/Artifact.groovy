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
	boolean sourcesAvailable
	long sizeInBytes

	Artifact() {

	}

	Artifact(String id, String name, ArtifactKind kind, boolean isDependency, boolean sourcesAvailable=false, long sizeInBytes=0) {
		this.id = id
		this.name = name
		this.kind = kind
		this.isDependency = isDependency
		this.sourcesAvailable = sourcesAvailable
		this.sizeInBytes = sizeInBytes
	}
}