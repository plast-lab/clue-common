package org.clyze.input

import org.clyze.analysis.InputType
import org.clyze.fetcher.Artifact
import org.clyze.fetcher.ArtifactFetcher
import org.clyze.fetcher.IvyArtifactFetcher

/**
 * Resolves the input as an Apache Ivy module descriptor that is downloaded using the default ivy settings.
 */
class IvyResolver implements InputResolver {

	String name() { "ivy" }

    void resolve(String artifactId, InputResolutionContext ctx, InputType inputType) {
        ArtifactFetcher.Repo repo = ArtifactFetcher.Repo.JCENTER
        Artifact art = new IvyArtifactFetcher().fetch(artifactId, repo, false)

        def resolvedInput = art.jar
        def resolvedInputDependencies = art.dependencies.collect { new File(it) }
        def sourcesJar = art.sourcesJar

        if (sourcesJar && [InputType.INPUT, InputType.INPUT_LOCAL, InputType.LIBRARY].contains(inputType)) {
            ctx.add(artifactId, InputType.SOURCES)
            ctx.set(artifactId, sourcesJar, InputType.SOURCES)
        }

        if (inputType == InputType.LIBRARY) {
            List<File> libs = [resolvedInput] + resolvedInputDependencies
            ctx.set(artifactId, libs, InputType.LIBRARY)
        } else if (inputType == InputType.INPUT || inputType == InputType.INPUT_LOCAL ) {
            ctx.set(artifactId, resolvedInput, inputType)
            ctx.add(artifactId, InputType.LIBRARY) //add the same input as library dependency too
            ctx.set(artifactId, resolvedInputDependencies, InputType.LIBRARY)
        } else {
            throw new RuntimeException("Ivy resolution is not supported for ${inputType.toString()} inputs.")
        }

        art.cleanUp()
    }
}
