package org.clyze.input

import org.clyze.analysis.InputType

/**
 * A resolver for inputs.
 */
interface InputResolver {

	String name()

	void resolve(String input, InputResolutionContext ctx, InputType inputType)
}
