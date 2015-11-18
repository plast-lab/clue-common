package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
class MethodInvocation extends Symbol {

    String doopID;
    String invokingMethodID;

    public MethodInvocation() {}

    public MethodInvocation(Position position, String sourceFileName, String doopID, String invokingMethodID) {
        super(position, sourceFileName);
        this.doopID = doopID;
        this.invokingMethodID = invokingMethodID;
    }
}
