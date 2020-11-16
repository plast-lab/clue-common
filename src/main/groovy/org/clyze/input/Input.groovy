package org.clyze.input

import groovy.transform.CompileStatic

/**
 * The input to an analysis: a string that may correspond to one or more files.
 */
@CompileStatic
interface Input {

	String name()

	Set<File> files()
}
