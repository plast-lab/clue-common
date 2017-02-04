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
        setup:
        Helper.registerFamily(new TestAnalysisFamily())

        expect:
        AnalysisFamilies.get(family).supportedOptions().find { it.id == option}

        where:
        family | option
        'test' | 'option1'
        'test' | 'option2'
    }
}
