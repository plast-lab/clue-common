package org.clyze.analysis

import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

@CompileStatic
@InheritConstructors
class BooleanAnalysisOption extends AnalysisOption<Boolean> {
	/**
	 * The value of the option
	 */
	Boolean value = false
}
