package org.clyze.utils

import spock.lang.Specification

class ContainerUtilsTest extends Specification {
    def "AAR can be repackaged as JAR"() {
        when:
        File aar = new File(ContainerUtilsTest.classLoader.getResource('assertj-android-cardview-v7-1.2.0.aar').toURI())
        println "AAR: ${aar.canonicalPath}"
        Set<String> tmpDirs = new HashSet<>()
        Set<String> jarLibs = new HashSet<>()
        List<String> jars = ContainerUtils.toJars([aar.canonicalPath], false, jarLibs, tmpDirs)
        println "JAR: ${jars.get(0)}"
        JHelper.cleanUp(tmpDirs)

        then:
        assert jars.size() == 1
        assert jars.get(0).endsWith('.jar')
    }

    def "WAR can be split into JAR files"() {
        when:
        File war = new File(ContainerUtilsTest.classLoader.getResource('daytrader-ee7-web.war').toURI())
        println "WAR: ${war.canonicalPath}"
        Set<String> tmpDirs = new HashSet<>()
        Set<String> jarLibs = new HashSet<>()
        List<String> jars = ContainerUtils.toJars([war.canonicalPath], false, jarLibs, tmpDirs)
        println "JAR: ${jars.get(0)} (total: ${jars} / ${jarLibs})"
        JHelper.cleanUp(tmpDirs)

        then:
        assert jars.size() == 1
        assert jarLibs.size() == 1
        assert jars.get(0).endsWith('.jar')
    }
}
