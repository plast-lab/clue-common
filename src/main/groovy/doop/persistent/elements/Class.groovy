package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
class Class extends Symbol {
    String name;

    public Class() {}

    public Class(Position position, String compilationUnit, String name) {
        super(position, compilationUnit);
        this.name = name;
    }
}