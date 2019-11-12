package org.clyze.utils

import spock.lang.Specification

class DoopConventionsTest extends Specification {
    def "JimpleDir"(String arg, String res) {
        expect:
        DoopConventions.jimpleDir(arg) == res
        where:
        arg | res
        "x" | "x/jimple"
    }

    /**
     * Invoking setSeparator() should fail for standalone clue-common
     * (no Soot available) and this failure should be observable.
     */
    def "SetSeparator"() {
        when:
        DoopConventions.setSeparator()
        then:
        DoopConventions.setSeparatorFailed == true
    }
}
