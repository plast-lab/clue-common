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

    public Method(Position position) {
        this(position, null, null, null, null, null, null, null, null);
    }

    public Method(Position position, String name) {
        this(position, name, null, null, null, null, null, null, null);
    }

    public Method(Position position, String name, Class enclosingClass) {
        this(position, name, enclosingClass, null, null, null, null, null, null);
    }

    public Method(Position position, String name, Class enclosingClass, String returnType) {
        this(position, name, enclosingClass, returnType, null, null, null, null, null);
    }

    public Method(Position position, String name, Class enclosingClass, String returnType, String doopSignature) {
        this(position, name, enclosingClass, returnType, doopSignature, null, null, null, null);
    }

    public Method(Position position, String name, Class enclosingClass, String returnType, String doopSignature, String doopCompactName) {
        this(position, name, enclosingClass, returnType, doopSignature, doopCompactName, null, null, null);
    }

    public Method(Position position, String name, Class enclosingClass, String returnType, String doopSignature, String doopCompactName, String[] args) {
        this(position, name, enclosingClass, returnType, doopSignature, doopSignature, doopCompactName, args, null);
    }

    public Method(Position position, String name, Class enclosingClass, String returnType, String signature, String doopSignature,
                  String doopCompactName, String[] args, String[] argTypes) {
        super(position);
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
