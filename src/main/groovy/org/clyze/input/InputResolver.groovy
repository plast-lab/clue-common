package org.clyze.input

import groovy.transform.CompileStatic
import org.clyze.analysis.InputType

/**
 * A resolver for inputs.
 */
@CompileStatic
interface InputResolver {

	String name()

	void resolve(String input, InputResolutionContext ctx, InputType inputType)
}
