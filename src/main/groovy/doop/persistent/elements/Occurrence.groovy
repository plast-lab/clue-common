package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
class Occurrence<T extends Symbol> {

    Position position;
    T symbol;
    OccurrenceType occurrenceType;
    String sourceFileName;

    public Occurrence() {}

    public Occurrence(Position position, String sourceFileName, T symbol, OccurrenceType occurrenceType) {
        this.position = position;
        this.symbol = symbol;
        this.occurrenceType = occurrenceType;
        this.sourceFileName = sourceFileName;
    }
}

