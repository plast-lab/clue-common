package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
public class Occurrence<T> {
    private Position position;
    private T symbol;
    private OccurrenceType occurrenceType;
    private String compilationUnit;

    public Occurrence() {}

    public Occurrence(Position position, String compilationUnit, T symbol, OccurrenceType occurrenceType) {
        this.position = position;
        this.symbol = symbol;
        this.occurrenceType = occurrenceType;
        this.compilationUnit = compilationUnit;
    }
}

