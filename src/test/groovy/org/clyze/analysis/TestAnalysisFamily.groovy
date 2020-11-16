package org.clyze.analysis

import groovy.transform.CompileStatic

@CompileStatic
class TestAnalysisFamily implements AnalysisFamily {
    @Override
    String getName() {
        return 'test'
    }

    @Override
    void init() throws RuntimeException {

    }

    @Override
    List<AnalysisOption> supportedOptions() {
        return [
            new AnalysisOption<String>(
                id:'option1',
                value: 'option1'
            ),
            new AnalysisOption<Boolean>(
                id:'option2',
                value: true
            )
        ] as List<AnalysisOption>
    }

    @Override
    Map<String, AnalysisOption> supportedOptionsAsMap() {
        return [ option1 : 'option1', option2 : true ] as Map<String, AnalysisOption>
    }

    @Override
    void cleanDeploy() {
        println "Test clean() invoked."
    }
}
