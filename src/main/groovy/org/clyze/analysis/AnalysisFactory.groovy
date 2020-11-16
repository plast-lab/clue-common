package org.clyze.analysis

import groovy.transform.CompileStatic

@CompileStatic
interface AnalysisFactory<A extends Analysis> {
	A newAnalysis(AnalysisFamily family, Map<String, AnalysisOption<?>> options)
}
