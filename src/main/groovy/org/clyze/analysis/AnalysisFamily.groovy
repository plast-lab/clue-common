package org.clyze.analysis

/**
 * An analysis family (e.g. doop, cclyzer, etc.).
 *
 * The init() method should be called to initialize the underlying machinery (e.g. in order to produce the supported options
 * dynamically, if required).
 *
 * The supportedOptions() method should be idempotent, once the init() call has succeeded.
 */
interface AnalysisFamily {

	/** The name of the AnalysisFamily. */
	String getName()

	/** Initialize the family. Required to be called once per instance. */
	void init() throws RuntimeException

	/** The list of options supported by the analysis. */
	List<AnalysisOption> supportedOptions()

	/** The list of options supported by the analysis, exposed as a map. */
	Map<String, AnalysisOption> supportedOptionsAsMap()
}
