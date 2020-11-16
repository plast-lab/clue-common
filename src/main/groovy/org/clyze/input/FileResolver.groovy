package org.clyze.input

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.clyze.analysis.InputType
import org.clyze.utils.FileOps

/**
 * Resolves the input as a local file.
 */
@CompileStatic
@TupleConstructor
class FileResolver implements InputResolver {

	File dir

	String name() { "file (${dir?.absolutePath})" }

	void resolve(String input, InputResolutionContext ctx, InputType inputType) {
		if (dir && input.contains(".."))
			throw new RuntimeException("Not a valid file: $input")

		def inputFile = dir ? new File(dir, input) : new File(input)
		def file = FileOps.findFileOrThrow(inputFile, "Not a file input: $inputFile.name")
		ctx.set(input, file, inputType)
	}
}
