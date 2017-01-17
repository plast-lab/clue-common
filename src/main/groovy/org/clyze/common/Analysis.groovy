package org.clyze.common

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
	 * The input filepaths of the analysis
	 */
	List<String> inputFiles

	protected Analysis(AnalysisFamily family,
	                   String id,
	                   String name,
	                   Map<String, AnalysisOption> options,
	                   List<String> inputFiles) {
		this.family = family
		this.id = id
		this.name = name
		this.options = options
		this.inputFiles
	}

	/**
	 * The phases of the analysis
	 */
	abstract Iterable<AnalysisPhase> phases()
}
