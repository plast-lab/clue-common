package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
class MethodInvocation extends Symbol {

    String methodInvocationID;
    String invokingMethodUUID;

    public MethodInvocation() {}

    public MethodInvocation(Position position, String sourceFileName, String methodInvocationID, String invokingMethodUUID) {
        super(position, sourceFileName);
        this.methodInvocationID = methodInvocationID;
        this.invokingMethodUUID = invokingMethodUUID;
    }
}
