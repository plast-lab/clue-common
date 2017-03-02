package org.clyze.persistent.doop

import groovy.transform.EqualsAndHashCode
import org.clyze.persistent.Position
import org.clyze.persistent.Symbol


/**
 *
 */
@EqualsAndHashCode(callSuper = true)
class HeapAllocation extends Symbol {

    String type

    /**
     * A "Method" symbol id
     */
    String allocatingMethodId

    String doopId

    /**
     *
     */
    HeapAllocation() {}

    /**
     *
     * @param position
     * @param sourceFileName
     * @param doopId
     * @param type
     * @param allocatingMethodId
     */
    HeapAllocation(Position position, String sourceFileName, String doopId, String type, String allocatingMethodId) {

        super(position, sourceFileName)

        this.doopId = doopId
        this.type = type
        this.allocatingMethodId = allocatingMethodId
    }

}
