package org.clyze.doop.persistent.elements

import groovy.transform.EqualsAndHashCode

/**
 * Created by anantoni on 2/10/2015.
 */
@EqualsAndHashCode
class Method extends Symbol {

    String name;
    String declaringClassID;
    String returnType;
    String doopSignature;
    String doopCompactName;
    String[] params;
    String[] paramTypes;
    boolean isStatic;
    int totalInvocations;
    int totalAllocations;

    public Method() {}

    public Method(Position position, String sourceFileName, String name, String declaringClassID, String returnType,
                  String doopSignature, String doopCompactName, String[] params, String[] paramTypes, boolean isStatic,
                  totalInvocations, totalAllocations)
    {
        super(position, sourceFileName);
        this.name = name;
        this.declaringClassID = declaringClassID;
        this.returnType = returnType;
        this.doopSignature = doopSignature;
        this.doopCompactName = doopCompactName;
        this.params = params;
        this.paramTypes = paramTypes;
        this.isStatic = isStatic;
        this.totalInvocations = totalInvocations;
        this.totalAllocations = totalAllocations;
    }
}
