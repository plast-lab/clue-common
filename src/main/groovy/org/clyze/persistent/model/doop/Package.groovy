package org.clyze.persistent.model.doop

import groovy.transform.EqualsAndHashCode

import org.clyze.persistent.model.ItemImpl

/**
 * A package of a project.
 */
@EqualsAndHashCode
class Package extends ItemImpl {

	String id
	String name
	String fullyQualifiedName
	String parentPackageId
	//A package can be defined in multiple artifacts
	List<String> artifactIds

	Package() {

	}

	Package(String id, String name, String fullyQualifiedName, String parentPackageId, List<String> artifactIds = []) {
		this.id = id
		this.name = name
		this.fullyQualifiedName = fullyQualifiedName
		this.parentPackageId = parentPackageId
		this.artifactIds = artifactIds
	}
}