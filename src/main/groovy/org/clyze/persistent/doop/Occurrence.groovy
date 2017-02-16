package org.clyze.persistent.doop

import org.clyze.persistent.Symbol


/**
 *
 */
class Occurrence extends Symbol {

    /**
     *
     */
    OccurrenceType occurrenceType

    /**
     * A "Symbol" id
     */
    String symbolID

    /**
     *
     */
    public Occurrence() {

    }

    /**
     *
     * @param position
     * @param sourceFileName
     * @param symbolID
     * @param occurrenceType
     */
    public Occurrence(Position position, String sourceFileName, String symbolID, OccurrenceType occurrenceType) {

        super(position, sourceFileName)

        this.symbolID = symbolID
        this.occurrenceType = occurrenceType
    }

}
