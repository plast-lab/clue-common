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
}
