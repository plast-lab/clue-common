package org.clyze.persistent.model.doop

import groovy.transform.EqualsAndHashCode
import groovy.transform.InheritConstructors
import org.clyze.persistent.model.Position
import org.clyze.persistent.model.Symbol

@EqualsAndHashCode(callSuper = true)
@InheritConstructors
class HeapAllocation extends Symbol {

	String allocatedTypeDoopId

	String allocatingMethodDoopId

	String doopId

	/** is inside instance initializer block */
	boolean inIIB

	/**
	 *
	 * @param position
	 * @param sourceFileName
	 * @param doopId
	 * @param allocatedTypeDoopId
	 * @param allocatingMethodDoopId
	 * @param inIIB is inside instance initializer block
	 */
	HeapAllocation(Position position, String sourceFileName, String doopId, String allocatedTypeDoopId, String allocatingMethodDoopId,
				   boolean inIIB = false) {
		super(position, sourceFileName)
		this.doopId = doopId
		this.allocatedTypeDoopId = allocatedTypeDoopId
		this.allocatingMethodDoopId = allocatingMethodDoopId
		this.inIIB = inIIB
	}
}
