package org.clyze.persistent.model.doop

import groovy.transform.EqualsAndHashCode

import org.clyze.persistent.model.ItemImpl

/**
 * A software artifact (jar, aar, etc) of a project.
 */
@EqualsAndHashCode
class Artifact extends ItemImpl {

	String id
	String name
	ArtifactKind kind
	boolean isDependency

	Artifact() {

	}

	Artifact(String id, String name, ArtifactKind kind, boolean isDependency) {
		this.id = id
		this.name = name
		this.kind = kind
		this.isDependency = isDependency
	}
}