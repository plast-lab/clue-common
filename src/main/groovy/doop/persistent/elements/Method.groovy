package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
class Method extends Symbol {

    String name;
    String declaringClassID;
    String returnType;
    String doopSignature;
    String doopCompactName;
    String[] params;
    String[] paramTypes;

    public Method() {}

    public Method(Position position, String sourceFileName, String name, String declaringClassID, String returnType,
                  String doopSignature, String doopCompactName, String[] params, String[] paramTypes)
    {
        super(position, sourceFileName);
        this.name = name;
        this.declaringClassID = declaringClassID;
        this.returnType = returnType;
        this.doopSignature = doopSignature;
        this.doopCompactName = doopCompactName;
        this.params = params;
        this.paramTypes = paramTypes;
    }
}
