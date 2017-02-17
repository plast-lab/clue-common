package org.clyze.persistent.doop

import org.clyze.persistent.Position
import org.clyze.persistent.Symbol


/**
 *
 */
class MethodInvocation extends Symbol {

    /**
     * A "Method" symbol id
     */
    String invokingMethodId

    String doopId

    /**
     *
     */
    MethodInvocation() {

    }

    /**
     *
     * @param position
     * @param sourceFileName
     * @param doopId
     * @param invokingMethodId
     */
    MethodInvocation(Position position, String sourceFileName, String doopId, String invokingMethodId) {

        super(position, sourceFileName)

        this.doopId = doopId
        this.invokingMethodId = invokingMethodId
    }

}
