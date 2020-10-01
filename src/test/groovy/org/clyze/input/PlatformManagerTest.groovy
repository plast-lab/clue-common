package org.clyze.input

import org.clyze.utils.JHelper
import spock.lang.IgnoreIf
import spock.lang.Specification

class PlatformManagerTest extends Specification {

    @IgnoreIf({ !JHelper.java9Plus() })
    def "Java 9+ platform generation"() {
        when:
        File jmodsDir = new File(JHelper.getJavaHome(), 'jmods')
        String platformId = 'java_' + JHelper.getJavaVersion()
        PlatformManager pm = new PlatformManager(cacheDir: 'build/platform-cache')
        File rtJar = pm.getJava9PlusJar(platformId, jmodsDir)
        // Call again to get the cached JAR.
        File rtJarCached = pm.getJava9PlusJar(platformId, jmodsDir)
        println "rtJar = ${rtJar.canonicalPath}, rtJarCached = ${rtJarCached.canonicalPath}"

        then:
        rtJarCached != null
        rtJarCached.exists()
        rtJar != null
        rtJar.exists()
        rtJar.canonicalPath == rtJarCached.canonicalPath
    }
}
