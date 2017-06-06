package org.clyze.persistent.model.doop

import groovy.transform.EqualsAndHashCode
import groovy.transform.InheritConstructors
import org.clyze.persistent.model.Position
import org.clyze.persistent.model.Symbol

@EqualsAndHashCode(callSuper = true)
@InheritConstructors
class Variable extends Symbol {

	String name

	String type

	boolean isLocal

	boolean isParameter

	String declaringMethodDoopId

	String doopId

	/** is inside an instance initializer block */
	boolean inIIB

	/**
	 *
	 * @param position
	 * @param sourceFileName
	 * @param name
	 * @param doopId
	 * @param type
	 * @param declaringMethodDoopId
	 * @param isLocal
	 * @param isParameter
	 * @param inIIB                 is inside instance initializer block
	 */
	Variable(Position position, String sourceFileName, String name, String doopId, String type, String declaringMethodDoopId,
			 boolean isLocal, boolean isParameter, boolean inIIB = false) {
		super(position, sourceFileName)
		this.name = name
		this.doopId = doopId
		this.type = type
		this.declaringMethodDoopId = declaringMethodDoopId
		this.isLocal = isLocal
		this.isParameter = isParameter
		this.inIIB = inIIB
	}
}
