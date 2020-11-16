package org.clyze.input

import groovy.transform.CompileStatic
import groovy.util.logging.Log4j
import org.clyze.analysis.InputType

@CompileStatic
@Log4j
class ChainResolver implements InputResolver {

	private final List<InputResolver> resolvers

	ChainResolver(InputResolver... resolvers) {
		this.resolvers = resolvers.toList()
	}

	String name() { "chain" }

	void resolve(String input, InputResolutionContext ctx, InputType inputType) {
		for (InputResolver resolver : resolvers) {
			try {
				log.debug "Resolving input: $input via ${resolver.name()}"
				resolver.resolve(input, ctx, inputType)
				log.debug "Resolved input $input -> ${ctx.get(input, inputType)}"
				return
			}
			catch (e) {
				log.debug e.message
				//log.warn Helper.stackTraceToString(e)
			}
		}

		throw new RuntimeException("Not a valid input: $input")
	}
}
