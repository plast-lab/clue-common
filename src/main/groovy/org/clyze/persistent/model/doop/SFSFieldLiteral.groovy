package org.clyze.persistent.model.doop

import groovy.transform.EqualsAndHashCode
import groovy.transform.InheritConstructors
import org.clyze.persistent.model.Position
import org.clyze.persistent.model.Symbol

@EqualsAndHashCode(callSuper = true)
@InheritConstructors
class SFSFieldLiteral extends Symbol {

	/** The doopId of the field that this literal value is assigned to. */
	String fieldDoopId

	/** The static final field's initialization string value.
	 *
	 *  This value may differ from the corresponding source code part, since it
	 *  is retrieved after the compiler has applied any possible concatenations.
	 *
	 *  e.g. The `static final String foo = "abc" + "def";` will get stored as "abcdef"
	 */
	String value

	/**
	 *
	 * @param position
	 * @param sourceFileName
	 * @param fieldDoopId
	 */
	public SFSFieldLiteral(Position position, String sourceFileName, String fieldDoopId, String value) {
		super(position, sourceFileName)
		this.fieldDoopId = fieldDoopId
		this.value = value
	}
}
