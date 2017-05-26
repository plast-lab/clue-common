package org.clyze.persistent.doop

import groovy.transform.EqualsAndHashCode
import groovy.transform.InheritConstructors
import org.clyze.persistent.Position
import org.clyze.persistent.Symbol

@EqualsAndHashCode(callSuper = true)
@InheritConstructors
class Field extends Symbol {

	String name

	String type

	boolean isStatic

	String declaringClassDoopId

	String doopId

	/**
	 * @param position
	 * @param sourceFileName
	 * @param name
	 * @param doopId
	 * @param type
	 * @param declaringClassDoopId
	 * @param isStatic
	 */
	Field(Position position, String sourceFileName, String name, String doopId, String type, String declaringClassDoopId,
		  boolean isStatic) {
		super(position, sourceFileName)
		this.name = name
		this.doopId = doopId
		this.type = type
		this.declaringClassDoopId = declaringClassDoopId
		this.isStatic = isStatic
	}
}
