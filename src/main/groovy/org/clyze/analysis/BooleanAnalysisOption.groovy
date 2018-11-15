package org.clyze.analysis

import groovy.transform.InheritConstructors

@InheritConstructors
class BooleanAnalysisOption extends AnalysisOption<Boolean> {
	/**
	 * The value of the option
	 */
	Boolean value = false
}
