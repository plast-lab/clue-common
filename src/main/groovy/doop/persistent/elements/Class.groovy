package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
class Class extends Symbol {
    String name;

    public Class() {}

    public Class(Position position, String sourceFileName, String name) {
        super(position, sourceFileName);
        this.name = name;
    }
}