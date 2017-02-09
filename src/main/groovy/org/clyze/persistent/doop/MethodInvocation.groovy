package org.clyze.persistent.doop

import org.clyze.persistent.Symbol

/**
 * Created by anantoni on 2/10/2015.
 */
class MethodInvocation extends Symbol {

    String doopID
    String invokingMethodID

    MethodInvocation() {}

    MethodInvocation(Position position, String sourceFileName, String doopID, String invokingMethodID) {
        super(position, sourceFileName)
        this.doopID = doopID
        this.invokingMethodID = invokingMethodID
    }
}
