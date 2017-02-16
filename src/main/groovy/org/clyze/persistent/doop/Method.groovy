package org.clyze.persistent.doop

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
    String declaringClassID

    /**
     * doop id
     */
    String doopSignature

    /**
     * TODO: deprecated; to be removed
     */
    String doopCompactName

    int totalInvocations

    int totalAllocations

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
     * @param declaringClassID
     * @param returnType
     * @param doopSignature
     * @param doopCompactName
     * @param params
     * @param paramTypes
     * @param isStatic
     * @param totalInvocations
     * @param totalAllocations
     */
    public Method(Position position, String sourceFileName, String name, String declaringClassID, String returnType,
                  String doopSignature, String doopCompactName, String[] params, String[] paramTypes, boolean isStatic,
                  totalInvocations, totalAllocations) {

        super(position, sourceFileName)

        this.name = name
        this.declaringClassID = declaringClassID
        this.returnType = returnType
        this.doopSignature = doopSignature
        this.doopCompactName = doopCompactName
        this.params = params
        this.paramTypes = paramTypes
        this.isStatic = isStatic
        this.totalInvocations = totalInvocations
        this.totalAllocations = totalAllocations
    }

}
