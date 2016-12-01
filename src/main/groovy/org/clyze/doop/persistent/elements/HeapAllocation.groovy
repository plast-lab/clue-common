package org.clyze.doop.persistent.elements

import groovy.transform.EqualsAndHashCode

/**
 * Created by anantoni on 2/10/2015.
 */
@EqualsAndHashCode
class HeapAllocation extends Symbol {

    String type
    String allocatingMethodID
    String doopID

    HeapAllocation() {}

    HeapAllocation(Position position, String sourceFileName, String doopID, String type, String allocatingMethodID) {
        super(position, sourceFileName)
        this.doopID = doopID
        this.type = type
        this.allocatingMethodID = allocatingMethodID
    }
}
