package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
class HeapAllocation extends Symbol {

    private String type;
    private Method enclosingMethod;
    private String doopAllocationID;

    public HeapAllocation() {}

    private HeapAllocation(Position position, String compilationUnit, String doopAllocationID, String type, Method enclosingMethod) {
        super(position, compilationUnit, null);
        this.doopAllocationID = doopAllocationID;
        this.type = type;
        this.enclosingMethod = enclosingMethod;
    }
}
