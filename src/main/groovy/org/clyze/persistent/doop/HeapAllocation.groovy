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

    String allocatingMethodDoopId

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
     * @param allocatingMethodDoopId
     */
    HeapAllocation(Position position, String sourceFileName, String doopId, String type, String allocatingMethodDoopId) {

        super(position, sourceFileName)

        this.doopId = doopId
        this.type = type
        this.allocatingMethodDoopId = allocatingMethodDoopId
    }

}
