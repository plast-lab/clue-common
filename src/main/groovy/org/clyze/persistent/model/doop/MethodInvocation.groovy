package org.clyze.persistent.model.doop

import groovy.transform.EqualsAndHashCode
import groovy.transform.InheritConstructors
import org.clyze.persistent.model.Position
import org.clyze.persistent.model.Symbol

@EqualsAndHashCode(callSuper = true)
@InheritConstructors
class MethodInvocation extends Symbol {

	String name

	String invokingMethodDoopId

	String doopId

	/** is inside instance initializer block */
	boolean inIIB

	/**
	 *
	 * @param position
	 * @param sourceFileName
	 * @param doopId
	 * @param invokingMethodDoopId
	 * @param inIIB                 is inside instance initializer block
	 */
	MethodInvocation(Position position, String sourceFileName, String name, String doopId, String invokingMethodDoopId,
					 boolean inIIB = false) {
		super(position, sourceFileName)
		this.name = name
		this.doopId = doopId
		this.invokingMethodDoopId = invokingMethodDoopId
		this.inIIB = inIIB
	}
}
