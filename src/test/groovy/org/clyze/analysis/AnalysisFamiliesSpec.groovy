package org.clyze.analysis

import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by saiko on 4/2/2017.
 */
class AnalysisFamiliesSpec extends Specification {

    @Shared String TEST_ID = 'test'

    def "AnalysisFamilies registration works"() {
        setup:
        AnalysisFamilies.register(new TestAnalysisFamily())

        expect:
        AnalysisFamilies.isRegistered(TEST_ID)
        AnalysisFamilies.getRegisteredFamilies().keySet().contains(TEST_ID)
    }

    def "AnalysisFamilies lookup works"() {
        expect:
        AnalysisOption o = AnalysisFamilies.supportedOptionsOf('test').find { it.id == option}
        assert o.value == value

        where:
        family  | option   | value
        TEST_ID | 'option1'| 'option1'
        TEST_ID | 'option2'| true
    }
}
