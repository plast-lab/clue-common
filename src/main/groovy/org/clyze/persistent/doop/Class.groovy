package org.clyze.persistent.doop

import groovy.transform.EqualsAndHashCode
import org.clyze.persistent.Symbol

/**
 * Created by anantoni on 2/10/2015.
 */
@EqualsAndHashCode
class Class extends Symbol {
    String name
    String packageName
    /**
     * Possibly change this to bitmap.
     */
    boolean isInterface
    boolean isEnum
    boolean isStatic
    boolean isInner
    boolean isAnonymous

    Class() {}

    Class(Position position, String sourceFileName, String name, String packageName, boolean isInterface, boolean isEnum, boolean isStatic, boolean isInner, boolean isAnonymous) {
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