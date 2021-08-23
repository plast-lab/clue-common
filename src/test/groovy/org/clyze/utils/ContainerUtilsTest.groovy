package org.clyze.utils

import spock.lang.Specification

class ContainerUtilsTest extends Specification {
    def "AAR can be repackaged as JAR"() {
        when:
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

    def "WAR can be split into JAR files"() {
        when:
        File war = new File(ContainerUtilsTest.classLoader.getResource('daytrader-ee7-web.war').toURI())
        println "WAR: ${war.canonicalPath}"
        Set<String> tmpDirs = [] as Set<String>
        List<String> jars = ContainerUtils.toJars([war.canonicalPath], false, tmpDirs)
        println "JAR: ${jars.get(0)} (total: ${jars})"
        JHelper.cleanUp(tmpDirs)

        then:
        assert jars.size() == 2
        assert jars.get(0).endsWith('.jar')
    }
}
