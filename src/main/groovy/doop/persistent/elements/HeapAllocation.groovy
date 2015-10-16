package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
class HeapAllocation extends Symbol {

    String type;
    String allocatingMethodID;
    String doopAllocationID;

    public HeapAllocation() {}

    public HeapAllocation(Position position, String sourceFileName, String doopAllocationID, String type, String allocatingMethodID) {
        super(position, sourceFileName);
        this.doopAllocationID = doopAllocationID;
        this.type = type;
        this.allocatingMethodID = allocatingMethodID;
    }
}
