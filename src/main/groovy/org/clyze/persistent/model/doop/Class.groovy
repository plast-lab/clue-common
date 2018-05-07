package org.clyze.persistent.model.doop

import groovy.transform.EqualsAndHashCode
import groovy.transform.InheritConstructors
import org.clyze.persistent.model.Position
import org.clyze.persistent.model.Symbol

/**
 * Symbol used for classes, interfaces and enums
 */
@EqualsAndHashCode(callSuper = true)
@InheritConstructors
class Class extends Symbol {

	/**
	 * The symbol name (package name not included)
	 */
	String name
	
	/** The artifact name (e.g. foo-1.2.jar) */
	String artifactName

	String packageName

	/**
	 * Various flags determining the symbol type
	 * (possibly change this to bitmap)
	 */
	boolean isInterface
	boolean isEnum
	boolean isStatic
	boolean isInner
	boolean isAnonymous
	boolean isAbstract

	String doopId

	/**
	 * The doopId of the type or method where this type is declared.
	 */
	String declaringSymbolDoopId

	long sizeInBytes

	/**
	 * @param position
	 * @param sourceFileName
	 * @param name
	 * @param packageName
	 * @param doopId
	 * @param isInterface
	 * @param isEnum
	 * @param isStatic
	 * @param isInner
	 * @param isAnonymous
	 */
	Class(Position position, String sourceFileName, String name, String packageName, String doopId,
		  boolean isInterface, boolean isEnum, boolean isStatic, boolean isInner, boolean isAnonymous, boolean isAbstract, long sizeInBytes=0) {
		super(position, sourceFileName)
		this.name = name
		this.packageName = packageName
		this.doopId = doopId
		this.isInterface = isInterface
		this.isEnum = isEnum
		this.isStatic = isStatic
		this.isInner = isInner
		this.isAnonymous = isAnonymous
		this.isAbstract = isAbstract
		this.sizeInBytes = sizeInBytes
	}
}
