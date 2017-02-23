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

    /**
     * A "Method" symbol id
     */
    String declaringMethodId

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
     * @param declaringMethodId
     * @param isLocal
     * @param isParameter
     */
    Variable(Position position, String sourceFileName, String name, String doopId, String type, String declaringMethodId,
             boolean isLocal, boolean isParameter) {

        super(position, sourceFileName)

        this.name = name
        this.doopId = doopId
        this.type = type
        this.declaringMethodId = declaringMethodId
        this.isLocal = isLocal
        this.isParameter = isParameter
    }

}
