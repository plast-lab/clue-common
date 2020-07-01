package org.clyze.fetcher

import groovy.transform.ToString
import java.nio.file.Files
import org.apache.commons.io.FileUtils
import org.apache.ivy.core.report.ResolveReport

@ToString(includeNames=true)
class Artifact {

    String id
    ResolveReport report
    File jar
    File sourcesJar
    Set<String> dependencies

    protected File baseOutDir

    Artifact prepare() {
        String idForFileName = id.replaceAll(":", "_")
        baseOutDir = Files.createTempDirectory(idForFileName).toFile()
        baseOutDir.deleteOnExit()
        return this
    }

    Artifact cleanUp() {
        if (baseOutDir) {

            println "Cleaning up $baseOutDir"

            FileUtils.forceDelete(baseOutDir)
            baseOutDir = null
            sourcesDir = null
            jsonDir    = null
        }

        return this
    }
}
