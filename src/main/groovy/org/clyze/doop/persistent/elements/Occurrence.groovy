package org.clyze.doop.persistent.elements

import groovy.transform.EqualsAndHashCode

/**
 * Created by anantoni on 2/10/2015.
 */
//@EqualsAndHashCode
class Occurrence extends Symbol {

    String symbolID;
    OccurrenceType occurrenceType;

    public Occurrence() {}

    public Occurrence(Position position, String sourceFileName, String symbolID, OccurrenceType occurrenceType) {
        super(position, sourceFileName);
        this.symbolID = symbolID;
        this.occurrenceType = occurrenceType;
    }
}

