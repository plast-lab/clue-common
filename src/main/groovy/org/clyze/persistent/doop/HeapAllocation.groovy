package org.clyze.persistent.doop

import groovy.transform.EqualsAndHashCode
import groovy.transform.InheritConstructors
import org.clyze.persistent.Position
import org.clyze.persistent.Symbol

@EqualsAndHashCode(callSuper = true)
@InheritConstructors
class HeapAllocation extends Symbol {

	String type

	String allocatingMethodDoopId

	String doopId

	/**
	 * @param position
	 * @param sourceFileName
	 * @param doopId
	 * @param type
	 * @param allocatingMethodDoopId
	 */
	HeapAllocation(Position position, String sourceFileName, String doopId, String type, String allocatingMethodDoopId) {
		super(position, sourceFileName)
		this.doopId = doopId
		this.type = type
		this.allocatingMethodDoopId = allocatingMethodDoopId
	}
}
