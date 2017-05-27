package org.clyze.utils

import groovy.transform.TypeChecked
import org.apache.commons.io.FileUtils
import org.clyze.analysis.Analysis
import org.clyze.analysis.AnalysisOption

@TypeChecked
class CPreprocessor {

	String macroCli
	Executor executor
	boolean emitLineMarkers

	CPreprocessor(Analysis analysis, Executor executor) {
		macroCli = analysis.options.values()
				.findAll { AnalysisOption option ->
			option.forPreprocessor && option.value
		}
		.collect { AnalysisOption option ->
			if (option.value instanceof Boolean)
				return "-D${option.id}"
			else if (option.value instanceof Integer)
				return "-D${option.id}=${option.value}"
			else
				return "-D${option.id}='\"${option.value}\"'"
		}
		.join(" ")
		this.executor = executor
		emitLineMarkers = false
	}

	CPreprocessor enableLineMarkers() {
		emitLineMarkers = true
		return this
	}

	CPreprocessor disableLineMarkers() {
		emitLineMarkers = false
		return this
	}

	CPreprocessor preprocessIfExists(String output, String input, String... includes) {
		if (new File(input).isFile())
			preprocess(output, input, includes)
		else {
			def tmpFile = new File(FileUtils.getTempDirectory(), "tmpFile")
			tmpFile.createNewFile()
			preprocess(output, tmpFile.getCanonicalPath(), includes)
			FileUtils.deleteQuietly(tmpFile)
		}
		return this
	}

	CPreprocessor preprocess(String output, String input, String... includes) {
		def lineMarkersFlag = emitLineMarkers ? '' : ' -P'
		def includeArgs = includes.collect { "-include $it" }.join(" ")
		executor.execute("cpp $lineMarkersFlag $macroCli $input $includeArgs $output")
		return this
	}

	// Preprocess input file and put contents *in the beginning* of the output file.
	void includeAtStart(String output, String input, String... includes) {
		def tmpFile = new File(FileUtils.getTempDirectory(), "tmpFile")
		preprocess(tmpFile.getCanonicalPath(), output, (includes + [input]) as String[])
		FileUtils.copyFile(tmpFile, new File(output))
		FileUtils.deleteQuietly(tmpFile)
	}

	void includeAtEnd(String output, String input, String... includes) {
		includeAtEnd0(output, input, includes, this.&preprocess)
	}

	void includeAtEndIfExists(String output, String input, String... includes) {
		includeAtEnd0(output, input, includes, this.&preprocessIfExists)
	}

	// Implementation method called by *includeAtEnd* and *includeAtEndIfExists* with the
	// appropriate preprocess method (*preprocess* and *preprocessIfExists* respectively) as parameter.
	private void includeAtEnd0(String output, String input, String... includes, Closure closure) {
		def tmpFile = new File(FileUtils.getTempDirectory(), "outTmpFile")
		closure(tmpFile.getCanonicalPath(), input, includes)
		tmpFile.withInputStream { stream ->
			new File(output) << stream
		}
		FileUtils.deleteQuietly(tmpFile)
	}
}
