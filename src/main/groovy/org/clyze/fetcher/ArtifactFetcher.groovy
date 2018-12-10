package org.clyze.fetcher

interface ArtifactFetcher {

	enum Repo {
        MAVEN_CENTRAL,
        JCENTER
    }

	Artifact fetch(String id, Repo repo, boolean ignoreSources)
}
