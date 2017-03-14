package org.clyze.persistent.doop

import groovy.transform.EqualsAndHashCode
import org.clyze.persistent.Position
import org.clyze.persistent.Symbol


/**
 *
 */
@EqualsAndHashCode(callSuper = true)
class Usage extends Symbol {

    UsageKind usageKind

    String doopId

    /**
     *
     */
    public Usage() {

    }

    /**
     *
     * @param position
     * @param sourceFileName
     * @param symbolId
     * @param usageKind
     */
    public Usage(Position position, String sourceFileName, String doopId, UsageKind usageKind) {

        super(position, sourceFileName)

        this.doopId = doopId
        this.usageKind = usageKind
    }

}
