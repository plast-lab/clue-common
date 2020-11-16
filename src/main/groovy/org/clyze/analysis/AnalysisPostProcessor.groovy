package org.clyze.analysis

import groovy.transform.CompileStatic

@CompileStatic
interface AnalysisPostProcessor<A extends Analysis> {
	void process(A analysis)
}
