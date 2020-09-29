package org.clyze.input

import org.clyze.utils.JHelper
import spock.lang.IgnoreIf
import spock.lang.Specification

class PlatformManagerTest extends Specification {

    @IgnoreIf({ !JHelper.java9Plus() })
    def "Java 9+ platform generation"() {
        when:
        File tmpDir = File.createTempDir()
        File jmodsDir = new File(JHelper.getJavaHome(), 'jmods')
        String platformId = 'java_' + JHelper.getJavaVersion()
        File rtJar = PlatformManager.getJava9PlusJar(platformId, 'build/platform-cache', jmodsDir)
        println "rtJar = ${rtJar.canonicalPath}"

        then:
        rtJar != null
        rtJar.exists()
        rtJar.deleteOnExit()
    }
}
