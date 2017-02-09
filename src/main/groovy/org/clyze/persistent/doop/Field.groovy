package org.clyze.persistent.doop

import org.clyze.persistent.Symbol

/**
 * Created by anantoni on 2/10/2015.
 */
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
