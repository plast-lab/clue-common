package org.clyze.fetcher

import groovy.transform.CompileStatic

@CompileStatic
interface ArtifactFetcher {

	enum Repo {
        MAVEN_CENTRAL,
        JCENTER
    }

	Artifact fetch(String id, Repo repo, boolean ignoreSources)
}
