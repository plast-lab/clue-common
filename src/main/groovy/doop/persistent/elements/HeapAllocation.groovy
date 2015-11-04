package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
class HeapAllocation extends Symbol {

    String type;
    String allocatingMethodID;
    String doopID;

    public HeapAllocation() {}

    public HeapAllocation(Position position, String sourceFileName, String doopID, String type, String allocatingMethodID) {
        super(position, sourceFileName);
        this.doopID = doopID;
        this.type = type;
        this.allocatingMethodID = allocatingMethodID;
    }
}
