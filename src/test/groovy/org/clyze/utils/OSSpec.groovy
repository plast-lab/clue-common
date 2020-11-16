package org.clyze.utils

import org.clyze.utils.OS
import spock.lang.Specification

class OSSpec extends Specification {
    def "Operating system is supported"() {
        expect:
        OS.linux || OS.macOS || OS.win
    }
}
