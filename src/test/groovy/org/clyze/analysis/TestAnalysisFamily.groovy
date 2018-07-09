package org.clyze.analysis

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
        ]
    }

    @Override
    Map<String, AnalysisOption> supportedOptionsAsMap() {
        return [ option1 : 'option1', option2 : true ]
    }
}
