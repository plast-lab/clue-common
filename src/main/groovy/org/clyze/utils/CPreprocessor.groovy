package org.clyze.utils

import groovy.transform.TypeChecked
import org.apache.commons.io.FileUtils
import org.clyze.analysis.Analysis
import org.clyze.analysis.AnalysisOption

import java.nio.file.Files

@TypeChecked
class CPreprocessor {

	List<String> macroCli
	Executor executor
	boolean emitLineMarkers

	CPreprocessor(Analysis analysis, Executor executor) {
		macroCli = analysis.options.values()
				.findAll { AnalysisOption option ->
			option.forPreprocessor && option.value
		}
		.collect { AnalysisOption option ->
			if (option.value instanceof Boolean)
				"-D${option.id}" as String
			else
				"-D${option.id}=${option.value}" as String
		}
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
			def tmpFile = createUniqueTmpFile()
			tmpFile.createNewFile()
			preprocess(output, tmpFile.getCanonicalPath(), includes)
			FileUtils.deleteQuietly(tmpFile)
		}
		return this
	}

	CPreprocessor preprocess(String output, String input, String... includes) {
		def cmd = ['cpp']
		if (!emitLineMarkers) cmd << '-P'
		cmd += macroCli
		cmd << input
		includes.each { cmd += ['-include', it as String] }
		cmd << output
		executor.execute(cmd)
		return this
	}

	// Preprocess input file and put contents *in the beginning* of the output file.
	void includeAtStart(String output, String input, String... includes) {
		def tmpFile = createUniqueTmpFile()
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
		def tmpFile = createUniqueTmpFile()
		closure(tmpFile.getCanonicalPath(), input, includes)
		tmpFile.withInputStream { stream ->
			new File(output) << stream
		}
		FileUtils.deleteQuietly(tmpFile)
	}

	private static File createUniqueTmpFile() {
		Files.createTempFile(FileUtils.getTempDirectory().toPath(), "tmp", "pre").toFile()
	}
}
