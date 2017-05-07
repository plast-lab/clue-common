package org.clyze.persistent.doop

import groovy.transform.EqualsAndHashCode
import groovy.transform.InheritConstructors
import org.clyze.persistent.Position
import org.clyze.persistent.Symbol

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

    String doopId

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
          boolean isInterface, boolean isEnum, boolean isStatic, boolean isInner, boolean isAnonymous) {
        super(position, sourceFileName)
        this.name = name
        this.packageName = packageName
        this.doopId = doopId
        this.isInterface = isInterface
        this.isEnum = isEnum
        this.isStatic = isStatic
        this.isInner = isInner
        this.isAnonymous = isAnonymous
    }
}
