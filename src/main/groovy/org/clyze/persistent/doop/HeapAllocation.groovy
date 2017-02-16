package org.clyze.persistent.doop

import org.clyze.persistent.Position
import org.clyze.persistent.Symbol


/**
 *
 */
class HeapAllocation extends Symbol {

    String type

    /**
     * A "Method" symbol id
     */
    String allocatingMethodID

    /**
     * doop id
     */
    String doopID

    /**
     *
     */
    HeapAllocation() {}

    /**
     *
     * @param position
     * @param sourceFileName
     * @param doopID
     * @param type
     * @param allocatingMethodID
     */
    HeapAllocation(Position position, String sourceFileName, String doopID, String type, String allocatingMethodID) {

        super(position, sourceFileName)

        this.doopID = doopID
        this.type = type
        this.allocatingMethodID = allocatingMethodID
    }

}
