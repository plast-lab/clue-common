package org.clyze.analysis

import groovy.transform.AutoClone

@AutoClone
class AnalysisOption<T> {
	/**
	 * The copy constructor pattern
	 */
	static AnalysisOption<T> newInstance(AnalysisOption<T> option) {
		return new AnalysisOption<>(
				id: option.id,
				name: option.name,
				description: option.description,
				value: option.value,
				validValues: option.validValues,
				multipleValues: option.multipleValues,
				isMandatory: option.isMandatory,
				forCacheID: option.forCacheID,
				forPreprocessor: option.forPreprocessor,
				webUI: option.webUI,
				cli: option.cli,
				argName: option.argName,
				isAdvanced: option.isAdvanced,
				isFile: option.isFile,
				isDir: option.isDir,
				nonStandard: option.nonStandard
		)
	}

	/**
	 * The id of the option as used internally by the code (e.g. by the preprocessor, the web form, etc)
	 */
	String id

	/**
	 * The name of the option (for the end-user)
	 */
	String name = null

	/**
	 * The description of the option (for the end-user)
	 */
	String description

	/**
	 * The value of the option
	 */
	T value

	/**
	 * An optional set of valid values
	 */
	Set<T> validValues = null

	/**
	 * Indicate whether the option accepts many values
	 */
	boolean multipleValues = false

	/**
	 * The type of the values
	 */
	InputType valueType = null

	/**
	 * Indicates whether the option is a mandatory one
	 */
	boolean isMandatory = false

	/**
	 * Indicates whether the option affects the cacheID generation
	 */
	boolean forCacheID = false

	/**
	 * Indicates whether the option affects the preprocessor
	 */
	boolean forPreprocessor = false

	/**
	 * Indicates whether the option can be specified by the user in the web UI
	 */
	boolean webUI = false

	/**
	 * Indicates whether the option can be specified by the user in the command line interface
	 */
	boolean cli = true

	/**
	 * The name of the option's arg value. If null, the option does not take arguments (it is a flag/boolean option)
	 */
	String argName = null

	/**
	 * Indicates whether the option is "advanced". Advanced options are treated differently by the UIs
	 */
	boolean isAdvanced = false

	/**
	 * Indicates whether the option is a file
	 */
	boolean isFile = false

	/**
	 * Indicates whether the option is a directoy
	 */
	boolean isDir = false

	/**
	 * Indicates whether the option is a non-standard flag
	 */
	boolean nonStandard = false

	@Override
	String toString() { "$id=$value" }
}
