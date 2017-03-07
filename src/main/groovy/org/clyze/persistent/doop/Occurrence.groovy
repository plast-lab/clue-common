package org.clyze.persistent.doop

import groovy.transform.EqualsAndHashCode
import org.clyze.persistent.Position
import org.clyze.persistent.Symbol


/**
 *
 */
@EqualsAndHashCode(callSuper = true)
class Occurrence extends Symbol {

    OccurrenceType occurrenceType

    String doopId

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
    public Occurrence(Position position, String sourceFileName, String doopId, OccurrenceType occurrenceType) {

        super(position, sourceFileName)

        this.doopId = doopId
        this.occurrenceType = occurrenceType
    }

}
