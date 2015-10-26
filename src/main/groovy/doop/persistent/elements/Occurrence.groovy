package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
class Occurrence {

    Position position;
    String sourceFileName;
    String symbolID;
    OccurrenceType occurrenceType;

    public Occurrence() {}

    public Occurrence(Position position, String sourceFileName, String symbolID, OccurrenceType occurrenceType) {
        this.position = position;
        this.sourceFileName = sourceFileName;
        this.symbolID = symbolID;
        this.occurrenceType = occurrenceType;
    }
}

