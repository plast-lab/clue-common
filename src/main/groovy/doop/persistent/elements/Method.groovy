package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
class Method extends Symbol {
    private String name;
    private Class enclosingClass;
    private String returnType;
    private String signature;
    private String doopSignature;
    private String doopCompactName;
    private String[] args;
    private String[] argTypes;

    public Method() {}

    public Method(Position position, String compilationUnit, String name, Class enclosingClass, String returnType,
                  String signature, String doopSignature, String doopCompactName, String[] args, String[] argTypes)
    {

        super(position, compilationUnit);
        this.name = name;
        this.enclosingClass = enclosingClass;
        this.returnType = returnType;
        this.signature = signature;
        this.doopSignature = doopSignature;
        this.doopCompactName = doopCompactName;
        this.args = args;
        this.argTypes = argTypes;
    }
}
