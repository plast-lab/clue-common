package org.clyze.persistent.doop

import org.clyze.persistent.Position
import org.clyze.persistent.Symbol


/**
 *
 */
class Field extends Symbol {

    String name

    String type

    boolean isStatic

    /**
     * A "Class" symbol id
     */
    String declaringClassID

    /**
     * doop id
     */
    String signature

    /**
     *
     */
    Field() {

    }

    /**
     *
     * @param position
     * @param sourceFileName
     * @param name
     * @param signature
     * @param type
     * @param declaringClassID
     * @param isStatic
     */
    Field(Position position, String sourceFileName, String name, String signature, String type, String declaringClassID,
          boolean isStatic) {

        super(position, sourceFileName)

        this.name = name
        this.signature = signature
        this.type = type
        this.declaringClassID = declaringClassID
        this.isStatic = isStatic
    }

}
