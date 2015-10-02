package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
class HeapAllocation extends Symbol {

    private String type;
    private Method enclosingMethod;
    private String doopAllocationID;

    public HeapAllocation() {}

    public HeapAllocation(String doopAllocationName) {
        this(null, doopAllocationName);
    }

    public HeapAllocation(Position position, String doopAllocationID) {
        this(position, doopAllocationID, null, null);
    }

    public HeapAllocation(Position position, String doopAllocationID, String type) {
        this(position, doopAllocationID, type, null);
    }

    private HeapAllocation(Position position, String doopAllocationID, String type, Method enclosingMethod) {
        this.position = position;
        this.doopAllocationID = doopAllocationID;
        this.type = type;
        this.enclosingMethod = enclosingMethod;
    }
}
