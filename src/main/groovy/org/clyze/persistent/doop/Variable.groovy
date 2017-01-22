package org.clyze.persistent.doop

import groovy.transform.EqualsAndHashCode
import org.clyze.persistent.Symbol

/**
 * Created by anantoni on 1/10/2015.
 */
@EqualsAndHashCode
class Variable extends Symbol{

    String name
    String doopName
    String type
    String declaringMethodID
    boolean isLocal
    boolean isParameter

    Variable() {}

    Variable(Position position, String sourceFileName, String name, String doopName, String type, String declaringMethodID, boolean isLocal, boolean isParameter) {
        super(position, sourceFileName)
        this.name = name
        this.doopName = doopName
        this.type = type
        this.declaringMethodID = declaringMethodID
        this.isLocal = isLocal
        this.isParameter = isParameter
    }
}