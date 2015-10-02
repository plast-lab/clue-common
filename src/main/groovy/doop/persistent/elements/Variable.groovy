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

    Variable(String compilationUnit, Position position, String name, String doopName) {
        super(compilationUnit, position);
        this.name = name;
        this.doopName = doopName;
    }
}
