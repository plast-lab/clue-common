package org.clyze.analysis

import spock.lang.Specification

/**
 * Created by saiko on 4/2/2017.
 */
class AnalysisFamiliesSpec extends Specification {

    def "AnalysisFamilies registration works"() {
        setup:
        AnalysisFamilies.register(new TestAnalysisFamily())

        expect:
        AnalysisFamilies.isRegistered('test')
    }

    def "AnalysisFamilies lookup works"() {
        expect:
        AnalysisOption o = AnalysisFamilies.supportedOptionsOf('test').find { it.id == option}
        assert o.value == value

        where:
        family | option   | value
        'test' | 'option1'| 'option1'
        'test' | 'option2'| true
    }
}
