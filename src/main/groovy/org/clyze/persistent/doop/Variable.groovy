package org.clyze.persistent.doop

import org.clyze.persistent.Position
import org.clyze.persistent.Symbol


/**
 *
 */
class Variable extends Symbol {

    String name

    String type

    boolean isLocal

    boolean isParameter

    /**
     * A "Method" symbol
     */
    String declaringMethodID

    /**
     * doop id
     */
    String doopName

    /**
     *
     */
    Variable() {

    }

    /**
     *
     * @param position
     * @param sourceFileName
     * @param name
     * @param doopName
     * @param type
     * @param declaringMethodID
     * @param isLocal
     * @param isParameter
     */
    Variable(Position position, String sourceFileName, String name, String doopName, String type, String declaringMethodID,
             boolean isLocal, boolean isParameter) {

        super(position, sourceFileName)

        this.name = name
        this.doopName = doopName
        this.type = type
        this.declaringMethodID = declaringMethodID
        this.isLocal = isLocal
        this.isParameter = isParameter
    }

}
