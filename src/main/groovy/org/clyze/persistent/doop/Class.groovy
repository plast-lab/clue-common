package org.clyze.persistent.doop

import org.clyze.persistent.Position
import org.clyze.persistent.Symbol


/**
 * Symbol used for classes, interfaces and enums
 */
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
     *
     */
    Class() {

    }

    /**
     *
     * @param position
     * @param sourceFileName
     * @param name
     * @param packageName
     * @param isInterface
     * @param isEnum
     * @param isStatic
     * @param isInner
     * @param isAnonymous
     */
    Class(Position position, String sourceFileName, String name, String packageName, boolean isInterface, boolean isEnum,
          boolean isStatic, boolean isInner, boolean isAnonymous) {

        super(position, sourceFileName)

        this.name = name
        this.packageName = packageName
        this.isInterface = isInterface
        this.isEnum = isEnum
        this.isStatic = isStatic
        this.isInner = isInner
        this.isAnonymous = isAnonymous
    }

}
