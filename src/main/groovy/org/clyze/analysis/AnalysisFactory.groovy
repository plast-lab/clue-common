package org.clyze.analysis

interface AnalysisFactory<A extends Analysis> {
	A newAnalysis(AnalysisFamily family, Map<String, AnalysisOption> options)
}
