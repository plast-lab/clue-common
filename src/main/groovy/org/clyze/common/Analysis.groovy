package org.clyze.common

abstract class Analysis {

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
	

	protected Analysis(AnalysisFamily family, String id, String name) {
		this.family = family
		this.id = id
		this.name = name
	}

	/**
	 * The phases of the analysis.
	 */
	abstract Iterable<AnalysisPhase> phases()
}
