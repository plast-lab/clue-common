package org.clyze.doop.persistent.elements

import groovy.transform.EqualsAndHashCode

/**
 * Created by anantoni on 2/10/2015.
 */
@EqualsAndHashCode
class Field extends Symbol {
    String name
    String signature
    String type
    String declaringClassID
    boolean isStatic

    Field() {}

    Field(Position position, String sourceFileName, String name, String signature, String type, String declaringClassID, boolean isStatic) {
        super(position, sourceFileName)
        this.name = name
        this.signature = signature
        this.type = type
        this.declaringClassID = declaringClassID
        this.isStatic = isStatic
    }

}
