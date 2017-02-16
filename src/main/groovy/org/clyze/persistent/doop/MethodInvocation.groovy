package org.clyze.persistent.doop

import org.clyze.persistent.Symbol


/**
 *
 */
class MethodInvocation extends Symbol {

    /**
     * A "Method" symbol id
     */
    String invokingMethodID

    /**
     * doop id
     */
    String doopID

    /**
     *
     */
    MethodInvocation() {

    }

    /**
     *
     * @param position
     * @param sourceFileName
     * @param doopID
     * @param invokingMethodID
     */
    MethodInvocation(Position position, String sourceFileName, String doopID, String invokingMethodID) {

        super(position, sourceFileName)

        this.doopID = doopID
        this.invokingMethodID = invokingMethodID
    }

}
