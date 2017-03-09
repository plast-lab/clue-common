package org.clyze.persistent.doop

import groovy.transform.EqualsAndHashCode
import org.clyze.persistent.Position
import org.clyze.persistent.Symbol


/**
 *
 */
@EqualsAndHashCode(callSuper = true)
class Variable extends Symbol {

    String name

    String type

    boolean isLocal

    boolean isParameter

    String declaringMethodDoopId

    String doopId

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
     * @param doopId
     * @param type
     * @param declaringMethodDoopId
     * @param isLocal
     * @param isParameter
     */
    Variable(Position position, String sourceFileName, String name, String doopId, String type, String declaringMethodDoopId,
             boolean isLocal, boolean isParameter) {

        super(position, sourceFileName)

        this.name = name
        this.doopId = doopId
        this.type = type
        this.declaringMethodDoopId = declaringMethodDoopId
        this.isLocal = isLocal
        this.isParameter = isParameter
    }

}
