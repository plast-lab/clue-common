package org.clyze.persistent.doop

import groovy.transform.EqualsAndHashCode
import groovy.transform.InheritConstructors
import org.clyze.persistent.Position
import org.clyze.persistent.Symbol

@EqualsAndHashCode(callSuper = true)
@InheritConstructors
class MethodInvocation extends Symbol {

	String invokingMethodDoopId

	String doopId

	/**
	 * @param position
	 * @param sourceFileName
	 * @param doopId
	 * @param invokingMethodDoopId
	 */
	MethodInvocation(Position position, String sourceFileName, String doopId, String invokingMethodDoopId) {
		super(position, sourceFileName)
		this.doopId = doopId
		this.invokingMethodDoopId = invokingMethodDoopId
	}
}
