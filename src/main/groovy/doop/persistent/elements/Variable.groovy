package doop.persistent.elements

/**
 * Created by anantoni on 1/10/2015.
 */
class Variable extends Symbol{
    private String name;
    private String doopName;
    private String type;
    private Method declaringMethod;

    Variable() {}

    Variable(Position position, String compilationUnit, String name, String doopName, String type, Method declaringMethod) {
        super(position, compilationUnit,);
        this.name = name;
        this.doopName = doopName;
        this.type = type;
        this.declaringMethod = declaringMethod;
    }
}
