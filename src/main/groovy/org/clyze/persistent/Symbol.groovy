package org.clyze.persistent

import groovy.transform.EqualsAndHashCode
import groovy.transform.InheritConstructors

import static java.util.UUID.randomUUID

/**
 * A symbol element that exists in source code
 */
@EqualsAndHashCode
@InheritConstructors
abstract class Symbol extends Element {

	Position position

	String sourceFileName

	/**
	 * @param position The symbol position in the source code
	 * @param sourceFileName The source code file name
	 */
	Symbol(Position position, String sourceFileName) {
		this.position = position
		this.sourceFileName = sourceFileName
		this.id = randomUUID().toString()
	}
}
