package org.clyze.utils

import org.clyze.fetcher.ArtifactFetcher
import org.clyze.fetcher.IvyArtifactFetcher
import spock.lang.Specification

class ContainerUtilsTest extends Specification {
    def "AAR can be repackaged as JAR"() {
        when:
        IvyArtifactFetcher fetcher = new IvyArtifactFetcher()
        ArtifactFetcher.Repo repo = ArtifactFetcher.Repo.MAVEN_CENTRAL
        File aar = new File(ContainerUtilsTest.classLoader.getResource('assertj-android-cardview-v7-1.2.0.aar').toURI())
        println "AAR: ${aar.canonicalPath}"
        Set<String> tmpDirs = [] as Set<String>
        List<String> jars = ContainerUtils.toJars([aar.canonicalPath], false, tmpDirs)
        println "JAR: ${jars.get(0)}"
        JHelper.cleanUp(tmpDirs)

        then:
        assert jars.size() == 1
        assert jars.get(0).endsWith('.jar')
    }
}
