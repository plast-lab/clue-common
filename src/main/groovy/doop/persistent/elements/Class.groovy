package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
public class Class extends Symbol {
    private String name;

    public Class() {}

    public Class(Position position, String compilationUnit, String name) {
        super(position, compilationUnit);
        this.name = name;
    }
}