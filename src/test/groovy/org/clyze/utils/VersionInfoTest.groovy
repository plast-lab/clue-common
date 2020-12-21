package org.clyze.utils

import spock.lang.Specification

class VersionInfoTest extends Specification {
    def "Version information works"() {
        when:
        String vi = JHelper.getVersionInfo(VersionInfoTest.class)
        println "Version information: ${vi}"

        then:
        assert vi == 'Git hash: test-git-id'
    }
}
