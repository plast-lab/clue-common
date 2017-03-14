package org.clyze.analysis

interface AnalysisPostProcessor<A extends Analysis> {
	void process(A analysis)
}
