package org.clyze.analysis

import groovy.transform.Canonical

@Canonical
abstract class Analysis implements Runnable {
	/**
	 * The family of the analysis (e.g. doop or cclyzer)
	 */
	AnalysisFamily family

	/**
	 * The unique identifier of the analysis (that determines the caching)
	 */
	String id

	/**
	 * The name of the analysis (that determines the logic)
	 */
	String name

	/**
	 * The options of the analysis
	 */
	Map<String, AnalysisOption> options

	/**
	 * The output dir for the analysis
	 */
	File outDir

	/**
	 * The input filepaths of the analysis
	 */
	List<File> inputFiles

	/**
	 * Generic query entry-point
	 */
	abstract void processRelation(String relation, Closure outputLineProcessor)
}
