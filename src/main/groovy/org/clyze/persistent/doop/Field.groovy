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
    String declaringClassId

    String doopId

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
     * @param doopId
     * @param type
     * @param declaringClassId
     * @param isStatic
     */
    Field(Position position, String sourceFileName, String name, String doopId, String type, String declaringClassId,
          boolean isStatic) {

        super(position, sourceFileName)

        this.name = name
        this.doopId = doopId
        this.type = type
        this.declaringClassId = declaringClassId
        this.isStatic = isStatic
    }

}
