package org.clyze.persistent.doop

import groovy.transform.EqualsAndHashCode
import org.clyze.persistent.Position
import org.clyze.persistent.Symbol


/**
 *
 */
@EqualsAndHashCode(callSuper = true)
class Method extends Symbol {

    String name

    String returnType

    /**
     * The parameter names
     */
    String[] params

    String[] paramTypes

    boolean isStatic

    String declaringClassDoopId

    int totalInvocations

    int totalAllocations

    String doopId

    /**
     *
     */
    public Method() {

    }

    /**
     *
     * @param position
     * @param sourceFileName
     * @param name
     * @param declaringClassDoopId
     * @param returnType
     * @param doopId
     * @param params
     * @param paramTypes
     * @param isStatic
     * @param totalInvocations
     * @param totalAllocations
     */
    public Method(Position position, String sourceFileName, String name, String declaringClassDoopId, String returnType,
                  String doopId, String[] params, String[] paramTypes, boolean isStatic, totalInvocations,
                  totalAllocations) {

        super(position, sourceFileName)

        this.name = name
        this.declaringClassDoopId = declaringClassDoopId
        this.returnType = returnType
        this.doopId = doopId
        this.params = params
        this.paramTypes = paramTypes
        this.isStatic = isStatic
        this.totalInvocations = totalInvocations
        this.totalAllocations = totalAllocations
    }

}
