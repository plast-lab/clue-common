package org.clyze.persistent.doop

import org.clyze.persistent.Position
import org.clyze.persistent.Symbol


/**
 *
 */
class Method extends Symbol {

    String name

    String returnType

    /**
     * The parameter names
     */
    String[] params

    String[] paramTypes

    boolean isStatic

    /**
     * A "Class" symbol id
     */
    String declaringClassId

    int totalInvocations

    int totalAllocations

    String doopId

    /**
     * TODO: deprecated; to be removed
     */
    String doopCompactName

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
     * @param declaringClassId
     * @param returnType
     * @param doopId
     * @param doopCompactName
     * @param params
     * @param paramTypes
     * @param isStatic
     * @param totalInvocations
     * @param totalAllocations
     */
    public Method(Position position, String sourceFileName, String name, String declaringClassId, String returnType,
                  String doopId, String doopCompactName, String[] params, String[] paramTypes, boolean isStatic,
                  totalInvocations, totalAllocations) {

        super(position, sourceFileName)

        this.name = name
        this.declaringClassId = declaringClassId
        this.returnType = returnType
        this.doopId = doopId
        this.doopCompactName = doopCompactName
        this.params = params
        this.paramTypes = paramTypes
        this.isStatic = isStatic
        this.totalInvocations = totalInvocations
        this.totalAllocations = totalAllocations
    }

}
