package org.clyze.analysis

import groovy.transform.Canonical

@Canonical
abstract class Analysis implements Runnable {
	/**
	 * The family of the analysis (e.g. doop or cclyzer)
	 */
	AnalysisFamily family

	/**
	 * The options of the analysis
	 */
	Map<String, AnalysisOption> options

	/**
	 * Optional closure for handling monitoring of the analysis
	 */
	Closure monitorClosure = null

	/**
	 * Generic query entry-point
	 */
	abstract void processRelation(String relation, Closure outputLineProcessor)

	/**
	 * Returns the id of the analysis
	 * @return the analysis id
	 */
	abstract String getId()
}
