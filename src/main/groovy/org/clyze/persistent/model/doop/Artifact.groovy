package org.clyze.persistent.model.doop

import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor
import org.clyze.persistent.model.ItemImpl

/**
 * A software artifact (jar, aar, etc) of a project.
 * 
 * The artifact refers either to an application or a dependency (isDependency) and has an ArtifactKind.
 */
@EqualsAndHashCode
@TupleConstructor
class Artifact extends ItemImpl {

	public static final String CHECKSUM_ALGORITHM = "SHA1"

	String id
	String name
	ArtifactKind kind
	boolean isDependency = true
	String sourcesName
	String checksum
	long sizeInBytes = 0
	Set<String> packages = [] as Set
	String parentArtifactId
}
