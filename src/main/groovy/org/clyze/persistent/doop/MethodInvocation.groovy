package org.clyze.persistent.doop

import groovy.transform.EqualsAndHashCode
import org.clyze.persistent.Position
import org.clyze.persistent.Symbol


/**
 *
 */
@EqualsAndHashCode(callSuper = true)
class MethodInvocation extends Symbol {

    String invokingMethodDoopId

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
     * @param invokingMethodDoopId
     */
    MethodInvocation(Position position, String sourceFileName, String doopId, String invokingMethodDoopId) {

        super(position, sourceFileName)

        this.doopId = doopId
        this.invokingMethodDoopId = invokingMethodDoopId
    }

}
