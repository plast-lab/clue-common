package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
public class Class extends Symbol {
    private String name;

    public Class() {}

    public Class(String name) {
        this(null, name);
    }

    public Class(Position position, String name) {
        super(position);
        this.name = name;
    }
}