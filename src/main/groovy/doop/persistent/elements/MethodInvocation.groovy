package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
class MethodInvocation extends Symbol {
    private String methodInvocationID;
    private Method invokingMethod;

    public MethodInvocation() {}

    public MethodInvocation(String methodInvocationID) {
        this.methodInvocationID = methodInvocationID;
    }

    public MethodInvocation(String methodInvocationID, Position position) {
        super(position);
        this.methodInvocationID = methodInvocationID;
    }
}
