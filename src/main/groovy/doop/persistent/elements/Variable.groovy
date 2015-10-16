package doop.persistent.elements

/**
 * Created by anantoni on 1/10/2015.
 */
class Variable extends Symbol{

    String name;
    String doopName;
    String type;
    String declaringMethodID;

    Variable() {}

    Variable(Position position, String sourceFileName, String name, String doopName, String type, String declaringMethodID) {
        super(position, sourceFileName);
        this.name = name;
        this.doopName = doopName;
        this.type = type;
        this.declaringMethodID = declaringMethodID;
    }
}
