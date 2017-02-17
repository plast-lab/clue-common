package org.clyze.persistent.doop

import org.clyze.persistent.Position
import org.clyze.persistent.Symbol


/**
 *
 */
class Occurrence extends Symbol {

    OccurrenceType occurrenceType

    /**
     * A "Symbol" id
     */
    String symbolId

    /**
     *
     */
    public Occurrence() {

    }

    /**
     *
     * @param position
     * @param sourceFileName
     * @param symbolId
     * @param occurrenceType
     */
    public Occurrence(Position position, String sourceFileName, String symbolId, OccurrenceType occurrenceType) {

        super(position, sourceFileName)

        this.symbolId = symbolId
        this.occurrenceType = occurrenceType
    }

}
