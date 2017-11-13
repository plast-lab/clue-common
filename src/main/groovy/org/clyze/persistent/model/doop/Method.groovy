package org.clyze.persistent.model.doop

import groovy.transform.EqualsAndHashCode
import groovy.transform.InheritConstructors
import org.clyze.persistent.model.Position
import org.clyze.persistent.model.Symbol

@EqualsAndHashCode(callSuper = true)
@InheritConstructors
class Method extends Symbol {

	String name

	String returnType

	/**
	 * The parameter names
	 */
	String[] params

	String[] paramTypes

	boolean isStatic

	boolean isInterface

	boolean isAbstract

	boolean isNative

	String declaringClassDoopId

	int totalInvocations

	int totalAllocations

	String doopId

	/**
	 * The place where the method definition begins (including any annotations and modifiers) and ends (i.e. right after
	 * the closing brace). If the starting or ending line contain nothing else except the method definition and whitespaces,
	 * then the corresponding column is set to zero
	 *
	 * Note: Differs from the "position" field inherited from "Symbol" in that the latter refers to the starting and
	 *       ending positions of the method name inside its definition
	 *
	 * todo: Add a constructor parameter for this field too. For now we simply set its value with the auto-produced setter
	 */
	Position outerPosition

	/**
	 * @param position
	 * @param sourceFileName
	 * @param name
	 * @param declaringClassDoopId
	 * @param returnType
	 * @param doopId
	 * @param params
	 * @param paramTypes
	 * @param isStatic
	 * @param totalInvocations
	 * @param totalAllocations
	 */
	public Method(Position position, String sourceFileName, String name, String declaringClassDoopId, String returnType,
				  String doopId, String[] params, String[] paramTypes, boolean isStatic, totalInvocations,
				  totalAllocations) {
		super(position, sourceFileName)
		this.name = name
		this.declaringClassDoopId = declaringClassDoopId
		this.returnType = returnType
		this.doopId = doopId
		this.params = params
		this.paramTypes = paramTypes
		this.isStatic = isStatic
		this.totalInvocations = totalInvocations
		this.totalAllocations = totalAllocations
	}
}
